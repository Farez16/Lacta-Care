package com.example.lactacare.dominio

data class Refrigerador(
    val id: Int,
    val idLactario: Int,
    val capacidadMax: Int,
    val pisos: Int,
    val filas: Int,
    val columnas: Int
)