package com.example.dictionary.model

enum class DictionarySourceId(
    val code: String,
    val displayName: String,
    val isPubliclyUsable: Boolean,
    val requiresApiKey: Boolean,
    val supportedLanguages: Set<String>, // "es", "en", "fr", "la", "qu", "all"
    val officialUrl: String,
    val attributionText: String,
    val licenseText: String
) {
    AUTOMATIC(
        code = "auto",
        displayName = "Automática (Inteligente)",
        isPubliclyUsable = true,
        requiresApiKey = false,
        supportedLanguages = setOf("all"),
        officialUrl = "",
        attributionText = "Selección automática según idioma y disponibilidad",
        licenseText = ""
    ),
    WIKTIONARY(
        code = "wiktionary",
        displayName = "Wiktionary",
        isPubliclyUsable = true,
        requiresApiKey = false,
        supportedLanguages = setOf("es", "en", "fr", "la", "qu", "de", "it", "pt", "all"),
        officialUrl = "https://wiktionary.org",
        attributionText = "Fuente: Wiktionary (Fundación Wikimedia)",
        licenseText = "Licencia Creative Commons Atribución-CompartirIgual (CC BY-SA 4.0)"
    ),
    FREE_DICTIONARY(
        code = "free_dict",
        displayName = "Free Dictionary API",
        isPubliclyUsable = true,
        requiresApiKey = false,
        supportedLanguages = setOf("en", "es", "fr"),
        officialUrl = "https://dictionaryapi.dev",
        attributionText = "Fuente: Free Dictionary API (dictionaryapi.dev)",
        licenseText = "Datos bajo licencias abiertas (Wiktionary / CC BY-SA)"
    ),
    WIKIDATA(
        code = "wikidata",
        displayName = "Wikidata Lexicographical Data",
        isPubliclyUsable = true,
        requiresApiKey = false,
        supportedLanguages = setOf("es", "en", "fr", "la", "qu", "de", "it", "all"),
        officialUrl = "https://www.wikidata.org",
        attributionText = "Fuente: Wikidata Lexemes (Fundación Wikimedia)",
        licenseText = "Licencia Creative Commons Cero (CC0 1.0 Universal)"
    ),
    LOCAL_CORPUS(
        code = "local_fya",
        displayName = "Corpus Fe y Alegría 31",
        isPubliclyUsable = true,
        requiresApiKey = false,
        supportedLanguages = setOf("es", "en", "fr", "la", "qu", "all"),
        officialUrl = "",
        attributionText = "Fuente: Repositorio Curricular I.E. Fe y Alegría 31",
        licenseText = "Uso Educativo Institucional"
    ),
    WORDNIK(
        code = "wordnik",
        displayName = "Wordnik",
        isPubliclyUsable = false, // Requires private API key
        requiresApiKey = true,
        supportedLanguages = setOf("en"),
        officialUrl = "https://developer.wordnik.com",
        attributionText = "Fuente: Wordnik API",
        licenseText = "Términos de servicio Wordnik API"
    ),
    RAE_DLE(
        code = "rae",
        displayName = "RAE / DLE",
        isPubliclyUsable = false, // Requires authorized institutional credentials
        requiresApiKey = true,
        supportedLanguages = setOf("es"),
        officialUrl = "https://dle.rae.es",
        attributionText = "Fuente: Real Academia Española (DLE)",
        licenseText = "Todos los derechos reservados RAE"
    ),
    CAMBRIDGE(
        code = "cambridge",
        displayName = "Cambridge Dictionary",
        isPubliclyUsable = false,
        requiresApiKey = true,
        supportedLanguages = setOf("en"),
        officialUrl = "https://dictionary.cambridge.org",
        attributionText = "Fuente: Cambridge Dictionary API",
        licenseText = "Licencia comercial autorizada Cambridge"
    ),
    MERRIAM_WEBSTER(
        code = "merriam_webster",
        displayName = "Merriam-Webster",
        isPubliclyUsable = false,
        requiresApiKey = true,
        supportedLanguages = setOf("en", "es"),
        officialUrl = "https://dictionaryapi.com",
        attributionText = "Fuente: Merriam-Webster Dictionary API",
        licenseText = "Licencia Merriam-Webster Developer API"
    ),
    COLLINS(
        code = "collins",
        displayName = "Collins Dictionary",
        isPubliclyUsable = false,
        requiresApiKey = true,
        supportedLanguages = setOf("en", "fr", "es"),
        officialUrl = "https://api.collinsdictionary.com",
        attributionText = "Fuente: HarperCollins Publishers API",
        licenseText = "Licencia HarperCollins"
    ),
    OXFORD(
        code = "oxford",
        displayName = "Oxford Languages",
        isPubliclyUsable = false,
        requiresApiKey = true,
        supportedLanguages = setOf("en", "es", "fr"),
        officialUrl = "https://developer.oxforddictionaries.com",
        attributionText = "Fuente: Oxford Dictionaries API",
        licenseText = "Licencia comercial Oxford Languages"
    );

    companion object {
        fun fromCode(code: String): DictionarySourceId {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: AUTOMATIC
        }
    }
}
