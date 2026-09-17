package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import com.example.data.corpus.LocalCorpus
import com.example.ui.components.FlashcardDialog
import com.example.ui.components.LexicoTopBar
import com.example.ui.components.WordDetailSheet
import com.example.viewmodel.LexicoViewModel

sealed class ScreenTab(
    val index: Int,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    object Home : ScreenTab(0, "Inicio", Icons.Filled.Explore, Icons.Outlined.Explore, "tab_home")
    object Search : ScreenTab(1, "Diccionario", Icons.Filled.MenuBook, Icons.Outlined.MenuBook, "tab_search")
    object Favorites : ScreenTab(2, "Favoritos", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, "tab_favorites")
    object Settings : ScreenTab(3, "Ajustes", Icons.Filled.Settings, Icons.Outlined.Settings, "tab_settings")

    companion object {
        val ALL = listOf(Home, Search, Favorites, Settings)
    }
}

@Composable
fun MainScreen(
    viewModel: LexicoViewModel,
    modifier: Modifier = Modifier
) {
    var showSplash by remember { mutableStateOf(true) }
    var currentTabIndex by remember { mutableIntStateOf(0) }
    var showHistoryView by remember { mutableStateOf(false) }

    val haptic = LocalHapticFeedback.current

    // State Collection from ViewModel
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val wordOfTheDay by viewModel.wordOfTheDay.collectAsState()
    val discoveries by viewModel.discoveries.collectAsState()

    val selectedWord by viewModel.selectedWord.collectAsState()
    val isDetailOpen by viewModel.isDetailOpen.collectAsState()
    val isFavoriteOfSelected by viewModel.isFavoriteOfSelected.collectAsState()
    val isSpeaking by viewModel.isSpeaking.collectAsState()

    val displayedFavorites by viewModel.displayedFavorites.collectAsState()
    val favoritesList by viewModel.favoritesList.collectAsState()
    val favoritesFilterQuery by viewModel.favoritesFilterQuery.collectAsState()
    val favoritesSortAlphabetical by viewModel.favoritesSortAlphabetical.collectAsState()

    val historyList by viewModel.historyList.collectAsState()

    val themeMode by viewModel.themeMode.collectAsState()
    val ttsRate by viewModel.ttsRate.collectAsState()
    val ttsPitch by viewModel.ttsPitch.collectAsState()

    val isFlashcardsActive by viewModel.isFlashcardsActive.collectAsState()
    val flashcardWords by viewModel.flashcardWords.collectAsState()
    val flashcardIndex by viewModel.flashcardIndex.collectAsState()
    val isCardFlipped by viewModel.isCardFlipped.collectAsState()

    // Multi-source Dictionary State
    val selectedSource by viewModel.selectedSource.collectAsState()
    val availableSources by viewModel.availableSources.collectAsState()
    val currentDictionaryResult by viewModel.currentDictionaryResult.collectAsState()
    val isSearchingNetwork by viewModel.isSearchingNetwork.collectAsState()
    val isComparingSources by viewModel.isComparingSources.collectAsState()
    val comparedResults by viewModel.comparedResults.collectAsState()

    val defaultSource by viewModel.defaultSource.collectAsState()
    val allowSourceSwitch by viewModel.allowSourceSwitch.collectAsState()
    val compareSourcesEnabled by viewModel.compareSourcesEnabled.collectAsState()
    val useAlternativeSourcesAuto by viewModel.useAlternativeSourcesAuto.collectAsState()
    val priorityList by viewModel.priorityList.collectAsState()
    val cacheDurationHours by viewModel.cacheDurationHours.collectAsState()

    if (showSplash) {
        SplashScreen(
            onSplashFinished = { showSplash = false }
        )
        return
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            if (!showHistoryView) {
                LexicoTopBar(
                    onHistoryClick = { showHistoryView = true }
                )
            }
        },
        bottomBar = {
            if (!showHistoryView) {
                NavigationBar(
                    modifier = Modifier.testTag("main_navigation_bar"),
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    ScreenTab.ALL.forEach { tab ->
                        val isSelected = currentTabIndex == tab.index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                currentTabIndex = tab.index
                            },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.testTag(tab.testTag)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (showHistoryView) {
                HistoryScreen(
                    history = historyList,
                    onItemClick = { query ->
                        showHistoryView = false
                        currentTabIndex = 1
                        viewModel.submitSearch(query)
                    },
                    onDeleteItem = { id -> viewModel.deleteHistoryItem(id) },
                    onClearAll = { viewModel.clearHistory() },
                    onBack = { showHistoryView = false }
                )
            } else {
                AnimatedContent(
                    targetState = currentTabIndex,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tabContent"
                ) { tab ->
                    when (tab) {
                        0 -> HomeScreen(
                            wordOfTheDay = wordOfTheDay,
                            discoveries = discoveries,
                            searchQuery = searchQuery,
                            onSearchQueryChange = { q ->
                                viewModel.onSearchQueryChanged(q)
                                if (q.isNotBlank()) {
                                    currentTabIndex = 1
                                }
                            },
                            onSearchSubmit = { q ->
                                currentTabIndex = 1
                                viewModel.submitSearch(q)
                            },
                            onNavigateToSearch = { currentTabIndex = 1 },
                            onSelectCategory = { cat -> viewModel.onCategorySelected(cat) },
                            onWordClick = { word -> viewModel.openWordDetail(word) },
                            onSpeakClick = { word -> viewModel.speakWord(word) },
                            onRefreshWordOfDay = { viewModel.refreshWordOfTheDay() }
                        )

                        1 -> SearchScreen(
                            searchQuery = searchQuery,
                            selectedLanguage = selectedLanguage,
                            selectedCategory = selectedCategory,
                            searchResults = searchResults,
                            suggestions = suggestions,
                            onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
                            onSearchSubmit = { viewModel.submitSearch(it) },
                            onLanguageSelected = { viewModel.onLanguageSelected(it) },
                            onCategorySelected = { viewModel.onCategorySelected(it) },
                            onWordClick = { viewModel.openWordDetail(it) },
                            onSpeakClick = { viewModel.speakWord(it) },
                            selectedSource = selectedSource,
                            availableSources = availableSources,
                            currentDictionaryResult = currentDictionaryResult,
                            isSearchingNetwork = isSearchingNetwork,
                            isComparingSources = isComparingSources,
                            onSourceSelected = { viewModel.onSourceSelected(it) },
                            onToggleCompareSources = { viewModel.toggleCompareSources() },
                            onRetry = { viewModel.retryCurrentQuery() }
                        )

                        2 -> FavoritesScreen(
                            favorites = displayedFavorites,
                            filterQuery = favoritesFilterQuery,
                            isSortedAlphabetically = favoritesSortAlphabetical,
                            onFilterQueryChange = { viewModel.onFavoritesQueryChanged(it) },
                            onToggleSort = { viewModel.toggleFavoritesSort() },
                            onOpenFlashcards = { viewModel.openFlashcards() },
                            onWordClick = { wordId -> viewModel.openWordByName(wordId) },
                            onRemoveFavorite = { wordId -> viewModel.removeFavorite(wordId) },
                            onSpeakClick = { text, lang -> viewModel.speakText(text, lang) },
                            onExploreClick = { currentTabIndex = 1 }
                        )

                        3 -> SettingsScreen(
                            themeMode = themeMode,
                            ttsRate = ttsRate,
                            ttsPitch = ttsPitch,
                            cacheWordCount = LocalCorpus.WORDS.size,
                            favoritesCount = favoritesList.size,
                            historyCount = historyList.size,
                            onThemeChange = { viewModel.setThemeMode(it) },
                            onTtsRateChange = { viewModel.setTtsRate(it) },
                            onTtsPitchChange = { viewModel.setTtsPitch(it) },
                            onTestTts = {
                                viewModel.speakText("Bienvenidos a Léxico Pro de la Institución Educativa Fe y Alegría 31.", "es")
                            },
                            onResetCache = {
                                viewModel.clearHistory()
                                viewModel.clearFavorites()
                            },
                            defaultSource = defaultSource,
                            allowSourceSwitch = allowSourceSwitch,
                            compareSourcesEnabled = compareSourcesEnabled,
                            useAlternativeSourcesAuto = useAlternativeSourcesAuto,
                            priorityList = priorityList,
                            cacheDurationHours = cacheDurationHours,
                            onDefaultSourceChange = { viewModel.setDefaultSource(it) },
                            onAllowSourceSwitchChange = { viewModel.setAllowSourceSwitch(it) },
                            onCompareSourcesChange = { viewModel.setCompareSourcesEnabled(it) },
                            onUseAlternativeSourcesAutoChange = { viewModel.setUseAlternativeSourcesAuto(it) },
                            onMovePriorityUp = { viewModel.movePriorityUp(it) },
                            onMovePriorityDown = { viewModel.movePriorityDown(it) },
                            onCacheDurationChange = { viewModel.setCacheDurationHours(it) },
                            onClearNetworkCache = { viewModel.clearNetworkCache() }
                        )
                    }
                }
            }

            // Word Detail Modal Bottom Sheet
            if (isDetailOpen && selectedWord != null) {
                WordDetailSheet(
                    word = selectedWord!!,
                    isFavorite = isFavoriteOfSelected,
                    isSpeaking = isSpeaking,
                    onFavoriteToggle = { viewModel.toggleFavorite(selectedWord!!) },
                    onSpeakClick = { viewModel.speakWord(selectedWord!!) },
                    onClose = { viewModel.closeWordDetail() },
                    onWordClick = { relatedWord -> viewModel.openWordByName(relatedWord) },
                    dictionaryResult = currentDictionaryResult,
                    selectedSource = selectedSource,
                    availableSources = availableSources,
                    isComparingSources = isComparingSources,
                    comparedResults = comparedResults,
                    isSearchingNetwork = isSearchingNetwork,
                    onSourceSelected = { viewModel.onSourceSelected(it) },
                    onToggleCompareSources = { viewModel.toggleCompareSources() },
                    onRetry = { viewModel.retryCurrentQuery() }
                )
            }

            // Flashcard Practice Dialog
            if (isFlashcardsActive) {
                FlashcardDialog(
                    words = flashcardWords,
                    currentIndex = flashcardIndex,
                    isFlipped = isCardFlipped,
                    onFlip = { viewModel.flipFlashcard() },
                    onNext = { viewModel.nextFlashcard() },
                    onPrevious = { viewModel.previousFlashcard() },
                    onSpeak = { word -> viewModel.speakWord(word) },
                    onClose = { viewModel.closeFlashcards() }
                )
            }
        }
    }
}
