package com.example.lactacare.vistas

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lactacare.dominio.RolUsuario
import com.example.lactacare.theme.*

@Composable
fun PantallaHome(
    viewModel: HomeViewModel = viewModel(),
    rolUsuario: RolUsuario,
    onLogout: () -> Unit,
    // --- NUEVOS PARÁMETROS REQUERIDOS ---
    // Estos son los que MainActivity está intentando enviar
    onNavReservas: () -> Unit = {},
    onNavBebe: () -> Unit = {},
    onNavInfo: (String) -> Unit = {}
) {
    LaunchedEffect(rolUsuario) {
        viewModel.setRol(rolUsuario)
    }

    val seccionActual by viewModel.seccionActual.collectAsState()

    // Configuración de colores dinámica
    val (colorPrincipal, colorAcento) = when (rolUsuario) {
        RolUsuario.PACIENTE -> Pair(MomPrimary, MomAccent)
        RolUsuario.DOCTOR -> Pair(DoctorPrimary, DoctorBackground)
        RolUsuario.ADMINISTRADOR -> Pair(AdminPrimary, AdminBackground)
    }

    val saludo = when (rolUsuario) {
        RolUsuario.PACIENTE -> "Buenos días, Sarah"
        RolUsuario.DOCTOR -> "Dr. Especialista"
        RolUsuario.ADMINISTRADOR -> "Admin Sistema"
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
            // 1. TOP BAR COMÚN
            TopBarHome(saludo = saludo, colorIcono = TextoOscuroClean)

            // 2. CONTENIDO DINÁMICO
            Box(modifier = Modifier.weight(1f)) {
                when (seccionActual.id) {
                    "inicio" -> {
                        when (rolUsuario) {
                            RolUsuario.PACIENTE -> DashboardPaciente(
                                colorPrimary = colorPrincipal,
                                colorAccent = colorAcento,
                                // Pasamos las funciones de navegación al Dashboard
                                onNavReservas = onNavReservas,
                                onNavBebe = onNavBebe,
                                onNavInfo = onNavInfo
                            )
                            RolUsuario.DOCTOR -> DashboardDoctor(colorPrincipal, colorAcento)
                            RolUsuario.ADMINISTRADOR -> DashboardAdmin(colorPrincipal, colorAcento)
                        }
                    }
                    else -> {
                        PantallaEnConstruccion(seccionActual.titulo)
                    }
                }
            }
        }
    }
}