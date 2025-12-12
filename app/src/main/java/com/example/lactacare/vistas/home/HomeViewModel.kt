package com.example.lactacare.vistas.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lactacare.dominio.*
import com.example.lactacare.vistas.navegacion.ItemMenu
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estado ampliado con datos del Dashboard
data class HomeUiState(
    val nombreUsuario: String = "Cargando...",
    val isLoading: Boolean = false,
    // Datos dinámicos para el Dashboard Paciente
    val proximaReserva: Reservas? = null,
    val nombreBebe: String? = null
)

class HomeViewModel(
    private val pacienteRepo: PacienteRepository,
    private val medicoRepo: MedicoRepository,
    private val adminRepo: AdminRepository,
    // Nuevas dependencias para llenar el Dashboard
    private val reservaRepo: ReservasRepository,
    private val bebeRepo: BebeRepository
) : ViewModel() {

    private val _rolActual = MutableStateFlow(RolUsuario.PACIENTE)
    val rolActual: StateFlow<RolUsuario> = _rolActual.asStateFlow()

    private val _seccionActual = MutableStateFlow<ItemMenu>(ItemMenu.Inicio)
    val seccionActual: StateFlow<ItemMenu> = _seccionActual.asStateFlow()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun setRol(rol: RolUsuario) {
        _rolActual.value = rol
        cargarDatosUsuario(rol)
    }

    private fun cargarDatosUsuario(rol: RolUsuario) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val idUsuario = 1 // Simulamos usuario logueado

            // 1. Cargar Nombre según Rol
            val nombreRecuperado = when (rol) {
                RolUsuario.PACIENTE -> {
                    val p = pacienteRepo.obtenerPacientePorId(idUsuario)
                    p?.primerNombre ?: "Mamá"
                }
                RolUsuario.DOCTOR -> {
                    val m = medicoRepo.obtenerMedicoPorId(idUsuario)
                    "Dr. ${m?.primerNombre ?: "Especialista"}"
                }
                RolUsuario.ADMINISTRADOR -> {
                    val a = adminRepo.obtenerAdminPorId(idUsuario)
                    "Admin. ${a?.primerNombre ?: "Sistema"}"
                }
            }

            // 2. Si es Paciente, cargar datos del Dashboard (Citas y Bebés)
            var proximaCita: Reservas? = null
            var miBebe: String? = null

            if (rol == RolUsuario.PACIENTE) {
                // Cargar próxima cita
                proximaCita = reservaRepo.obtenerProximaReservaPaciente(idUsuario)

                // Cargar nombre del primer bebé
                val bebes = bebeRepo.obtenerBebesPorFamiliar(idUsuario)
                if (bebes.isNotEmpty()) {
                    miBebe = bebes[0].nombre
                }
            }

            _uiState.value = HomeUiState(
                nombreUsuario = "Hola, $nombreRecuperado",
                isLoading = false,
                proximaReserva = proximaCita,
                nombreBebe = miBebe
            )
        }
    }

    fun seleccionarSeccion(item: ItemMenu) {
        _seccionActual.value = item
    }

    fun obtenerMenuPorRol(rol: RolUsuario): List<ItemMenu> {
        return when (rol) {
            RolUsuario.PACIENTE -> listOf(ItemMenu.Inicio, ItemMenu.Chatbot, ItemMenu.Registros, ItemMenu.Perfil)
            RolUsuario.DOCTOR -> listOf(ItemMenu.Inicio, ItemMenu.Agenda, ItemMenu.Pacientes, ItemMenu.Perfil)
            RolUsuario.ADMINISTRADOR -> listOf(ItemMenu.Inicio, ItemMenu.Empleados, ItemMenu.Reportes, ItemMenu.Perfil)
        }
    }
}