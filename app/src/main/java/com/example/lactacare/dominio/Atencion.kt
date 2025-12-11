package com.example.lactacare.dominio

data class Atencion(
    val id: Int,
    val idEmpleado: Int, // Quién atendió
    val idReserva: Int,
    val fecha: String,
    val hora: String
)