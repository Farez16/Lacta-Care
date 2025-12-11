package com.example.lactacare.dominio

interface MedicoRepository {
    suspend fun registrar(medico: Medico): Result<Boolean>
    suspend fun login(correo: String, clave: String): Result<Boolean>
    suspend fun obtenerMedicoPorId(id: Int): Medico?
}