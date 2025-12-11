package com.example.lactacare.datos

import com.example.lactacare.dominio.*
import kotlinx.coroutines.delay

class MockReservasRepository : ReservasRepository {

    // Lista mutable para simular base de datos en memoria RAM
    private val reservasFalsas = mutableListOf(
        Reserva(101, 1, 1, "Confirmada", "2025-12-12", "10:00", "10:30"),
        Reserva(102, 1, 1, "Pendiente", "2025-12-14", "14:00", "14:30")
    )

    override suspend fun crearReserva(reserva: Reserva): Boolean {
        delay(1500) // Simula tiempo de guardado
        reservasFalsas.add(reserva)
        return true // Siempre decimos que sí se guardó
    }

    override suspend fun obtenerReservasPorPaciente(idPaciente: Int): List<Reserva> {
        delay(1000)
        // Filtramos solo las del usuario actual
        return reservasFalsas.filter { it.idPaciente == idPaciente }
    }

    override suspend fun cancelarReserva(idReserva: Int): Boolean {
        delay(1000)
        val reserva = reservasFalsas.find { it.id == idReserva }
        return if (reserva != null) {
            reservasFalsas.remove(reserva)
            true
        } else {
            false
        }
    }
}