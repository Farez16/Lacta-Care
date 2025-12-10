package com.example.lactacare.vistas

import androidx.lifecycle.ViewModel
import com.example.lactacare.dominio.RolUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    // Estado del Rol actual
    private val _rolActual = MutableStateFlow(RolUsuario.PACIENTE)
    val rolActual: StateFlow<RolUsuario> = _rolActual.asStateFlow()

    // Estado de la Sección seleccionada (Por defecto "Inicio")
    private val _seccionActual = MutableStateFlow<ItemMenu>(ItemMenu.Inicio)
    val seccionActual: StateFlow<ItemMenu> = _seccionActual.asStateFlow()

    // Función para inicializar el rol (la llamaremos desde el MainActivity)
    fun setRol(rol: RolUsuario) {
        _rolActual.value = rol
    }

    // Función para cambiar de pestaña en el BottomBar
    fun seleccionarSeccion(item: ItemMenu) {
        _seccionActual.value = item
    }

    // --- LÓGICA CORE: ¿Qué menú muestro? ---
    fun obtenerMenuPorRol(rol: RolUsuario): List<ItemMenu> {
        return when (rol) {
            RolUsuario.PACIENTE -> listOf(
                ItemMenu.Inicio,      // Fijo
                ItemMenu.Chatbot,     // Variable
                ItemMenu.Registros,   // Variable
                ItemMenu.Perfil       // Fijo
            )
            RolUsuario.DOCTOR -> listOf(
                ItemMenu.Inicio,
                ItemMenu.Agenda,      // Variable
                ItemMenu.Pacientes,   // Variable
                ItemMenu.Perfil
            )
            RolUsuario.ADMINISTRADOR -> listOf(
                ItemMenu.Inicio,
                ItemMenu.Empleados,   // Variable
                ItemMenu.Reportes,    // Variable
                ItemMenu.Perfil
            )
        }
    }
}