package com.example.lactacare.vistas.lactarios

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lactacare.datos.MockLactarioRepository
import com.example.lactacare.datos.MockReservasRepository

@Composable
fun PantallaLactario(
    onVolver: () -> Unit,
    // Aquí inicializamos el ViewModel con los Mocks
    viewModel: LactarioViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LactarioViewModel(
                    lactarioRepository = MockLactarioRepository(),
                    reservaRepository = MockReservasRepository()
                ) as T
            }
        }
    )
) {
    // Llamamos a la UI real pasándole el viewModel ya configurado
    PantallaReservas(
        onVolver = onVolver,
        viewModel = viewModel
    )
}