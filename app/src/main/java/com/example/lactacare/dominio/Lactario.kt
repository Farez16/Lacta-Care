package com.example.lactacare.dominio

data class Lactario(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val correo: String,
    val telefono: String,

    // 1. Los dejamos como String para coincidir con tu SQL (VARCHAR)
    val latitud: String,
    val longitud: String,

    val idInstitucion: Int
) {
    // 2. Agregamos estos "Ayudantes" para el Mapa
    // Si el String viene vacío o mal escrito, devolverá 0.0 y la app no se cerrará.
    fun obtenerLatitudNum(): Double {
        return latitud.toDoubleOrNull() ?: 0.0
    }

    fun obtenerLongitudNum(): Double {
        return longitud.toDoubleOrNull() ?: 0.0
    }
}