package com.example.lactacare.dominio

interface AdminRepository {
    suspend fun registrar(admin: Administrador): Result<Boolean>
    suspend fun login(correo: String, clave: String): Result<Boolean>
}