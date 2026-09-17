package com.example.dictionary.manager

import com.example.dictionary.model.DictionarySourceId
import com.example.dictionary.provider.CambridgeProvider
import com.example.dictionary.provider.CollinsProvider
import com.example.dictionary.provider.DictionaryProvider
import com.example.dictionary.provider.FreeDictionaryProvider
import com.example.dictionary.provider.LocalCorpusProvider
import com.example.dictionary.provider.MerriamWebsterProvider
import com.example.dictionary.provider.OxfordProvider
import com.example.dictionary.provider.RAEProvider
import com.example.dictionary.provider.WikidataProvider
import com.example.dictionary.provider.WiktionaryProvider
import com.example.dictionary.provider.WordnikProvider

class SourceManager {

    private val allProviders: List<DictionaryProvider> = listOf(
        WiktionaryProvider(),
        FreeDictionaryProvider(),
        WikidataProvider(),
        LocalCorpusProvider(),
        WordnikProvider(),
        RAEProvider(),
        CambridgeProvider(),
        MerriamWebsterProvider(),
        CollinsProvider(),
        OxfordProvider()
    )

    fun getProvider(sourceId: DictionarySourceId): DictionaryProvider? {
        return allProviders.firstOrNull { it.sourceId == sourceId }
    }

    /**
     * Returns only providers that are actually configured, available, and legal to use.
     */
    fun getAvailableProviders(): List<DictionaryProvider> {
        return allProviders.filter { it.isAvailable() }
    }

    /**
     * Returns available providers compatible with the specified language.
     */
    fun getAvailableProvidersForLanguage(languageCode: String): List<DictionaryProvider> {
        val lang = languageCode.lowercase()
        return getAvailableProviders().filter { provider ->
            if (lang == "todos" || lang == "all") {
                true
            } else {
                provider.supportsLanguage(lang)
            }
        }
    }

    /**
     * Returns the list of source IDs that should be shown to the user in the selector,
     * always including AUTOMATIC plus compatible available sources.
     */
    fun getSelectableSourcesForLanguage(languageCode: String): List<DictionarySourceId> {
        val compatible = getAvailableProvidersForLanguage(languageCode).map { it.sourceId }
        return listOf(DictionarySourceId.AUTOMATIC) + compatible
    }
}
