package com.example.lactacare.vistas.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lactacare.dominio.AdminRepository
import com.example.lactacare.dominio.MedicoRepository
import com.example.lactacare.dominio.PacienteRepository
import com.example.lactacare.dominio.RolUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PerfilUiState(
    val isLoading: Boolean = false,
    val nombreCompleto: String = "",
    val correo: String = "",
    // Usamos un Mapa para que la lista de detalles sea dinámica según el rol
    val detalles: Map<String, String> = emptyMap(),
    val error: String? = null
)

class PerfilViewModel(
    private val pacienteRepo: PacienteRepository,
    private val medicoRepo: MedicoRepository,
    private val adminRepo: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState(isLoading = true))
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    fun cargarPerfil(rol: RolUsuario) {
        viewModelScope.launch {
            _uiState.value = PerfilUiState(isLoading = true)
            try {
                val idUsuario = 1 // Simulamos el ID del usuario logueado

                when (rol) {
                    RolUsuario.PACIENTE -> {
                        val p = pacienteRepo.obtenerPacientePorId(idUsuario)
                        if (p != null) {
                            _uiState.value = PerfilUiState(
                                nombreCompleto = "${p.primerNombre} ${p.primerApellido}",
                                correo = p.correo,
                                detalles = mapOf(
                                    "Cédula" to p.cedula,
                                    "Teléfono" to p.telefono,
                                    "Fecha Nacimiento" to p.fechaNacimiento
                                )
                            )
                        }
                    }
                    RolUsuario.DOCTOR -> {
                        val m = medicoRepo.obtenerMedicoPorId(idUsuario)
                        if (m != null) {
                            _uiState.value = PerfilUiState(
                                nombreCompleto = "Dr. ${m.primerNombre} ${m.primerApellido}",
                                correo = m.correo,
                                detalles = mapOf(
                                    "Especialidad" to (m.especialidad ?: "General"),
                                    "Cédula" to m.cedula,
                                    "Teléfono" to m.telefono
                                )
                            )
                        }
                    }
                    RolUsuario.ADMINISTRADOR -> {
                        val a = adminRepo.obtenerAdminPorId(idUsuario)
                        if (a != null) {
                            _uiState.value = PerfilUiState(
                                nombreCompleto = "${a.primerNombre} ${a.primerApellido}",
                                correo = a.correo,
                                detalles = mapOf(
                                    // Aquí usamos los campos específicos de tu Entidad Admin
                                    "Departamento" to a.departamento,
                                    "Código Empleado" to a.codigoEmpleado,
                                    "Cédula" to a.cedula,
                                    "Teléfono" to a.telefono
                                )
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = PerfilUiState(error = "Error al cargar: ${e.message}")
            }
        }
    }
}