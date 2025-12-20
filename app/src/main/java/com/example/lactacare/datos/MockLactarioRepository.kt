package com.example.lactacare.datos

import com.example.lactacare.dominio.Institucion
import com.example.lactacare.dominio.Lactario
import com.example.lactacare.dominio.LactarioRepository
import kotlinx.coroutines.delay

class MockLactarioRepository : LactarioRepository {

    // Datos maestros en memoria
    private val listaLactarios = listOf(
        Lactario(
            id = 1,
            nombre = "Sala 3A - Principal",
            direccion = "Piso 3, Bloque A",
            correo = "sala3a@hosp.com",
            telefono = "0991234567",
            latitud = "-2.90055",
            longitud = "-79.00245",
            idInstitucion = 100
        ),
        Lactario(
            id = 2,
            nombre = "Sala 2B - RRHH",
            direccion = "Piso 2, Oficina 204",
            correo = "rrhh@hosp.com",
            telefono = "0987654321",
            latitud = "-2.90100",
            longitud = "-79.00300",
            idInstitucion = 100
        ),
        Lactario(
            id = 3,
            nombre = "Sala 1 - Recepción",
            direccion = "Planta Baja",
            correo = "recepcion@hosp.com",
            telefono = "0981122334",
            latitud = "-2.89900",
            longitud = "-79.00100",
            idInstitucion = 100
        ),
        Lactario(
            id = 4,
            nombre = "Lactario Sur",
            direccion = "Bloque Sur, Cafetería",
            correo = "sur@hosp.com",
            telefono = "0999999999",
            latitud = "-2.90500",
            longitud = "-79.00500",
            idInstitucion = 101
        )
    )

    // 1. Obtener Lactarios (Tu función principal)
    override suspend fun obtenerLactarios(): List<Lactario> {
        delay(500)
        return listaLactarios
    }

    // 2. Obtener por ID
    override suspend fun obtenerLactarioPorId(id: Int): Lactario? {
        delay(200)
        return listaLactarios.find { it.id == id }
    }

    // 3. Obtener Todos (Esta faltaba y es redundante con la 1, pero la pide la interfaz)
    override suspend fun obtenerTodos(): List<Lactario> {
        return listaLactarios // Devolvemos la misma lista
    }

    // 4. Obtener Instituciones (Esta faltaba)
    override suspend fun obtenerInstituciones(): List<Institucion> {
        delay(500)
        // Retornamos lista vacía por ahora para que compile,
        // ya que no estamos usando pantalla de instituciones todavía.
        return emptyList()
    }
}