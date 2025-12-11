package com.example.lactacare.datos

import com.example.lactacare.dominio.*
import kotlinx.coroutines.delay

class MockBebeRepository : BebeRepository {

    private val bebesFalsos = mutableListOf(
        Bebe(1, 1, "Thiago", "2025-01-15", "Masculino"),
        Bebe(2, 1, "Lucía", "2024-06-20", "Femenino")
    )

    override suspend fun obtenerBebesPorFamiliar(idFamiliar: Int): List<Bebe> {
        delay(500)
        return bebesFalsos.filter { it.idFamiliar == idFamiliar }
    }

    override suspend fun registrarBebe(bebe: Bebe): Boolean {
        delay(1000)
        bebesFalsos.add(bebe)
        return true
    }
}