package com.example.dictionary.provider

import com.example.dictionary.model.DictionaryResult
import com.example.dictionary.model.DictionarySourceId
import com.example.dictionary.network.DictionaryHttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URLEncoder

class RAEProvider : DictionaryProvider {

    override val sourceId: DictionarySourceId = DictionarySourceId.RAE_DLE

    // RAE Enclave authorized institutional token/endpoint if provided by institution
    private val enclaveToken: String = System.getenv("RAE_ENCLAVE_TOKEN") ?: ""
    private val enclaveProxyUrl: String = System.getenv("RAE_PROXY_URL") ?: ""

    override fun isAvailable(): Boolean = enclaveToken.isNotBlank() || enclaveProxyUrl.isNotBlank()

    override fun supportsLanguage(languageCode: String): Boolean {
        return languageCode.lowercase() == "es"
    }

    override suspend fun searchWord(word: String, languageCode: String): DictionaryResult? = withContext(Dispatchers.IO) {
        if (!isAvailable()) return@withContext null

        val encoded = URLEncoder.encode(word.trim(), "UTF-8")
        val targetUrl = if (enclaveProxyUrl.isNotBlank()) {
            "$enclaveProxyUrl/dle/$encoded"
        } else {
            "https://enclave.rae.es/api/v1/search?term=$encoded&token=$enclaveToken"
        }

        val json = DictionaryHttpClient.getJsonString(targetUrl) ?: return@withContext null

        try {
            val root = JSONObject(json)
            val definitions = mutableListOf<String>()
            val defsArray = root.optJSONArray("definitions")
            if (defsArray != null) {
                for (i in 0 until defsArray.length()) {
                    val d = defsArray.optString(i, "")
                    if (d.isNotBlank()) definitions.add(d)
                }
            }

            if (definitions.isEmpty()) return@withContext null

            DictionaryResult(
                palabra = word,
                idioma = "es",
                definiciones = definitions,
                categoriaGramatical = root.optString("partOfSpeech", "No disponible"),
                etimologia = root.optString("etymology", "No disponible"),
                fuente = sourceId.displayName,
                urlFuente = "https://dle.rae.es/$encoded",
                atribucion = sourceId.attributionText,
                licencia = sourceId.licenseText
            )
        } catch (e: Exception) {
            null
        }
    }
}
