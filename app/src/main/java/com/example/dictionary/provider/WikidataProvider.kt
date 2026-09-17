package com.example.dictionary.provider

import com.example.dictionary.model.DictionaryResult
import com.example.dictionary.model.DictionarySourceId
import com.example.dictionary.network.DictionaryHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URLEncoder

class WikidataProvider : DictionaryProvider {

    override val sourceId: DictionarySourceId = DictionarySourceId.WIKIDATA

    override fun isAvailable(): Boolean = true

    override fun supportsLanguage(languageCode: String): Boolean = true

    override suspend fun searchWord(word: String, languageCode: String): DictionaryResult? = withContext(Dispatchers.IO) {
        val trimmed = word.trim()
        if (trimmed.isBlank()) return@withContext null

        val lang = languageCode.lowercase()
        val encodedWord = URLEncoder.encode(trimmed, "UTF-8")

        // Step 1: Search lexemes in Wikidata
        val searchUrl = "https://www.wikidata.org/w/api.php?action=wbsearchentities&search=$encodedWord&type=lexeme&language=$lang&format=json"
        val searchJson = DictionaryHttpClient.getJsonString(searchUrl) ?: return@withContext null

        val lexemeId = extractFirstLexemeId(searchJson) ?: return@withContext null

        // Step 2: Fetch detailed lexeme data
        val entityUrl = "https://www.wikidata.org/w/api.php?action=wbgetentities&ids=$lexemeId&format=json"
        val entityJson = DictionaryHttpClient.getJsonString(entityUrl) ?: return@withContext null

        parseLexemeEntity(trimmed, lang, lexemeId, entityJson)
    }

    private fun extractFirstLexemeId(jsonStr: String): String? {
        return try {
            val root = JSONObject(jsonStr)
            val search = root.optJSONArray("search") ?: return null
            if (search.length() == 0) return null
            search.getJSONObject(0).optString("id", null)
        } catch (e: Exception) {
            null
        }
    }

    private fun parseLexemeEntity(word: String, lang: String, lexemeId: String, jsonStr: String): DictionaryResult? {
        return try {
            val root = JSONObject(jsonStr)
            val entities = root.optJSONObject("entities") ?: return null
            val lexemeObj = entities.optJSONObject(lexemeId) ?: return null

            // Lexical Category
            val categoryId = lexemeObj.optString("lexicalCategory", "")
            val categoryLabel = mapLexicalCategory(categoryId)

            // Senses (Glosses / Definitions)
            val definitions = mutableListOf<String>()
            val sensesArray = lexemeObj.optJSONArray("senses")
            if (sensesArray != null) {
                for (i in 0 until sensesArray.length()) {
                    val sense = sensesArray.optJSONObject(i) ?: continue
                    val glosses = sense.optJSONObject("glosses") ?: continue

                    // Check requested language, then Spanish, then English
                    val glossObj = glosses.optJSONObject(lang)
                        ?: glosses.optJSONObject("es")
                        ?: glosses.optJSONObject("en")

                    val value = glossObj?.optString("value", "")
                    if (!value.isNullOrBlank() && !definitions.contains(value)) {
                        definitions.add(value)
                    }
                }
            }

            // Forms (inflections)
            val formsList = mutableListOf<String>()
            val formsArray = lexemeObj.optJSONArray("forms")
            if (formsArray != null) {
                for (i in 0 until formsArray.length()) {
                    val formObj = formsArray.optJSONObject(i) ?: continue
                    val representations = formObj.optJSONObject("representations") ?: continue
                    val repObj = representations.optJSONObject(lang)
                        ?: representations.optJSONObject("es")
                        ?: representations.optJSONObject("en")

                    val formValue = repObj?.optString("value", "")
                    if (!formValue.isNullOrBlank() && !formsList.contains(formValue) && formValue != word) {
                        formsList.add(formValue)
                    }
                }
            }

            if (definitions.isEmpty()) {
                // If senses had no gloss in requested language, check description from lexeme
                val lemmas = lexemeObj.optJSONObject("lemmas")
                val lemmaVal = lemmas?.optJSONObject(lang)?.optString("value", word) ?: word
                definitions.add("Entidad léxica registrada en Wikidata con identificador $lexemeId.")
            }

            DictionaryResult(
                palabra = word,
                idioma = lang,
                categoriaGramatical = if (categoryLabel.isNotBlank()) categoryLabel else "Entidad Léxica ($categoryId)",
                definiciones = definitions,
                formasGramaticales = formsList.take(6),
                fuente = sourceId.displayName,
                urlFuente = "https://www.wikidata.org/wiki/Lexeme:$lexemeId",
                atribucion = "${sourceId.attributionText} (ID: $lexemeId)",
                licencia = sourceId.licenseText
            )
        } catch (e: Exception) {
            null
        }
    }

    private fun mapLexicalCategory(id: String): String {
        return when (id) {
            "Q1084" -> "Sustantivo (Noun)"
            "Q24905" -> "Verbo (Verb)"
            "Q34698" -> "Adjetivo (Adjective)"
            "Q380057" -> "Adverbio (Adverb)"
            "Q4833830" -> "Pronombre (Pronoun)"
            "Q484469" -> "Interjección (Interjection)"
            else -> "Entidad gramatical Wikidata ($id)"
        }
    }
}
