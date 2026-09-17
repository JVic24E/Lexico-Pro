package com.example.dictionary.manager

import com.example.dictionary.model.DictionarySourceId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SourcePriorityManager {

    private val defaultPriority = listOf(
        DictionarySourceId.WIKTIONARY,
        DictionarySourceId.FREE_DICTIONARY,
        DictionarySourceId.WIKIDATA,
        DictionarySourceId.LOCAL_CORPUS,
        DictionarySourceId.RAE_DLE,
        DictionarySourceId.WORDNIK,
        DictionarySourceId.MERRIAM_WEBSTER,
        DictionarySourceId.CAMBRIDGE,
        DictionarySourceId.COLLINS,
        DictionarySourceId.OXFORD
    )

    private val _priorityList = MutableStateFlow(defaultPriority)
    val priorityList: StateFlow<List<DictionarySourceId>> = _priorityList.asStateFlow()

    private val _defaultSource = MutableStateFlow(DictionarySourceId.AUTOMATIC)
    val defaultSource: StateFlow<DictionarySourceId> = _defaultSource.asStateFlow()

    private val _allowSourceSwitch = MutableStateFlow(true)
    val allowSourceSwitch: StateFlow<Boolean> = _allowSourceSwitch.asStateFlow()

    private val _compareSourcesEnabled = MutableStateFlow(false)
    val compareSourcesEnabled: StateFlow<Boolean> = _compareSourcesEnabled.asStateFlow()

    private val _useAlternativeSourcesAuto = MutableStateFlow(true)
    val useAlternativeSourcesAuto: StateFlow<Boolean> = _useAlternativeSourcesAuto.asStateFlow()

    fun setDefaultSource(sourceId: DictionarySourceId) {
        _defaultSource.value = sourceId
    }

    fun setAllowSourceSwitch(enabled: Boolean) {
        _allowSourceSwitch.value = enabled
    }

    fun setCompareSourcesEnabled(enabled: Boolean) {
        _compareSourcesEnabled.value = enabled
    }

    fun setUseAlternativeSourcesAuto(enabled: Boolean) {
        _useAlternativeSourcesAuto.value = enabled
    }

    fun setPriorityList(list: List<DictionarySourceId>) {
        _priorityList.value = list
    }

    fun movePriorityUp(index: Int) {
        if (index > 0 && index < _priorityList.value.size) {
            val list = _priorityList.value.toMutableList()
            val item = list.removeAt(index)
            list.add(index - 1, item)
            _priorityList.value = list
        }
    }

    fun movePriorityDown(index: Int) {
        if (index >= 0 && index < _priorityList.value.size - 1) {
            val list = _priorityList.value.toMutableList()
            val item = list.removeAt(index)
            list.add(index + 1, item)
            _priorityList.value = list
        }
    }

    fun getOrderedProvidersForLanguage(sourceManager: SourceManager, languageCode: String): List<com.example.dictionary.provider.DictionaryProvider> {
        val available = sourceManager.getAvailableProvidersForLanguage(languageCode)
        val availableMap = available.associateBy { it.sourceId }

        val ordered = mutableListOf<com.example.dictionary.provider.DictionaryProvider>()
        for (sourceId in _priorityList.value) {
            availableMap[sourceId]?.let { ordered.add(it) }
        }
        for (provider in available) {
            if (!ordered.contains(provider)) {
                ordered.add(provider)
            }
        }
        return ordered
    }
}
