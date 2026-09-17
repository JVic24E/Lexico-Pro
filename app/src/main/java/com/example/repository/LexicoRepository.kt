package com.example.repository

import com.example.data.corpus.LocalCorpus
import com.example.data.dao.FavoriteDao
import com.example.data.dao.SearchHistoryDao
import com.example.data.entity.FavoriteEntity
import com.example.data.entity.SearchHistoryEntity
import com.example.model.LexicalCategories
import com.example.model.WordEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Calendar

class LexicoRepository(
    private val historyDao: SearchHistoryDao,
    private val favoriteDao: FavoriteDao
) {

    val allFavorites: Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()
    val allFavoritesAlphabetical: Flow<List<FavoriteEntity>> = favoriteDao.getAllFavoritesAlphabetical()
    val allHistory: Flow<List<SearchHistoryEntity>> = historyDao.getAllHistory()

    fun isFavorite(wordId: String): Flow<Boolean> {
        return favoriteDao.isFavoriteFlow(wordId)
    }

    suspend fun toggleFavorite(word: WordEntry) = withContext(Dispatchers.IO) {
        val isFav = favoriteDao.isFavoriteSync(word.id)
        if (isFav) {
            favoriteDao.deleteFavoriteById(word.id)
        } else {
            val entity = FavoriteEntity(
                wordId = word.id,
                word = word.word,
                phonetic = word.phonetic,
                partOfSpeech = word.partOfSpeech,
                definitionPreview = word.definitions.firstOrNull() ?: "",
                language = word.language,
                category = word.category,
                timestamp = System.currentTimeMillis()
            )
            favoriteDao.insertFavorite(entity)
        }
    }

    suspend fun removeFavorite(wordId: String) = withContext(Dispatchers.IO) {
        favoriteDao.deleteFavoriteById(wordId)
    }

    suspend fun recordSearch(query: String, language: String = "es") = withContext(Dispatchers.IO) {
        val trimmed = query.trim()
        if (trimmed.isNotBlank()) {
            historyDao.deleteByQuery(trimmed)
            historyDao.insertHistory(
                SearchHistoryEntity(
                    query = trimmed,
                    language = language,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun deleteHistoryItem(id: Long) = withContext(Dispatchers.IO) {
        historyDao.deleteById(id)
    }

    suspend fun clearHistory() = withContext(Dispatchers.IO) {
        historyDao.clearAll()
    }

    suspend fun clearFavorites() = withContext(Dispatchers.IO) {
        favoriteDao.clearAll()
    }

    fun searchWords(
        query: String,
        language: String = "todos",
        category: String = LexicalCategories.ALL
    ): List<WordEntry> {
        val q = query.trim().lowercase()
        return LocalCorpus.WORDS.filter { word ->
            val matchesLanguage = language.equals("todos", ignoreCase = true) ||
                    word.language.equals(language, ignoreCase = true)

            val matchesCategory = category == LexicalCategories.ALL ||
                    word.category.equals(category, ignoreCase = true)

            val matchesQuery = if (q.isBlank()) {
                true
            } else {
                word.word.lowercase().contains(q) ||
                        word.definitions.any { it.lowercase().contains(q) } ||
                        word.synonyms.any { it.lowercase().contains(q) } ||
                        word.etymology.lowercase().contains(q) ||
                        word.translations.values.any { it.lowercase().contains(q) }
            }

            matchesLanguage && matchesCategory && matchesQuery
        }
    }

    fun getWordById(id: String): WordEntry? {
        return LocalCorpus.WORDS.firstOrNull { it.id.equals(id, ignoreCase = true) }
            ?: LocalCorpus.WORDS.firstOrNull { it.word.equals(id, ignoreCase = true) }
    }

    fun getWordOfTheDay(): WordEntry {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val index = (dayOfYear % LocalCorpus.WORDS.size).coerceIn(0, LocalCorpus.WORDS.size - 1)
        return LocalCorpus.WORDS[index]
    }

    fun getWordsByCategory(category: String): List<WordEntry> {
        return if (category == LexicalCategories.ALL) {
            LocalCorpus.WORDS
        } else {
            LocalCorpus.WORDS.filter { it.category.equals(category, ignoreCase = true) }
        }
    }

    fun getSuggestions(prefix: String): List<String> {
        val p = prefix.trim().lowercase()
        if (p.isBlank()) return emptyList()
        return LocalCorpus.WORDS
            .filter { it.word.lowercase().contains(p) }
            .map { it.word }
            .distinct()
            .take(6)
    }

    fun getRandomWords(count: Int = 5): List<WordEntry> {
        return LocalCorpus.WORDS.shuffled().take(count)
    }
}
