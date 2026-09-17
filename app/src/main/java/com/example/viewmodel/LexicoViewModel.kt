package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.corpus.LocalCorpus
import com.example.data.db.LexicoDatabase
import com.example.data.entity.FavoriteEntity
import com.example.data.entity.SearchHistoryEntity
import com.example.dictionary.model.DictionaryResult
import com.example.dictionary.model.DictionarySourceId
import com.example.dictionary.model.toWordEntry
import com.example.dictionary.repository.DictionaryRepository
import com.example.model.LexicalCategories
import com.example.model.ThemeMode
import com.example.model.WordEntry
import com.example.repository.LexicoRepository
import com.example.util.TtsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LexicoViewModel(application: Application) : AndroidViewModel(application) {

    private val db = LexicoDatabase.getInstance(application)
    private val localRepo = LexicoRepository(db.searchHistoryDao(), db.favoriteDao())
    val ttsManager = TtsManager(application)
    val dictionaryRepository = DictionaryRepository()

    // Multi-source Dictionary State
    private val _selectedSource = MutableStateFlow(DictionarySourceId.AUTOMATIC)
    val selectedSource: StateFlow<DictionarySourceId> = _selectedSource.asStateFlow()

    private val _currentDictionaryResult = MutableStateFlow<DictionaryResult?>(null)
    val currentDictionaryResult: StateFlow<DictionaryResult?> = _currentDictionaryResult.asStateFlow()

    private val _isSearchingNetwork = MutableStateFlow(false)
    val isSearchingNetwork: StateFlow<Boolean> = _isSearchingNetwork.asStateFlow()

    private val _isComparingSources = MutableStateFlow(false)
    val isComparingSources: StateFlow<Boolean> = _isComparingSources.asStateFlow()

    private val _comparedResults = MutableStateFlow<List<DictionaryResult>>(emptyList())
    val comparedResults: StateFlow<List<DictionaryResult>> = _comparedResults.asStateFlow()

    // Settings for Dictionary Sources
    val priorityList: StateFlow<List<DictionarySourceId>> = dictionaryRepository.priorityManager.priorityList
    val defaultSource: StateFlow<DictionarySourceId> = dictionaryRepository.priorityManager.defaultSource
    val allowSourceSwitch: StateFlow<Boolean> = dictionaryRepository.priorityManager.allowSourceSwitch
    val compareSourcesEnabled: StateFlow<Boolean> = dictionaryRepository.priorityManager.compareSourcesEnabled
    val useAlternativeSourcesAuto: StateFlow<Boolean> = dictionaryRepository.priorityManager.useAlternativeSourcesAuto

    private val _cacheDurationHours = MutableStateFlow(24)
    val cacheDurationHours: StateFlow<Int> = _cacheDurationHours.asStateFlow()

    // Search and Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("todos")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _selectedCategory = MutableStateFlow(LexicalCategories.ALL)
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchResults = MutableStateFlow<List<WordEntry>>(emptyList())
    val searchResults: StateFlow<List<WordEntry>> = _searchResults.asStateFlow()

    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions.asStateFlow()

    // Available sources for current selected language
    val availableSourcesForCurrentLang: StateFlow<List<DictionarySourceId>> = combine(
        _selectedLanguage,
        priorityList
    ) { lang, _ ->
        dictionaryRepository.sourceManager.getSelectableSourcesForLanguage(lang)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf(DictionarySourceId.AUTOMATIC))

    val availableSources: StateFlow<List<DictionarySourceId>> = availableSourcesForCurrentLang

    // Word of the Day
    private val _wordOfTheDay = MutableStateFlow(localRepo.getWordOfTheDay())
    val wordOfTheDay: StateFlow<WordEntry> = _wordOfTheDay.asStateFlow()

    // Featured and suggested discoveries for Home
    private val _discoveries = MutableStateFlow(LocalCorpus.WORDS.take(8))
    val discoveries: StateFlow<List<WordEntry>> = _discoveries.asStateFlow()

    // Detail modal/sheet
    private val _selectedWord = MutableStateFlow<WordEntry?>(null)
    val selectedWord: StateFlow<WordEntry?> = _selectedWord.asStateFlow()

    private val _isDetailOpen = MutableStateFlow(false)
    val isDetailOpen: StateFlow<Boolean> = _isDetailOpen.asStateFlow()

    private val _isFavoriteOfSelected = MutableStateFlow(false)
    val isFavoriteOfSelected: StateFlow<Boolean> = _isFavoriteOfSelected.asStateFlow()

    // History and Favorites from Room
    val historyList: StateFlow<List<SearchHistoryEntity>> = localRepo.allHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoritesList: StateFlow<List<FavoriteEntity>> = localRepo.allFavorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _favoritesFilterQuery = MutableStateFlow("")
    val favoritesFilterQuery: StateFlow<String> = _favoritesFilterQuery.asStateFlow()

    private val _favoritesSortAlphabetical = MutableStateFlow(false)
    val favoritesSortAlphabetical: StateFlow<Boolean> = _favoritesSortAlphabetical.asStateFlow()

    val displayedFavorites: StateFlow<List<FavoriteEntity>> = combine(
        favoritesList,
        _favoritesFilterQuery,
        _favoritesSortAlphabetical
    ) { list, query, alphabetical ->
        val filtered = if (query.isBlank()) {
            list
        } else {
            list.filter {
                it.word.contains(query, ignoreCase = true) ||
                        it.definitionPreview.contains(query, ignoreCase = true)
            }
        }
        if (alphabetical) {
            filtered.sortedBy { it.word.lowercase() }
        } else {
            filtered.sortedByDescending { it.timestamp }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Settings
    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _ttsRate = MutableStateFlow(1.0f)
    val ttsRate: StateFlow<Float> = _ttsRate.asStateFlow()

    private val _ttsPitch = MutableStateFlow(1.0f)
    val ttsPitch: StateFlow<Float> = _ttsPitch.asStateFlow()

    val isSpeaking: StateFlow<Boolean> = ttsManager.isSpeaking

    // Flashcard Study Mode
    private val _isFlashcardsActive = MutableStateFlow(false)
    val isFlashcardsActive: StateFlow<Boolean> = _isFlashcardsActive.asStateFlow()

    private val _flashcardWords = MutableStateFlow<List<WordEntry>>(emptyList())
    val flashcardWords: StateFlow<List<WordEntry>> = _flashcardWords.asStateFlow()

    private val _flashcardIndex = MutableStateFlow(0)
    val flashcardIndex: StateFlow<Int> = _flashcardIndex.asStateFlow()

    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped: StateFlow<Boolean> = _isCardFlipped.asStateFlow()

    private var searchDebounceJob: Job? = null
    private var networkQueryJob: Job? = null

    init {
        performLocalSearchNow()
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _suggestions.value = localRepo.getSuggestions(query)

        searchDebounceJob?.cancel()
        searchDebounceJob = viewModelScope.launch {
            delay(250)
            performLocalSearchNow()
            if (query.trim().length >= 2) {
                executeNetworkDictionaryQuery(query.trim(), _selectedSource.value)
            }
        }
    }

    fun submitSearch(query: String) {
        val trimmed = query.trim()
        _searchQuery.value = trimmed
        performLocalSearchNow()
        viewModelScope.launch(Dispatchers.IO) {
            localRepo.recordSearch(trimmed, _selectedLanguage.value)
        }
        executeNetworkDictionaryQuery(trimmed, _selectedSource.value)
    }

    fun onLanguageSelected(code: String) {
        _selectedLanguage.value = code
        // Ensure selected source is compatible, or fallback to AUTOMATIC
        val compatibleSources = dictionaryRepository.sourceManager.getSelectableSourcesForLanguage(code)
        if (!_selectedSource.value.isPubliclyUsable || !compatibleSources.contains(_selectedSource.value)) {
            _selectedSource.value = DictionarySourceId.AUTOMATIC
        }
        performLocalSearchNow()
        if (_searchQuery.value.isNotBlank()) {
            executeNetworkDictionaryQuery(_searchQuery.value, _selectedSource.value)
        }
    }

    fun onCategorySelected(category: String) {
        _selectedCategory.value = category
        performLocalSearchNow()
    }

    private fun performLocalSearchNow() {
        val results = localRepo.searchWords(
            query = _searchQuery.value,
            language = _selectedLanguage.value,
            category = _selectedCategory.value
        )
        _searchResults.value = results
    }

    /**
     * Changes dictionary source in-place and re-queries immediately without clearing search.
     */
    fun onSourceSelected(newSource: DictionarySourceId) {
        _selectedSource.value = newSource
        val wordToQuery = _selectedWord.value?.word ?: _searchQuery.value
        if (wordToQuery.isNotBlank()) {
            executeNetworkDictionaryQuery(wordToQuery, newSource)
        }
    }

    fun retryCurrentQuery() {
        val wordToQuery = _selectedWord.value?.word ?: _searchQuery.value
        if (wordToQuery.isNotBlank()) {
            executeNetworkDictionaryQuery(wordToQuery, _selectedSource.value)
        }
    }

    fun toggleCompareSources() {
        val nextState = !_isComparingSources.value
        _isComparingSources.value = nextState
        if (nextState) {
            val wordToCompare = _selectedWord.value?.word ?: _searchQuery.value
            if (wordToCompare.isNotBlank()) {
                executeCompareSources(wordToCompare)
            }
        }
    }

    private fun executeCompareSources(word: String) {
        viewModelScope.launch {
            _isSearchingNetwork.value = true
            val lang = if (_selectedLanguage.value == "todos") "es" else _selectedLanguage.value
            val comparisons = dictionaryRepository.compareSources(word, lang)
            _comparedResults.value = comparisons
            _isSearchingNetwork.value = false
        }
    }

    private fun executeNetworkDictionaryQuery(word: String, source: DictionarySourceId) {
        networkQueryJob?.cancel()
        networkQueryJob = viewModelScope.launch {
            _isSearchingNetwork.value = true
            val lang = if (_selectedLanguage.value == "todos") "es" else _selectedLanguage.value
            val result = dictionaryRepository.queryWord(word, lang, source)
            _currentDictionaryResult.value = result
            _isSearchingNetwork.value = false

            // If a result is found and detail is open or queried word matches, sync selectedWord
            if (result.isSuccess) {
                val mappedWord = result.toWordEntry()
                if (_selectedWord.value == null || _selectedWord.value?.word.equals(word, ignoreCase = true)) {
                    _selectedWord.value = mappedWord
                    checkFavoriteStatus(mappedWord.id)
                }

                // Append to search results if not already present
                val current = _searchResults.value.toMutableList()
                if (current.none { it.word.equals(mappedWord.word, ignoreCase = true) }) {
                    current.add(0, mappedWord)
                    _searchResults.value = current
                }
            }

            if (_isComparingSources.value) {
                executeCompareSources(word)
            }
        }
    }

    fun openWordDetail(word: WordEntry) {
        _selectedWord.value = word
        _isDetailOpen.value = true
        checkFavoriteStatus(word.id)
        // Query providers to complement or show current source
        executeNetworkDictionaryQuery(word.word, _selectedSource.value)
    }

    fun openWordByName(name: String) {
        val local = localRepo.getWordById(name)
        if (local != null) {
            openWordDetail(local)
        } else {
            val effectiveLang = if (_selectedLanguage.value == "todos") "es" else _selectedLanguage.value
            _selectedWord.value = WordEntry(
                id = name.lowercase().trim(),
                word = name,
                phonetic = "",
                partOfSpeech = "Consultando...",
                language = effectiveLang,
                languageName = com.example.model.SupportedLanguages.getByCode(effectiveLang).name,
                etymology = "",
                definitions = listOf("Obteniendo información desde las fuentes lexicográficas..."),
                examples = emptyList(),
                category = "General"
            )
            _isDetailOpen.value = true
            executeNetworkDictionaryQuery(name, _selectedSource.value)
        }
    }

    fun closeWordDetail() {
        _isDetailOpen.value = false
        _isComparingSources.value = false
        ttsManager.stop()
    }

    private fun checkFavoriteStatus(wordId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFav = db.favoriteDao().isFavoriteSync(wordId)
            _isFavoriteOfSelected.value = isFav
        }
    }

    fun toggleFavorite(word: WordEntry) {
        viewModelScope.launch {
            localRepo.toggleFavorite(word)
            checkFavoriteStatus(word.id)
        }
    }

    fun removeFavorite(wordId: String) {
        viewModelScope.launch {
            localRepo.removeFavorite(wordId)
            if (_selectedWord.value?.id == wordId) {
                _isFavoriteOfSelected.value = false
            }
        }
    }

    fun speakWord(word: WordEntry) {
        ttsManager.speak(word.word, word.language)
    }

    fun speakText(text: String, language: String) {
        ttsManager.speak(text, language)
    }

    fun stopSpeaking() {
        ttsManager.stop()
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            localRepo.deleteHistoryItem(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            localRepo.clearHistory()
        }
    }

    fun clearFavorites() {
        viewModelScope.launch {
            localRepo.clearFavorites()
            _isFavoriteOfSelected.value = false
        }
    }

    fun onFavoritesQueryChanged(query: String) {
        _favoritesFilterQuery.value = query
    }

    fun toggleFavoritesSort() {
        _favoritesSortAlphabetical.value = !_favoritesSortAlphabetical.value
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    fun setTtsRate(rate: Float) {
        _ttsRate.value = rate
        ttsManager.setSpeechRate(rate)
    }

    fun setTtsPitch(pitch: Float) {
        _ttsPitch.value = pitch
        ttsManager.setPitch(pitch)
    }

    fun refreshWordOfTheDay() {
        _wordOfTheDay.value = localRepo.getRandomWords(1).firstOrNull() ?: localRepo.getWordOfTheDay()
    }

    // Source Priority & Settings
    fun setDefaultSource(sourceId: DictionarySourceId) {
        dictionaryRepository.priorityManager.setDefaultSource(sourceId)
        _selectedSource.value = sourceId
    }

    fun setAllowSourceSwitch(enabled: Boolean) {
        dictionaryRepository.priorityManager.setAllowSourceSwitch(enabled)
    }

    fun setCompareSourcesEnabled(enabled: Boolean) {
        dictionaryRepository.priorityManager.setCompareSourcesEnabled(enabled)
    }

    fun setUseAlternativeSourcesAuto(enabled: Boolean) {
        dictionaryRepository.priorityManager.setUseAlternativeSourcesAuto(enabled)
    }

    fun movePriorityUp(index: Int) {
        dictionaryRepository.priorityManager.movePriorityUp(index)
    }

    fun movePriorityDown(index: Int) {
        dictionaryRepository.priorityManager.movePriorityDown(index)
    }

    fun setCacheDurationHours(hours: Int) {
        _cacheDurationHours.value = hours
        dictionaryRepository.cacheManager.cacheDurationMillis = TimeUnit.HOURS.toMillis(hours.toLong())
    }

    fun clearNetworkCache() {
        dictionaryRepository.cacheManager.clear()
    }

    // Flashcards
    fun openFlashcards() {
        val currentFavs = favoritesList.value
        val words = if (currentFavs.isNotEmpty()) {
            currentFavs.mapNotNull { localRepo.getWordById(it.wordId) }
        } else {
            LocalCorpus.WORDS.shuffled().take(6)
        }
        if (words.isNotEmpty()) {
            _flashcardWords.value = words.shuffled()
            _flashcardIndex.value = 0
            _isCardFlipped.value = false
            _isFlashcardsActive.value = true
        }
    }

    fun closeFlashcards() {
        _isFlashcardsActive.value = false
        _isCardFlipped.value = false
    }

    fun flipFlashcard() {
        _isCardFlipped.value = !_isCardFlipped.value
    }

    fun nextFlashcard() {
        val size = _flashcardWords.value.size
        if (size > 0) {
            _flashcardIndex.value = (_flashcardIndex.value + 1) % size
            _isCardFlipped.value = false
        }
    }

    fun previousFlashcard() {
        val size = _flashcardWords.value.size
        if (size > 0) {
            val prev = _flashcardIndex.value - 1
            _flashcardIndex.value = if (prev < 0) size - 1 else prev
            _isCardFlipped.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
