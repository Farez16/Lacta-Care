package com.example.lactacare.datos

import com.example.lactacare.dominio.*
import kotlinx.coroutines.delay

class MockLactarioRepository : LactarioRepository {

    // Datos falsos "Hardcodeados"
    private val instituciones = listOf(
        Institucion(1, "Hospital Vicente Corral", "https://via.placeholder.com/150"),
        Institucion(2, "Clínica Santa Inés", "https://via.placeholder.com/150")
    )

    private val lactarios = listOf(
        Lactario(
            id = 1,
            nombre = "Lactario Planta Baja",
            direccion = "Av. 12 de Abril, Cuenca",
            correo = "lactario1@hospital.com",
            telefono = "072888888",
            latitud = "-2.900128",  // String como en tu BD
            longitud = "-79.005896", // String como en tu BD
            idInstitucion = 1
        ),
        Lactario(
            id = 2,
            nombre = "Lactario Pediatría",
            direccion = "Av. Daniel Córdova",
            correo = "lactario2@clinica.com",
            telefono = "072999999",
            latitud = "-2.901500",
            longitud = "-79.006000",
            idInstitucion = 2
        )
    )

    override suspend fun obtenerLactarios(): List<Lactario> {
        delay(1000) // Simulamos 1 segundo de "carga" de internet
        return lactarios
    }

    override suspend fun obtenerLactarioPorId(id: Int): Lactario? {
        delay(500)
        return lactarios.find { it.id == id }
    }

    override suspend fun obtenerInstituciones(): List<Institucion> {
        delay(500)
        return instituciones
    }
}