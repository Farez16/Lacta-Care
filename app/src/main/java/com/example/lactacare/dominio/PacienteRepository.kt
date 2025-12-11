package com.example.lactacare.dominio

interface PacienteRepository {
    suspend fun registrar(paciente: Paciente): Result<Boolean>
    suspend fun login(correo: String, clave: String): Result<Boolean>

    // --- AGREGA ESTA LÍNEA ---
    suspend fun obtenerPacientePorId(id: Int): Paciente?
}