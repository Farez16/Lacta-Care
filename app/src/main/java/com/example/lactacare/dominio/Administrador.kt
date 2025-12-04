package com.example.lactacare.dominio

data class Administrador(
    val idEmpleado: Int = 0, // Mismo caso: por defecto 0

    val cedula: String = "",
    val primerNombre: String = "",
    val segundoNombre: String = "",
    val primerApellido: String = "",
    val segundoApellido: String = "",
    val correo: String = "",
    val telefono: String = "",
    val fechaNacimiento: String = "",
    val fotoPerfil: String = "",
    // Específicos
    val codigoEmpleado: String = "",
    val departamento: String = ""
)