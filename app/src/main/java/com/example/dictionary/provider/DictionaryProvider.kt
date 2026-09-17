package com.example.dictionary.provider

import com.example.dictionary.model.DictionaryResult
import com.example.dictionary.model.DictionarySourceId

interface DictionaryProvider {
    val sourceId: DictionarySourceId
    val displayName: String
        get() = sourceId.displayName

    fun isAvailable(): Boolean
    fun supportsLanguage(languageCode: String): Boolean

    suspend fun searchWord(word: String, languageCode: String): DictionaryResult?
}
