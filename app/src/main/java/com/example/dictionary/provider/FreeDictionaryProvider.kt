package com.example.dictionary.provider

import com.example.dictionary.model.DictionaryResult
import com.example.dictionary.model.DictionarySourceId
import com.example.dictionary.network.DictionaryHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class FreeDictionaryProvider : DictionaryProvider {

    override val sourceId: DictionarySourceId = DictionarySourceId.FREE_DICTIONARY

    override fun isAvailable(): Boolean = true

    override fun supportsLanguage(languageCode: String): Boolean {
        val code = languageCode.lowercase()
        return code == "en" || code == "es" || code == "fr"
    }

    override suspend fun searchWord(word: String, languageCode: String): DictionaryResult? = withContext(Dispatchers.IO) {
        val trimmed = word.trim()
        if (trimmed.isBlank()) return@withContext null

        val lang = when (languageCode.lowercase()) {
            "en" -> "en"
            "es" -> "es"
            "fr" -> "fr"
            else -> "en"
        }

        val encoded = URLEncoder.encode(trimmed, "UTF-8")
        val url = "https://api.dictionaryapi.dev/api/v2/entries/$lang/$encoded"
        val response = DictionaryHttpClient.getJsonString(url) ?: return@withContext null

        parseResponse(trimmed, lang, response)
    }

    private fun parseResponse(word: String, lang: String, jsonStr: String): DictionaryResult? {
        return try {
            val jsonArray = JSONArray(jsonStr)
            if (jsonArray.length() == 0) return null

            val firstEntry = jsonArray.getJSONObject(0)
            val returnedWord = firstEntry.optString("word", word)

            // Phonetics
            var phoneticText = firstEntry.optString("phonetic", "")
            var audioUrl = ""
            val phoneticsArray = firstEntry.optJSONArray("phonetics")
            if (phoneticsArray != null) {
                for (i in 0 until phoneticsArray.length()) {
                    val pObj = phoneticsArray.optJSONObject(i) ?: continue
                    if (phoneticText.isBlank()) {
                        phoneticText = pObj.optString("text", "")
                    }
                    val audio = pObj.optString("audio", "")
                    if (audio.isNotBlank() && audioUrl.isBlank()) {
                        audioUrl = audio
                    }
                }
            }

            val definitionsList = mutableListOf<String>()
            val examplesList = mutableListOf<String>()
            val synonymsList = mutableListOf<String>()
            val antonymsList = mutableListOf<String>()
            var primaryPartOfSpeech = ""

            val meaningsArray = firstEntry.optJSONArray("meanings")
            if (meaningsArray != null) {
                for (i in 0 until meaningsArray.length()) {
                    val meaning = meaningsArray.optJSONObject(i) ?: continue
                    val pos = meaning.optString("partOfSpeech", "")
                    if (primaryPartOfSpeech.isBlank() && pos.isNotBlank()) {
                        primaryPartOfSpeech = pos
                    }

                    // Meanings-level synonyms/antonyms
                    val mSyn = meaning.optJSONArray("synonyms")
                    if (mSyn != null) {
                        for (s in 0 until mSyn.length()) {
                            val syn = mSyn.optString(s, "")
                            if (syn.isNotBlank() && !synonymsList.contains(syn)) synonymsList.add(syn)
                        }
                    }

                    val defs = meaning.optJSONArray("definitions") ?: continue
                    for (j in 0 until defs.length()) {
                        val defObj = defs.optJSONObject(j) ?: continue
                        val defText = defObj.optString("definition", "")
                        if (defText.isNotBlank()) {
                            val prefix = if (pos.isNotBlank()) "[$pos] " else ""
                            definitionsList.add("$prefix$defText")
                        }

                        val example = defObj.optString("example", "")
                        if (example.isNotBlank() && !examplesList.contains(example)) {
                            examplesList.add(example)
                        }

                        val dSyn = defObj.optJSONArray("synonyms")
                        if (dSyn != null) {
                            for (s in 0 until dSyn.length()) {
                                val syn = dSyn.optString(s, "")
                                if (syn.isNotBlank() && !synonymsList.contains(syn)) synonymsList.add(syn)
                            }
                        }

                        val dAnt = defObj.optJSONArray("antonyms")
                        if (dAnt != null) {
                            for (a in 0 until dAnt.length()) {
                                val ant = dAnt.optString(a, "")
                                if (ant.isNotBlank() && !antonymsList.contains(ant)) antonymsList.add(ant)
                            }
                        }
                    }
                }
            }

            if (definitionsList.isEmpty()) return null

            val sourceUrls = firstEntry.optJSONArray("sourceUrls")
            val sourceUrl = if (sourceUrls != null && sourceUrls.length() > 0) {
                sourceUrls.optString(0, "https://api.dictionaryapi.dev")
            } else {
                "https://api.dictionaryapi.dev"
            }

            DictionaryResult(
                palabra = returnedWord,
                idioma = lang,
                pronunciacion = phoneticText,
                ipa = phoneticText,
                audioUrl = audioUrl,
                categoriaGramatical = if (primaryPartOfSpeech.isNotBlank()) primaryPartOfSpeech else "No disponible",
                definiciones = definitionsList.take(8),
                ejemplos = examplesList.take(4),
                sinonimos = synonymsList.take(6),
                antonimos = antonymsList.take(4),
                fuente = sourceId.displayName,
                urlFuente = sourceUrl,
                atribucion = sourceId.attributionText,
                licencia = sourceId.licenseText
            )
        } catch (e: Exception) {
            null
        }
    }
}
