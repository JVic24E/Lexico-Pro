package com.example.dictionary.provider

import com.example.data.corpus.LocalCorpus
import com.example.dictionary.model.DictionaryResult
import com.example.dictionary.model.DictionarySourceId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalCorpusProvider : DictionaryProvider {

    override val sourceId: DictionarySourceId = DictionarySourceId.LOCAL_CORPUS

    override fun isAvailable(): Boolean = true

    override fun supportsLanguage(languageCode: String): Boolean = true

    override suspend fun searchWord(word: String, languageCode: String): DictionaryResult? = withContext(Dispatchers.Default) {
        val trimmed = word.trim()
        if (trimmed.isBlank()) return@withContext null

        val entry = LocalCorpus.WORDS.firstOrNull {
            it.word.equals(trimmed, ignoreCase = true) || it.id.equals(trimmed, ignoreCase = true)
        } ?: LocalCorpus.WORDS.firstOrNull {
            it.word.contains(trimmed, ignoreCase = true)
        } ?: return@withContext null

        DictionaryResult(
            palabra = entry.word,
            idioma = entry.language,
            pronunciacion = entry.phonetic,
            ipa = entry.phonetic,
            categoriaGramatical = entry.partOfSpeech,
            definiciones = entry.definitions,
            traducciones = entry.translations,
            ejemplos = entry.examples,
            sinonimos = entry.synonyms,
            antonimos = entry.antonyms,
            etimologia = entry.etymology,
            expresiones = if (entry.inspirationQuote.isNotBlank()) listOf(entry.inspirationQuote) else emptyList(),
            dificultad = entry.category,
            fuente = sourceId.displayName,
            urlFuente = "",
            atribucion = sourceId.attributionText,
            licencia = sourceId.licenseText
        )
    }
}
