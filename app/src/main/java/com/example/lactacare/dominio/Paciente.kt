package com.example.lactacare.dominio

data class Paciente(
    // Agregamos el ID como pediste (por defecto 0 para nuevos registros)
    val idPaciente: Int = 0,

    val cedula: String = "",
    val primerNombre: String = "",
    val segundoNombre: String = "",
    val primerApellido: String = "",
    val segundoApellido: String = "",
    val correo: String = "",
    val telefono: String = "",
    val fechaNacimiento: String = "",
    val discapacidad: String = "",
    val password: String = "",
    val fotoPerfil: String = ""
)