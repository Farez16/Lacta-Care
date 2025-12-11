package com.example.lactacare.vistas.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lactacare.datos.MockAdminRepository
import com.example.lactacare.datos.MockMedicoRepository
import com.example.lactacare.datos.MockPacienteRepository
import com.example.lactacare.dominio.RolUsuario
import com.example.lactacare.theme.*
import com.example.lactacare.vistas.chat.PantallaChat
import com.example.lactacare.vistas.inventario.PantallaInventario
import com.example.lactacare.vistas.perfil.PantallaPerfil // Asegúrate de importar esto

@Composable
fun PantallaHome(
    rolUsuario: RolUsuario, // <--- Este dato lo usaremos abajo
    onLogout: () -> Unit,
    onNavReservas: () -> Unit = {},
    onNavBebe: () -> Unit = {},
    onNavInfo: (String) -> Unit = {},

    viewModel: HomeViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    pacienteRepo = MockPacienteRepository(),
                    medicoRepo = MockMedicoRepository(),
                    adminRepo = MockAdminRepository()
                ) as T
            }
        }
    )
) {
    LaunchedEffect(rolUsuario) {
        viewModel.setRol(rolUsuario)
    }

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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // CABECERA: Solo se muestra en "Inicio"
            if (seccionActual.id == "inicio") {
                TopBarHome(
                    saludo = uiState.nombreUsuario,
                    colorIcono = TextoOscuroClean
                )
            }

            Box(modifier = Modifier.weight(1f)) {
                when (seccionActual.id) {
                    "inicio" -> {
                        when (rolUsuario) {
                            RolUsuario.PACIENTE -> DashboardPaciente(
                                colorPrimary = colorPrincipal,
                                colorAccent = colorAcento,
                                onNavReservas = onNavReservas,
                                onNavBebe = onNavBebe,
                                onNavInfo = onNavInfo
                            )
                            RolUsuario.DOCTOR -> DashboardDoctor(colorPrincipal, colorAcento)
                            RolUsuario.ADMINISTRADOR -> DashboardAdmin(colorPrincipal, colorAcento)
                        }
                    }

                    // --- SECCIONES SECUNDARIAS ---
                    "chatbot" -> PantallaChat()
                    "registros" -> PantallaInventario()

                    // --- AQUÍ ESTABA EL ERROR ---
                    "perfil" -> PantallaPerfil(
                        rolUsuario = rolUsuario, // <--- ¡AHORA SÍ LE PASAMOS EL ROL!
                        onLogout = onLogout
                    )

                    else -> Box(modifier = Modifier.fillMaxSize()) { Text("En construcción") }
                }
            }
        }
    }
}