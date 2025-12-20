package com.example.lactacare.datos

import com.example.lactacare.dominio.Administrador
import com.example.lactacare.dominio.AdminRepository
import kotlinx.coroutines.delay

class MockAdminRepository : AdminRepository {

    override suspend fun obtenerAdminPorId(id: Int): Administrador? {
        delay(500)
        // Usamos EXACTAMENTE los campos de tu data class Administrador
        return Administrador(
            idEmpleado = 1,
            cedula = "0999999999",
            primerNombre = "Ana",       // Se mostrará en el Home
            segundoNombre = "Lucia",
            primerApellido = "Gomez",
            segundoApellido = "Ruiz",
            correo = "admin@sistema.com",
            telefono = "0987654321",
            fechaNacimiento = "1990-05-15",
            fotoPerfil = "",
            password = "admin",
            // Campos específicos de Admin
            codigoEmpleado = "ADM-2025",

        )
    }

    override suspend fun login(correo: String, clave: String): Result<Boolean> {
        delay(1000)
        return Result.success(true)
    }

    // Obligatorio para cumplir con la interfaz
    override suspend fun registrar(admin: Administrador): Result<Boolean> {
        delay(1500)
        return Result.success(true)
    }
}