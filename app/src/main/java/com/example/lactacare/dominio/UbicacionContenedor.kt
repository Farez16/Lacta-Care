package com.example.lactacare.dominio

data class UbicacionContenedor(
    val id: Int,
    val idContenedor: Int,
    val idRefrigerador: Int,
    val piso: Int,
    val fila: Int,
    val columna: Int
)