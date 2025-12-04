package com.example.lactacare.datos

import com.example.lactacare.dominio.Paciente
import com.example.lactacare.dominio.PacienteRepository
import kotlinx.coroutines.delay

class MockPacienteRepository : PacienteRepository {
    override suspend fun registrar(paciente: Paciente): Result<Boolean> {
        delay(2000)
        return if (paciente.cedula.isNotEmpty()) Result.success(true) else Result.failure(Exception("Falta cédula"))
    }

    override suspend fun login(correo: String, clave: String): Result<Boolean> {
        delay(1000)
        return Result.success(true) // Login simple para paciente
    }
}