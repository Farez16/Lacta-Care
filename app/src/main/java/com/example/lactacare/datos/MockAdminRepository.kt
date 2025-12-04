package com.example.lactacare.datos

import com.example.lactacare.dominio.Administrador
import com.example.lactacare.dominio.AdminRepository
import kotlinx.coroutines.delay

class MockAdminRepository : AdminRepository {
    override suspend fun registrar(admin: Administrador): Result<Boolean> {
        delay(2000)
        return if (admin.codigoEmpleado.isNotEmpty()) {
            println("MOCK: Admin ${admin.codigoEmpleado} registrado.")
            Result.success(true)
        } else {
            Result.failure(Exception("Código de empleado requerido"))
        }
    }

    override suspend fun login(correo: String, clave: String): Result<Boolean> {
        delay(1000)
        return Result.success(true)
    }
}