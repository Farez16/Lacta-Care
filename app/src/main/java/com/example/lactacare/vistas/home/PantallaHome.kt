package com.example.lactacare.vistas.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lactacare.datos.*
import com.example.lactacare.dominio.RolUsuario
import com.example.lactacare.theme.*
import com.example.lactacare.vistas.chat.PantallaChat
import com.example.lactacare.vistas.inventario.PantallaInventario
import com.example.lactacare.vistas.perfil.PantallaPerfil

@Composable
fun PantallaHome(
    rolUsuario: RolUsuario,
    onLogout: () -> Unit,
    // Recibimos las acciones de navegación desde el Main
    onNavegarANuevaReserva: () -> Unit = {},
    onNavegarADetalleReserva: () -> Unit = {},
    onNavBebe: () -> Unit = {},
    onNavInfo: (String) -> Unit = {},

    viewModel: HomeViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    pacienteRepo = MockPacienteRepository(),
                    medicoRepo = MockMedicoRepository(),
                    adminRepo = MockAdminRepository(),
                    reservaRepo = MockReservasRepository(),
                    bebeRepo = MockBebeRepository()
                ) as T
            }
        }
    )
) {
    LaunchedEffect(rolUsuario) { viewModel.setRol(rolUsuario) }

    val seccionActual by viewModel.seccionActual.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val (colorPrincipal, colorAcento) = when (rolUsuario) {
        RolUsuario.PACIENTE -> Pair(MomPrimary, MomAccent)
        RolUsuario.DOCTOR -> Pair(DoctorPrimary, DoctorBackground)
        RolUsuario.ADMINISTRADOR -> Pair(AdminPrimary, AdminBackground)
    }

    val menuItems = viewModel.obtenerMenuPorRol(rolUsuario)

    Scaffold(
        containerColor = Color(0xFFFAFAFA),
        bottomBar = {
            BottomNavBarFlotante(
                items = menuItems,
                seccionActual = seccionActual,
                colorActivo = colorPrincipal,
                onItemClick = { viewModel.seleccionarSeccion(it) }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            if (seccionActual.id == "inicio") {
                TopBarHome(saludo = uiState.nombreUsuario, colorIcono = TextoOscuroClean)
            }

            Box(modifier = Modifier.weight(1f)) {
                when (seccionActual.id) {
                    "inicio" -> {
                        when (rolUsuario) {
                            RolUsuario.PACIENTE -> DashboardPaciente(
                                colorPrimary = colorPrincipal,
                                colorAccent = colorAcento,
                                nombreUsuario = uiState.nombreUsuario,
                                proximaCita = uiState.proximaReserva,
                                nombreBebe = uiState.nombreBebe,

                                // --- CORRECCIÓN AQUÍ ---
                                // El Dashboard espera 'onNavReservas', así que le asignamos la función que viene del Main
                                onNavReservas = onNavegarANuevaReserva,

                                onNavBebe = onNavBebe,
                                onNavInfo = onNavInfo
                            )
                            RolUsuario.DOCTOR -> DashboardDoctor(colorPrincipal, colorAcento)
                            RolUsuario.ADMINISTRADOR -> DashboardAdmin(colorPrincipal, colorAcento)
                        }
                    }
                    "chatbot" -> PantallaChat()
                    "registros" -> PantallaInventario()
                    "perfil" -> PantallaPerfil(rolUsuario = rolUsuario, onLogout = onLogout)
                    else -> Box(modifier = Modifier.fillMaxSize()) { Text("En construcción") }
                }
            }
        }
    }
}