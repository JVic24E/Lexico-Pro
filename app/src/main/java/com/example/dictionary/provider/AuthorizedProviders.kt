package com.example.dictionary.provider

import com.example.dictionary.model.DictionaryResult
import com.example.dictionary.model.DictionarySourceId
import com.example.dictionary.network.DictionaryHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

class CambridgeProvider : DictionaryProvider {
    override val sourceId: DictionarySourceId = DictionarySourceId.CAMBRIDGE
    private val apiKey = System.getenv("CAMBRIDGE_API_KEY") ?: ""

    override fun isAvailable(): Boolean = apiKey.isNotBlank()
    override fun supportsLanguage(languageCode: String): Boolean = languageCode.lowercase() == "en"

    override suspend fun searchWord(word: String, languageCode: String): DictionaryResult? = withContext(Dispatchers.IO) {
        if (!isAvailable()) return@withContext null
        // Authorized Cambridge API endpoint
        null
    }
}

class MerriamWebsterProvider : DictionaryProvider {
    override val sourceId: DictionarySourceId = DictionarySourceId.MERRIAM_WEBSTER
    private val apiKey = System.getenv("MERRIAM_WEBSTER_API_KEY") ?: ""

    override fun isAvailable(): Boolean = apiKey.isNotBlank()
    override fun supportsLanguage(languageCode: String): Boolean {
        val l = languageCode.lowercase()
        return l == "en" || l == "es"
    }

    override suspend fun searchWord(word: String, languageCode: String): DictionaryResult? = withContext(Dispatchers.IO) {
        if (!isAvailable()) return@withContext null

        val encoded = URLEncoder.encode(word.trim(), "UTF-8")
        val endpoint = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/$encoded?key=$apiKey"
        val response = DictionaryHttpClient.getJsonString(endpoint) ?: return@withContext null

        try {
            val arr = JSONArray(response)
            if (arr.length() == 0) return@withContext null
            val first = arr.getJSONObject(0)
            val shortDef = first.optJSONArray("shortdef") ?: return@withContext null
            val defs = mutableListOf<String>()
            for (i in 0 until shortDef.length()) {
                val d = shortDef.optString(i, "")
                if (d.isNotBlank()) defs.add(d)
            }
            if (defs.isEmpty()) return@withContext null

            DictionaryResult(
                palabra = word,
                idioma = languageCode,
                definiciones = defs,
                categoriaGramatical = first.optString("fl", "No disponible"),
                fuente = sourceId.displayName,
                urlFuente = "https://www.merriam-webster.com/dictionary/$encoded",
                atribucion = sourceId.attributionText,
                licencia = sourceId.licenseText
            )
        } catch (e: Exception) {
            null
        }
    }
}

class CollinsProvider : DictionaryProvider {
    override val sourceId: DictionarySourceId = DictionarySourceId.COLLINS
    private val apiKey = System.getenv("COLLINS_API_KEY") ?: ""

    override fun isAvailable(): Boolean = apiKey.isNotBlank()
    override fun supportsLanguage(languageCode: String): Boolean = languageCode.lowercase() in setOf("en", "es", "fr")

    override suspend fun searchWord(word: String, languageCode: String): DictionaryResult? = withContext(Dispatchers.IO) {
        if (!isAvailable()) return@withContext null
        null
    }
}

class OxfordProvider : DictionaryProvider {
    override val sourceId: DictionarySourceId = DictionarySourceId.OXFORD
    private val appId = System.getenv("OXFORD_APP_ID") ?: ""
    private val appKey = System.getenv("OXFORD_APP_KEY") ?: ""

    override fun isAvailable(): Boolean = appId.isNotBlank() && appKey.isNotBlank()
    override fun supportsLanguage(languageCode: String): Boolean = languageCode.lowercase() in setOf("en", "es", "fr")

    override suspend fun searchWord(word: String, languageCode: String): DictionaryResult? = withContext(Dispatchers.IO) {
        if (!isAvailable()) return@withContext null
        null
    }
}
