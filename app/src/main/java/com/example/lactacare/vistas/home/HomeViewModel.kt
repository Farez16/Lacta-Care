package com.example.lactacare.vistas.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lactacare.datos.repository.AuthRepositoryImpl
import com.example.lactacare.dominio.AdminRepository
import com.example.lactacare.dominio.MedicoRepository
import com.example.lactacare.dominio.PacienteRepository
import com.example.lactacare.dominio.RolUsuario
import com.example.lactacare.vistas.navegacion.ItemMenu
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Estado para la UI del Home (Nombre, saludo, carga)
data class HomeUiState(
    val nombreUsuario: String = "Usuario",
    val isLoading: Boolean = false

)

class HomeViewModel(
    private val pacienteRepo: PacienteRepository,
    private val medicoRepo: MedicoRepository,
    private val adminRepo: AdminRepository,
    private val authRepository: AuthRepositoryImpl // Inyectamos el repo de auth
) : ViewModel() {

    // Estado del Rol actual
    private val _rolActual = MutableStateFlow(RolUsuario.PACIENTE)
    val rolActual: StateFlow<RolUsuario> = _rolActual.asStateFlow()

    // Estado de la Sección seleccionada
    private val _seccionActual = MutableStateFlow<ItemMenu>(ItemMenu.Inicio)
    val seccionActual: StateFlow<ItemMenu> = _seccionActual.asStateFlow()


    // Nuevo: Estado de Datos de Usuario (Para quitar el hardcode)
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun setRol(rol: RolUsuario) {
        _rolActual.value = rol
        cargarDatosUsuario(rol)
    }

    // ... dentro de HomeViewModel ...

    private fun cargarDatosUsuario(rol: RolUsuario) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Obtenemos el ID real de la sesión actual
            val session = authRepository.getCurrentSession()
            val userId = session?.userId?.toInt() ?: -1

            val nombreRecuperado = if (userId != -1) {
                when (rol) {
                    RolUsuario.PACIENTE -> {
                        val paciente = pacienteRepo.obtenerPacientePorId(userId)
                        if (paciente != null) "Hola, ${paciente.primerNombre}" else "Hola, Mamá"
                    }
                    RolUsuario.DOCTOR -> {
                        val medico = medicoRepo.obtenerMedicoPorId(userId)
                        if (medico != null) "Dr. ${medico.primerNombre}" else "Dr. Especialista"
                    }
                    RolUsuario.ADMINISTRADOR -> {
                        val admin = adminRepo.obtenerAdminPorId(userId)
                        if (admin != null) "Admin. ${admin.primerNombre}" else "Administrador"
                    }
                }
            } else {
                "Bienvenida"
            }

            _uiState.value = HomeUiState(
                nombreUsuario = nombreRecuperado,
                isLoading = false
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