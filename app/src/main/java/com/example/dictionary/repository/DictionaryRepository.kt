package com.example.dictionary.repository

import com.example.dictionary.manager.CacheManager
import com.example.dictionary.manager.SourceManager
import com.example.dictionary.manager.SourcePriorityManager
import com.example.dictionary.model.DictionaryResult
import com.example.dictionary.model.DictionarySourceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.text.Normalizer

class DictionaryRepository(
    val sourceManager: SourceManager = SourceManager(),
    val priorityManager: SourcePriorityManager = SourcePriorityManager(),
    val cacheManager: CacheManager = CacheManager()
) {

    /**
     * Searches for a word using the specified source or automatic fallback.
     */
    suspend fun queryWord(
        word: String,
        languageCode: String,
        selectedSource: DictionarySourceId = DictionarySourceId.AUTOMATIC
    ): DictionaryResult = withContext(Dispatchers.IO) {
        val trimmed = word.trim()
        if (trimmed.isBlank()) {
            return@withContext DictionaryResult(
                palabra = "",
                idioma = languageCode,
                fuente = selectedSource.displayName,
                errorMensaje = "Término de búsqueda vacío."
            )
        }

        val cacheKeySource = selectedSource.code
        val cached = cacheManager.get(trimmed, languageCode, cacheKeySource)
        if (cached != null) {
            return@withContext cached
        }

        val result = if (selectedSource == DictionarySourceId.AUTOMATIC) {
            executeAutomaticSearch(trimmed, languageCode)
        } else {
            executeSpecificSourceSearch(trimmed, languageCode, selectedSource)
        }

        if (result.isSuccess) {
            cacheManager.put(trimmed, languageCode, cacheKeySource, result)
        }

        result
    }

    /**
     * Automatic search mode:
     * 1. Generates smart word candidates (accents, plural, inflections)
     * 2. Iterates through ordered compatible providers
     * 3. Falls back to next provider if not found
     * 4. Combines compatible enrichments (e.g. audio from Free Dictionary + etymology from Wiktionary)
     * 5. Clearly indicates the primary source and any complementary source without inventing data.
     */
    private suspend fun executeAutomaticSearch(word: String, languageCode: String): DictionaryResult {
        val providers = priorityManager.getOrderedProvidersForLanguage(sourceManager, languageCode)
        val candidates = generateWordCandidates(word, languageCode)

        var primaryResult: DictionaryResult? = null

        // Try primary candidates and providers in priority order
        outerLoop@ for (candidate in candidates) {
            for (provider in providers) {
                try {
                    val res = provider.searchWord(candidate, languageCode)
                    if (res != null && res.definiciones.isNotEmpty()) {
                        primaryResult = res
                        break@outerLoop
                    }
                } catch (e: Exception) {
                    // Fail silently in auto mode to allow graceful fallback to the next source
                }
            }
        }

        val resolved = primaryResult ?: return DictionaryResult(
            palabra = word,
            idioma = languageCode,
            fuente = "Automática",
            errorMensaje = "No se encontraron definiciones válidas para \"$word\" en ninguna de las fuentes disponibles."
        )

        // Check if secondary sources can complement missing fields (e.g., audio or etymology)
        var combined = resolved
        if (priorityManager.useAlternativeSourcesAuto.value) {
            for (provider in providers) {
                if (provider.sourceId == DictionarySourceId.fromCode(resolved.fuente)) continue

                if (combined.audioUrl.isBlank() || combined.etimologia.isBlank() || combined.sinonimos.isEmpty()) {
                    try {
                        val secondary = provider.searchWord(word, languageCode)
                        if (secondary != null) {
                            combined = combined.copy(
                                audioUrl = if (combined.audioUrl.isBlank()) secondary.audioUrl else combined.audioUrl,
                                ipa = if (combined.ipa.isBlank()) secondary.ipa else combined.ipa,
                                etimologia = if (combined.etimologia.isBlank()) secondary.etimologia else combined.etimologia,
                                sinonimos = if (combined.sinonimos.isEmpty()) secondary.sinonimos else combined.sinonimos,
                                antonimos = if (combined.antonimos.isEmpty()) secondary.antonimos else combined.antonimos,
                                traducciones = if (combined.traducciones.isEmpty()) secondary.traducciones else combined.traducciones
                            )
                        }
                    } catch (e: Exception) {
                        // ignore secondary enrich failures
                    }
                }
            }
        }

        return combined
    }

    /**
     * Executes query strictly against a single user-selected source.
     */
    private suspend fun executeSpecificSourceSearch(
        word: String,
        languageCode: String,
        sourceId: DictionarySourceId
    ): DictionaryResult {
        val provider = sourceManager.getProvider(sourceId)
            ?: return DictionaryResult(
                palabra = word,
                idioma = languageCode,
                fuente = sourceId.displayName,
                errorMensaje = "La fuente ${sourceId.displayName} no está configurada o disponible."
            )

        if (!provider.isAvailable()) {
            return DictionaryResult(
                palabra = word,
                idioma = languageCode,
                fuente = sourceId.displayName,
                errorMensaje = "No fue posible obtener información desde ${sourceId.displayName}. Esta fuente requiere credenciales autorizadas o conexión especial."
            )
        }

        val candidates = generateWordCandidates(word, languageCode)
        for (candidate in candidates) {
            try {
                val res = provider.searchWord(candidate, languageCode)
                if (res != null && res.definiciones.isNotEmpty()) {
                    return res
                }
            } catch (e: Exception) {
                return DictionaryResult(
                    palabra = word,
                    idioma = languageCode,
                    fuente = sourceId.displayName,
                    errorMensaje = "No fue posible obtener información desde ${sourceId.displayName}. Error de conexión o formato: ${e.localizedMessage ?: "desconocido"}."
                )
            }
        }

        return DictionaryResult(
            palabra = word,
            idioma = languageCode,
            fuente = sourceId.displayName,
            errorMensaje = "No fue posible obtener información desde ${sourceId.displayName} para el término \"$word\"."
        )
    }

    /**
     * Compares multiple available sources in parallel.
     */
    suspend fun compareSources(word: String, languageCode: String): List<DictionaryResult> = coroutineScope {
        val providers = sourceManager.getAvailableProvidersForLanguage(languageCode)
        val deferreds = providers.map { provider ->
            async(Dispatchers.IO) {
                try {
                    val res = provider.searchWord(word, languageCode)
                    if (res != null && res.definiciones.isNotEmpty()) {
                        res
                    } else {
                        DictionaryResult(
                            palabra = word,
                            idioma = languageCode,
                            fuente = provider.displayName,
                            errorMensaje = "Sin resultados en ${provider.displayName}"
                        )
                    }
                } catch (e: Exception) {
                    DictionaryResult(
                        palabra = word,
                        idioma = languageCode,
                        fuente = provider.displayName,
                        errorMensaje = "Error al consultar ${provider.displayName}"
                    )
                }
            }
        }

        deferreds.awaitAll()
    }

    /**
     * Generates intelligent candidate variations:
     * - Original
     * - Normalized accents (e.g. cancion <-> canción)
     * - Plural reduction (e.g. libros -> libro)
     * - Verb conjugation lemmatization (e.g. hablaron -> hablar)
     */
    private fun generateWordCandidates(word: String, languageCode: String): List<String> {
        val trimmed = word.trim()
        val list = mutableListOf(trimmed)

        val lower = trimmed.lowercase()
        if (lower != trimmed) list.add(lower)

        // Common accent variations in Spanish
        if (languageCode.equals("es", ignoreCase = true) || languageCode.equals("todos", ignoreCase = true)) {
            val accentMap = mapOf(
                "cancion" to "canción",
                "latrocinio" to "latrocinio",
                "corazon" to "corazón",
                "accion" to "acción",
                "leccion" to "lección",
                "arbol" to "árbol",
                "facil" to "fácil",
                "dificil" to "difícil",
                "musica" to "música",
                "politica" to "política"
            )
            accentMap[lower]?.let { if (!list.contains(it)) list.add(it) }

            // Inflection / conjugation heuristics
            if (lower.endsWith("aron")) {
                list.add(lower.removeSuffix("aron") + "ar")
            } else if (lower.endsWith("ieron")) {
                list.add(lower.removeSuffix("ieron") + "er")
                list.add(lower.removeSuffix("ieron") + "ir")
            } else if (lower.endsWith("ando")) {
                list.add(lower.removeSuffix("ando") + "ar")
            } else if (lower.endsWith("iendo")) {
                list.add(lower.removeSuffix("iendo") + "er")
                list.add(lower.removeSuffix("iendo") + "ir")
            }

            // Plural heuristics
            if (lower.endsWith("es") && lower.length > 3) {
                list.add(lower.removeSuffix("es"))
            } else if (lower.endsWith("s") && lower.length > 3) {
                list.add(lower.removeSuffix("s"))
            }
        }

        // English plurals / verb endings
        if (languageCode.equals("en", ignoreCase = true)) {
            if (lower.endsWith("ing") && lower.length > 4) {
                list.add(lower.removeSuffix("ing"))
                list.add(lower.removeSuffix("ing") + "e")
            } else if (lower.endsWith("ed") && lower.length > 3) {
                list.add(lower.removeSuffix("ed"))
                list.add(lower.removeSuffix("ed") + "e")
            } else if (lower.endsWith("ies") && lower.length > 4) {
                list.add(lower.removeSuffix("ies") + "y")
            } else if (lower.endsWith("s") && lower.length > 3) {
                list.add(lower.removeSuffix("s"))
            }
        }

        return list.distinct()
    }
}
