package com.example.lactacare.dominio

interface ReservasRepository {
    suspend fun crearReserva(reserva: Reserva): Boolean
    suspend fun obtenerReservasPorPaciente(idPaciente: Int): List<Reserva>
    suspend fun cancelarReserva(idReserva: Int): Boolean
}