package com.example.lactacare.vistas.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lactacare.datos.MockAdminRepository
import com.example.lactacare.datos.MockMedicoRepository
import com.example.lactacare.datos.MockPacienteRepository
import com.example.lactacare.dominio.RolUsuario
import com.example.lactacare.theme.*
import com.example.lactacare.vistas.auth.BordeGrisClean
import com.example.lactacare.vistas.auth.TextoOscuroClean


@Composable
fun PantallaPerfil(
    rolUsuario: RolUsuario,
    onLogout: () -> Unit,
    viewModel: PerfilViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PerfilViewModel(
                    pacienteRepo = MockPacienteRepository(),
                    medicoRepo = MockMedicoRepository(),
                    adminRepo = MockAdminRepository()
                ) as T
            }
        }
    )
) {
    // 1. Lógica de Colores Dinámicos según Rol
    val (colorPrincipal, colorFondoSuave) = when (rolUsuario) {
        RolUsuario.PACIENTE -> Pair(MomPrimary, MomAccent)       // Rosa
        RolUsuario.DOCTOR -> Pair(DoctorPrimary, DoctorBackground) // Azul
        RolUsuario.ADMINISTRADOR -> Pair(AdminPrimary, AdminBackground) // Verde
    }

    // 2. Cargar datos
    LaunchedEffect(rolUsuario) {
        viewModel.cargarPerfil(rolUsuario)
    }

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {
        // --- CABECERA PERSONALIZADA ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                // Foto Perfil
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(colorFondoSuave, CircleShape) // Fondo dinámico
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = colorPrincipal, modifier = Modifier.size(60.dp))
                }

                // Botón Editar (Con color del rol)
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(32.dp)
                        .background(colorPrincipal, CircleShape) // Botón dinámico
                        .padding(4.dp)
                ) {
                    Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            Spacer(Modifier.height(16.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = colorPrincipal)
            } else {
                Text(uiState.nombreCompleto, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextoOscuroClean)
                Text(uiState.correo, fontSize = 14.sp, color = Color.Gray)

                // Badge del Rol
                Spacer(Modifier.height(8.dp))
                Surface(
                    color = colorFondoSuave,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = rolUsuario.name,
                        color = colorPrincipal,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // --- DETALLES (Iconos con color del rol) ---
        SeccionPerfil("DETALLES DEL PERFIL")
        CardOpciones {
            if (!uiState.isLoading) {
                uiState.detalles.entries.forEachIndexed { index, entry ->
                    val icono = when(entry.key) {
                        "Teléfono" -> Icons.Outlined.Phone
                        "Cédula" -> Icons.Outlined.Badge
                        "Departamento" -> Icons.Outlined.Business
                        "Código Empleado" -> Icons.Outlined.QrCode
                        "Especialidad" -> Icons.Outlined.MedicalServices
                        else -> Icons.Outlined.Info
                    }

                    // Pasamos el color principal al Item
                    ItemPerfil(icono, entry.key, entry.value, colorPrincipal)

                    if (index < uiState.detalles.size - 1) {
                        Divider(color = BordeGrisClean.copy(alpha = 0.5f))
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // --- GESTIÓN (Iconos con color del rol) ---
        SeccionPerfil("GESTIÓN DE CUENTA")
        CardOpciones {
            ItemPerfil(Icons.Outlined.Security, "Privacidad", "", colorPrincipal)
            Divider(color = BordeGrisClean.copy(alpha = 0.5f))
            ItemPerfil(Icons.Outlined.HelpOutline, "Ayuda y Soporte", "", colorPrincipal)
            Divider(color = BordeGrisClean.copy(alpha = 0.5f))

            // Logout (Siempre rojo por convención de UI)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLogout() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFFEBEE), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Logout, null, tint = Color.Red)
                }
                Spacer(Modifier.width(16.dp))
                Text("Cerrar Sesión", modifier = Modifier.weight(1f), fontSize = 16.sp, color = Color.Red, fontWeight = FontWeight.Medium)
                Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}

// --- COMPONENTES AUXILIARES ADAPTADOS ---

@Composable
fun SeccionPerfil(titulo: String) {
    Text(
        text = titulo,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@Composable
fun CardOpciones(content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        content()
    }
}

@Composable
fun ItemPerfil(
    icon: ImageVector,
    titulo: String,
    subtitulo: String,
    colorTema: Color // <-- Nuevo parámetro para recibir el color del rol
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                // Usamos el color del tema con transparencia
                .background(colorTema.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Icono del color del tema
            Icon(icon, null, tint = colorTema)
        }
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, fontSize = 12.sp, color = Color.Gray)
            if (subtitulo.isNotEmpty()) {
                Text(subtitulo, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = TextoOscuroClean)
            }
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
    }
}