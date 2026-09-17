package com.example.dictionary.model

import com.example.model.SupportedLanguages
import com.example.model.WordEntry

fun DictionaryResult.toWordEntry(): WordEntry {
    return WordEntry(
        id = "${palabra.lowercase().trim()}_${fuente.lowercase().replace(" ", "_")}",
        word = palabra,
        phonetic = if (ipa.isNotBlank()) ipa else pronunciacion,
        partOfSpeech = if (categoriaGramatical.isNotBlank()) categoriaGramatical else "Léxico",
        language = idioma,
        languageName = SupportedLanguages.getByCode(idioma).name,
        etymology = etimologia,
        definitions = if (definiciones.isNotEmpty()) definiciones else listOf("No disponible"),
        examples = ejemplos,
        synonyms = sinonimos,
        antonyms = antonimos,
        translations = traducciones,
        category = if (dificultad.isNotBlank()) dificultad else "General",
        inspirationQuote = if (expresiones.isNotEmpty()) expresiones.first() else ""
    )
}
