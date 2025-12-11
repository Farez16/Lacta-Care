package com.example.lactacare.vistas.lactarios

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lactacare.dominio.Lactario
import com.example.lactacare.dominio.LactarioRepository
import com.example.lactacare.dominio.Reserva
import com.example.lactacare.dominio.ReservasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LactarioViewModel(
    private val lactarioRepository: LactarioRepository,
    private val reservaRepository: ReservasRepository
) : ViewModel() {

    // ESTADOS DE LA UI (Loading, Success, Error)
    private val _uiState = MutableStateFlow<LactarioUiState>(LactarioUiState.Loading)
    val uiState: StateFlow<LactarioUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = LactarioUiState.Loading
            try {
                // Pedimos los datos al repositorio (simula internet)
                val lactarios = lactarioRepository.obtenerLactarios()
                _uiState.value = LactarioUiState.Success(lactarios)
            } catch (e: Exception) {
                _uiState.value = LactarioUiState.Error("Error al cargar lactarios")
            }
        }
    }

    fun reservar(idLactario: Int, idPaciente: Int, fecha: String, hora: String) {
        viewModelScope.launch {
            try {
                val nuevaReserva = Reserva(
                    id = (0..1000).random(), // ID temporal
                    idLactario = idLactario,
                    idPaciente = idPaciente,
                    estado = "Pendiente",
                    fecha = fecha,
                    horaInicio = hora,
                    horaFin = hora // Simplificado por ahora
                )
                reservaRepository.crearReserva(nuevaReserva)
                // Aquí podrías actualizar el estado para mostrar un mensaje de éxito
                println("Reserva creada con éxito: $nuevaReserva")
            } catch (e: Exception) {
                println("Error al reservar: ${e.message}")
            }
        }
    }
}

// Definimos los estados posibles de esta pantalla
sealed class LactarioUiState {
    object Loading : LactarioUiState()
    data class Success(val lactarios: List<Lactario>) : LactarioUiState()
    data class Error(val mensaje: String) : LactarioUiState()
}