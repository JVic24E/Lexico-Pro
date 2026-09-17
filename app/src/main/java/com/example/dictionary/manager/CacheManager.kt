package com.example.dictionary.manager

import com.example.dictionary.model.DictionaryResult
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class CacheManager {

    data class CacheKey(
        val word: String,
        val language: String,
        val sourceCode: String
    )

    data class CacheEntry(
        val result: DictionaryResult,
        val timestamp: Long
    )

    private val cache = ConcurrentHashMap<CacheKey, CacheEntry>()

    // Configurable cache duration (default 24 hours)
    var cacheDurationMillis: Long = TimeUnit.HOURS.toMillis(24)

    fun get(word: String, language: String, sourceCode: String): DictionaryResult? {
        val key = CacheKey(word.trim().lowercase(), language.trim().lowercase(), sourceCode.trim().lowercase())
        val entry = cache[key] ?: return null

        val now = System.currentTimeMillis()
        if (now - entry.timestamp > cacheDurationMillis) {
            cache.remove(key)
            return null
        }

        return entry.result.copy(isFromCache = true)
    }

    fun put(word: String, language: String, sourceCode: String, result: DictionaryResult) {
        val key = CacheKey(word.trim().lowercase(), language.trim().lowercase(), sourceCode.trim().lowercase())
        cache[key] = CacheEntry(
            result = result,
            timestamp = System.currentTimeMillis()
        )
    }

    fun clear() {
        cache.clear()
    }

    val size: Int
        get() = cache.size
}
