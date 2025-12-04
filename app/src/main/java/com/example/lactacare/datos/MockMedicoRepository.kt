package com.example.lactacare.datos

import com.example.lactacare.dominio.Medico
import com.example.lactacare.dominio.MedicoRepository
import kotlinx.coroutines.delay

class MockMedicoRepository : MedicoRepository {
    override suspend fun registrar(medico: Medico): Result<Boolean> {
        delay(2000)
        return if (medico.licenciaMedica.isNotEmpty()) {
            println("MOCK: Dr. ${medico.primerApellido} registrado.")
            Result.success(true)
        } else {
            Result.failure(Exception("Licencia médica requerida"))
        }
    }

    override suspend fun login(correo: String, clave: String): Result<Boolean> {
        delay(1000)
        // Aquí podrías validar que el correo tenga formato de doctor si quisieras
        return Result.success(true)
    }
}