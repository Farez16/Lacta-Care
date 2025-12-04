package com.example.lactacare.dominio

// Esto es una "Sealed Class" (Clase Sellada).
// Significa que en tu app SOLO pueden existir estos 3 tipos de usuarios, nada más.
enum class RolUsuario {
    ADMINISTRADOR,
    DOCTOR,
    PACIENTE // (Usuario normal)
}