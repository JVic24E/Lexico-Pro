package com.example

import com.example.dictionary.manager.CacheManager
import com.example.dictionary.manager.SourceManager
import com.example.dictionary.manager.SourcePriorityManager
import com.example.dictionary.model.DictionaryResult
import com.example.dictionary.model.DictionarySourceId
import com.example.dictionary.model.toWordEntry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class DictionarySystemTest {

    @Test
    fun sourceManager_containsRequiredSources() {
        val sourceManager = SourceManager()
        val spanishSources = sourceManager.getSelectableSourcesForLanguage("es")

        assertTrue("Must include AUTOMATIC", spanishSources.contains(DictionarySourceId.AUTOMATIC))
        assertTrue("Must include WIKTIONARY", spanishSources.contains(DictionarySourceId.WIKTIONARY))
        assertTrue("Must include LOCAL_CORPUS", spanishSources.contains(DictionarySourceId.LOCAL_CORPUS))

        val englishSources = sourceManager.getSelectableSourcesForLanguage("en")
        assertTrue(englishSources.contains(DictionarySourceId.FREE_DICTIONARY))
        assertTrue(englishSources.contains(DictionarySourceId.WIKTIONARY))
    }

    @Test
    fun sourcePriorityManager_reorderingWorks() {
        val priorityManager = SourcePriorityManager()
        val initialList = priorityManager.priorityList.value

        assertEquals(DictionarySourceId.AUTOMATIC, priorityManager.defaultSource.value)

        if (initialList.size >= 2) {
            val secondItem = initialList[1]
            priorityManager.movePriorityUp(1)
            assertEquals(secondItem, priorityManager.priorityList.value[0])

            priorityManager.movePriorityDown(0)
            assertEquals(secondItem, priorityManager.priorityList.value[1])
        }
    }

    @Test
    fun cacheManager_storesAndExpires() {
        val cache = CacheManager()
        cache.cacheDurationMillis = 500
        val dummyResult = DictionaryResult(
            palabra = "latrocinio",
            idioma = "es",
            fuente = "Wiktionary",
            definiciones = listOf("Acción propia de un ladrón.")
        )

        cache.put("latrocinio", "es", DictionarySourceId.WIKTIONARY.code, dummyResult)
        val cached = cache.get("latrocinio", "es", DictionarySourceId.WIKTIONARY.code)

        assertNotNull(cached)
        assertEquals("latrocinio", cached?.palabra)
        assertEquals("Wiktionary", cached?.fuente)

        cache.clear()
        assertNull(cache.get("latrocinio", "es", DictionarySourceId.WIKTIONARY.code))
    }

    @Test
    fun dictionaryMappers_mapsToWordEntryCorrectly() {
        val result = DictionaryResult(
            palabra = "liberté",
            idioma = "fr",
            fuente = "Wiktionary",
            categoriaGramatical = "Nom commun",
            ipa = "/li.bɛʁ.te/",
            definiciones = listOf("État d'une personne libre."),
            ejemplos = listOf("La liberté guidant le peuple."),
            sinonimos = listOf("affranchissement", "émancipation"),
            atribucion = "Wiktionnaire",
            licencia = "CC BY-SA 4.0"
        )

        val entry = result.toWordEntry()
        assertEquals("liberté", entry.word)
        assertEquals("fr", entry.language)
        assertEquals("Francés", entry.languageName)
        assertEquals("/li.bɛʁ.te/", entry.phonetic)
        assertEquals("Nom commun", entry.partOfSpeech)
        assertEquals(1, entry.definitions.size)
        assertEquals("État d'une personne libre.", entry.definitions.first())
        assertEquals(1, entry.examples.size)
        assertEquals(2, entry.synonyms.size)
    }
}
