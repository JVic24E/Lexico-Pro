package com.example.dictionary.provider

import com.example.dictionary.model.DictionaryResult
import com.example.dictionary.model.DictionarySourceId
import com.example.dictionary.network.DictionaryHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URLEncoder

class WordnikProvider : DictionaryProvider {

    override val sourceId: DictionarySourceId = DictionarySourceId.WORDNIK

    // Check if API key is provided securely via environment
    private val apiKey: String = System.getenv("WORDNIK_API_KEY") ?: ""

    override fun isAvailable(): Boolean = apiKey.isNotBlank()

    override fun supportsLanguage(languageCode: String): Boolean {
        return languageCode.lowercase() == "en"
    }

    override suspend fun searchWord(word: String, languageCode: String): DictionaryResult? = withContext(Dispatchers.IO) {
        if (!isAvailable()) return@withContext null

        val encoded = URLEncoder.encode(word.trim(), "UTF-8")
        val url = "https://api.wordnik.com/v4/word.json/$encoded/definitions?limit=5&includeRelated=true&useCanonical=false&includeTags=false&api_key=$apiKey"
        val response = DictionaryHttpClient.getJsonString(url) ?: return@withContext null

        try {
            val jsonArray = JSONArray(response)
            if (jsonArray.length() == 0) return@withContext null

            val definitions = mutableListOf<String>()
            var partOfSpeech = ""

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val text = obj.optString("text", "")
                if (text.isNotBlank()) definitions.add(text)
                if (partOfSpeech.isBlank()) {
                    partOfSpeech = obj.optString("partOfSpeech", "")
                }
            }

            if (definitions.isEmpty()) return@withContext null

            DictionaryResult(
                palabra = word,
                idioma = "en",
                categoriaGramatical = if (partOfSpeech.isNotBlank()) partOfSpeech else "No disponible",
                definiciones = definitions,
                fuente = sourceId.displayName,
                urlFuente = "https://www.wordnik.com/words/$encoded",
                atribucion = sourceId.attributionText,
                licencia = sourceId.licenseText
            )
        } catch (e: Exception) {
            null
        }
    }
}
