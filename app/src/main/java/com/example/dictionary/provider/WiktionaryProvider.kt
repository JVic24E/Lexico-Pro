package com.example.dictionary.provider

import com.example.dictionary.model.DictionaryResult
import com.example.dictionary.model.DictionarySourceId
import com.example.dictionary.network.DictionaryHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.util.regex.Pattern

class WiktionaryProvider : DictionaryProvider {

    override val sourceId: DictionarySourceId = DictionarySourceId.WIKTIONARY

    override fun isAvailable(): Boolean = true

    override fun supportsLanguage(languageCode: String): Boolean {
        // Wiktionary has editions for all requested languages
        return true
    }

    override suspend fun searchWord(word: String, languageCode: String): DictionaryResult? = withContext(Dispatchers.IO) {
        val trimmed = word.trim()
        if (trimmed.isBlank()) return@withContext null

        val lang = mapLanguageCode(languageCode)
        val encodedWord = URLEncoder.encode(trimmed, "UTF-8")

        // 1. Try Wikimedia REST definitions endpoint
        val restUrl = "https://$lang.wiktionary.org/api/rest_v1/page/definition/$encodedWord"
        val restJson = DictionaryHttpClient.getJsonString(restUrl)

        if (!restJson.isNullOrBlank()) {
            val parsed = parseRestDefinition(trimmed, lang, restJson)
            if (parsed != null && parsed.definiciones.isNotEmpty()) {
                return@withContext parsed
            }
        }

        // 2. Fallback to MediaWiki Query Extract API
        val mediaWikiUrl = "https://$lang.wiktionary.org/w/api.php?action=query&prop=extracts&explaintext=1&titles=$encodedWord&format=json"
        val mwJson = DictionaryHttpClient.getJsonString(mediaWikiUrl)
        if (!mwJson.isNullOrBlank()) {
            val parsedMw = parseMediaWikiExtract(trimmed, lang, mwJson)
            if (parsedMw != null && parsedMw.definiciones.isNotEmpty()) {
                return@withContext parsedMw
            }
        }

        null
    }

    private fun mapLanguageCode(code: String): String {
        return when (code.lowercase()) {
            "en" -> "en"
            "fr" -> "fr"
            "la" -> "la"
            "qu" -> "qu"
            "de" -> "de"
            "it" -> "it"
            "pt" -> "pt"
            else -> "es"
        }
    }

    private fun parseRestDefinition(word: String, lang: String, jsonStr: String): DictionaryResult? {
        return try {
            val root = JSONObject(jsonStr)
            val definitionsList = mutableListOf<String>()
            val examplesList = mutableListOf<String>()
            var partOfSpeech = ""

            val keys = root.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val entriesArray = root.optJSONArray(key) ?: continue
                for (i in 0 until entriesArray.length()) {
                    val entryObj = entriesArray.optJSONObject(i) ?: continue
                    if (partOfSpeech.isBlank()) {
                        partOfSpeech = entryObj.optString("partOfSpeech", "")
                    }
                    val defsArray = entryObj.optJSONArray("definitions") ?: continue
                    for (j in 0 until defsArray.length()) {
                        val defItem = defsArray.optJSONObject(j) ?: continue
                        val rawDef = defItem.optString("definition", "")
                        val cleanDef = cleanHtml(rawDef)
                        if (cleanDef.isNotBlank() && !definitionsList.contains(cleanDef)) {
                            definitionsList.add(cleanDef)
                        }
                        val exArray = defItem.optJSONArray("examples")
                        if (exArray != null) {
                            for (k in 0 until exArray.length()) {
                                val exStr = cleanHtml(exArray.optString(k, ""))
                                if (exStr.isNotBlank() && !examplesList.contains(exStr)) {
                                    examplesList.add(exStr)
                                }
                            }
                        }
                    }
                }
            }

            if (definitionsList.isEmpty()) return null

            DictionaryResult(
                palabra = word,
                idioma = lang,
                categoriaGramatical = if (partOfSpeech.isNotBlank()) partOfSpeech else "No disponible",
                definiciones = definitionsList,
                ejemplos = examplesList,
                fuente = sourceId.displayName,
                urlFuente = "https://$lang.wiktionary.org/wiki/${URLEncoder.encode(word, "UTF-8")}",
                atribucion = sourceId.attributionText,
                licencia = sourceId.licenseText
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun parseMediaWikiExtract(word: String, lang: String, jsonStr: String): DictionaryResult? {
        return try {
            val root = JSONObject(jsonStr)
            val query = root.optJSONObject("query") ?: return null
            val pages = query.optJSONObject("pages") ?: return null
            val pageIdKey = pages.keys().next()
            if (pageIdKey == "-1") return null // Page not found

            val pageObj = pages.getJSONObject(pageIdKey)
            val extract = pageObj.optString("extract", "")
            if (extract.isBlank()) return null

            // Parse lines of the text extract
            val lines = extract.split("\n").map { it.trim() }.filter { it.isNotBlank() }
            val definitions = mutableListOf<String>()
            var currentEtymology = ""

            for (line in lines) {
                if (line.startsWith("==")) continue
                if (line.contains("Etimología", ignoreCase = true) || line.contains("Etymology", ignoreCase = true)) {
                    currentEtymology = line
                    continue
                }
                // Check if line looks like a numbered or bulleted definition
                val isDef = line.matches(Regex("^([0-9]+[.\\)]|[-*•])\\s*.*")) ||
                        (definitions.size < 4 && line.length in 15..300 && !line.startsWith("=") && !line.startsWith("Véase"))

                if (isDef) {
                    val cleanLine = line.replace(Regex("^([0-9]+[.\\)]|[-*•])\\s*"), "").trim()
                    if (cleanLine.length > 5 && !definitions.contains(cleanLine)) {
                        definitions.add(cleanLine)
                    }
                }
            }

            if (definitions.isEmpty()) {
                val candidate = lines.firstOrNull { it.length > 20 && !it.startsWith("=") }
                if (candidate != null) definitions.add(candidate)
            }

            if (definitions.isEmpty()) return null

            DictionaryResult(
                palabra = word,
                idioma = lang,
                definiciones = definitions.take(8),
                etimologia = if (currentEtymology.isNotBlank()) currentEtymology else "No disponible",
                fuente = sourceId.displayName,
                urlFuente = "https://$lang.wiktionary.org/wiki/${URLEncoder.encode(word, "UTF-8")}",
                atribucion = sourceId.attributionText,
                licencia = sourceId.licenseText
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun cleanHtml(html: String): String {
        return html
            .replace(Regex("<[^>]*>"), "")
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .trim()
    }
}
