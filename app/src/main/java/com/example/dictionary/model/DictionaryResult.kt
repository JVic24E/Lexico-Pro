package com.example.dictionary.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DictionaryResult(
    val palabra: String,
    val idioma: String,
    val pronunciacion: String = "",
    val ipa: String = "",
    val audioUrl: String = "",
    val categoriaGramatical: String = "",
    val definiciones: List<String> = emptyList(),
    val traducciones: Map<String, String> = emptyMap(),
    val ejemplos: List<String> = emptyList(),
    val sinonimos: List<String> = emptyList(),
    val antonimos: List<String> = emptyList(),
    val etimologia: String = "",
    val formasGramaticales: List<String> = emptyList(),
    val expresiones: List<String> = emptyList(),
    val dificultad: String = "", // e.g. "Básico", "Intermedio", "Avanzado", o "No disponible"
    val fuente: String,
    val urlFuente: String = "",
    val atribucion: String = "",
    val licencia: String = "",
    val fechaConsulta: String = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date()),
    val isFromCache: Boolean = false,
    val errorMensaje: String? = null
) {
    val isSuccess: Boolean
        get() = errorMensaje == null && definiciones.isNotEmpty()
}
