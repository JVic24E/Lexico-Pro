package com.example.model

data class WordEntry(
    val id: String,
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val language: String, // "es", "en", "fr", "la", "qu"
    val languageName: String,
    val etymology: String,
    val definitions: List<String>,
    val examples: List<String>,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
    val translations: Map<String, String> = emptyMap(),
    val category: String, // "Ciencias", "Literatura", "Filosofía", "Peruanismos", "Vocabulario Académico"
    val inspirationQuote: String = "",
    val isWordOfDay: Boolean = false
)

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

data class TtsSettings(
    val speechRate: Float = 1.0f,
    val pitch: Float = 1.0f,
    val currentLanguage: String = "es"
)

data class LanguageOption(
    val code: String,
    val name: String,
    val nativeName: String,
    val flag: String,
    val localeTag: String
)

object SupportedLanguages {
    val ALL = listOf(
        LanguageOption("todos", "Todos", "Omnia", "🌐", "es-ES"),
        LanguageOption("es", "Español", "Castellano", "🇪🇸", "es-ES"),
        LanguageOption("en", "Inglés", "English", "🇬🇧", "en-US"),
        LanguageOption("fr", "Francés", "Français", "🇫🇷", "fr-FR"),
        LanguageOption("la", "Latín", "Latina", "🏛️", "es-ES"), // Latin uses classical phonetic reader
        LanguageOption("qu", "Quechua", "Runasimi", "🏔️", "es-PE") // Quechua uses Andean phonetic reader
    )

    fun getByCode(code: String): LanguageOption {
        return ALL.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: ALL[1]
    }
}

object LexicalCategories {
    const val ALL = "Todos"
    const val CIENCIAS = "Ciencias"
    const val LITERATURA = "Literatura"
    const val FILOSOFIA = "Filosofía"
    const val PERUANISMOS = "Peruanismos"
    const val ACADEMICO = "Vocabulario Académico"

    val LIST = listOf(ALL, ACADEMICO, CIENCIAS, LITERATURA, FILOSOFIA, PERUANISMOS)
}
