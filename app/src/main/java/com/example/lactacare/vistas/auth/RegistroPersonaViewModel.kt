package com.example.lactacare.vistas.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lactacare.datos.MockAdminRepository
import com.example.lactacare.datos.MockMedicoRepository
import com.example.lactacare.datos.MockPacienteRepository
import com.example.lactacare.dominio.* // Importa todos los modelos y repos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// UI State unificado: Guarda TODOS los campos posibles como texto
data class RegistroUiState(
    val cedula: String = "",
    val primerNombre: String = "",
    val segundoNombre: String = "",
    val primerApellido: String = "",
    val segundoApellido: String = "",
    val correo: String = "",
    val telefono: String = "",
    val fechaNacimiento: String = "",
    val discapacidad: String = "",
    // Campos Específicos
    val licenciaMedica: String = "",
    val especialidad: String = "",
    val codigoEmpleado: String = "",
    val departamento: String = ""
)

class RegistroPersonaViewModel(
    // Inyectamos los 3 repositorios (Por defecto usamos los Mocks)
    private val pacienteRepo: PacienteRepository = MockPacienteRepository(),
    private val medicoRepo: MedicoRepository = MockMedicoRepository(),
    private val adminRepo: AdminRepository = MockAdminRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState = _uiState.asStateFlow()

    private val _rolActual = MutableStateFlow(RolUsuario.PACIENTE)
    val rolActual = _rolActual.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje = _mensaje.asStateFlow()

    fun setRol(rol: RolUsuario) {
        _rolActual.value = rol
        _mensaje.value = null // Limpiar mensajes al cambiar rol
    }

    // --- SETTERS (Actualizan el UI State) ---
    fun onCedulaChange(v: String) { _uiState.update { it.copy(cedula = v) } }
    fun onPrimerNombreChange(v: String) { _uiState.update { it.copy(primerNombre = v) } }
    fun onSegundoNombreChange(v: String) { _uiState.update { it.copy(segundoNombre = v) } }
    fun onPrimerApellidoChange(v: String) { _uiState.update { it.copy(primerApellido = v) } }
    fun onSegundoApellidoChange(v: String) { _uiState.update { it.copy(segundoApellido = v) } }
    fun onCorreoChange(v: String) { _uiState.update { it.copy(correo = v) } }
    fun onTelefonoChange(v: String) { _uiState.update { it.copy(telefono = v) } }
    fun onFechaNacimientoChange(v: String) { _uiState.update { it.copy(fechaNacimiento = v) } }
    fun onDiscapacidadChange(v: String) { _uiState.update { it.copy(discapacidad = v) } }
    fun onLicenciaChange(v: String) { _uiState.update { it.copy(licenciaMedica = v) } }
    fun onEspecialidadChange(v: String) { _uiState.update { it.copy(especialidad = v) } }
    fun onCodigoEmpleadoChange(v: String) { _uiState.update { it.copy(codigoEmpleado = v) } }
    fun onDepartamentoChange(v: String) { _uiState.update { it.copy(departamento = v) } }

    // --- FUNCIÓN MAESTRA: REGISTRAR ---
    fun registrar() {
        viewModelScope.launch {
            _isLoading.value = true
            _mensaje.value = null

            val datos = _uiState.value
            val resultado: Result<Boolean>

            // AQUÍ OCURRE LA MAGIA: Elegimos qué objeto crear y a quién llamar
            when (_rolActual.value) {
                RolUsuario.PACIENTE -> {
                    val nuevoPaciente = Paciente(
                        cedula = datos.cedula, primerNombre = datos.primerNombre,
                        primerApellido = datos.primerApellido, correo = datos.correo,
                        fechaNacimiento = datos.fechaNacimiento
                        // ... mapear el resto de campos si es necesario
                    )
                    resultado = pacienteRepo.registrar(nuevoPaciente)
                }

                RolUsuario.DOCTOR -> {
                    val nuevoMedico = Medico(
                        cedula = datos.cedula, primerNombre = datos.primerNombre,
                        primerApellido = datos.primerApellido, correo = datos.correo,
                        licenciaMedica = datos.licenciaMedica, especialidad = datos.especialidad
                    )
                    resultado = medicoRepo.registrar(nuevoMedico)
                }

                RolUsuario.ADMINISTRADOR -> {
                    val nuevoAdmin = Administrador(
                        cedula = datos.cedula, primerNombre = datos.primerNombre,
                        primerApellido = datos.primerApellido, correo = datos.correo,
                        codigoEmpleado = datos.codigoEmpleado, departamento = datos.departamento
                    )
                    resultado = adminRepo.registrar(nuevoAdmin)
                }
            }

            _isLoading.value = false

            resultado.onSuccess {
                _mensaje.value = "¡Registro de ${_rolActual.value.name} Exitoso!"
            }.onFailure {
                _mensaje.value = "Error: ${it.message}"
            }
        }
    }
}