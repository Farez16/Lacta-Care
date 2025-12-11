package com.example.lactacare.vistas.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lactacare.dominio.RolUsuario

// --- COLORES ESTILO CLEAN ---
val TextoOscuroClean = Color(0xFF546E7A)
val BordeGrisClean = Color(0xFFE0E0E0)
val FondoBlancoClean = Color(0xFFFEFEFE)

@Composable
fun PantallaRegistroPersona(
    viewModel: RegistroPersonaViewModel = viewModel(),
    onIrALogin: () -> Unit
) {
    // Datos del ViewModel
    val datos by viewModel.uiState.collectAsState()
    val rol by viewModel.rolActual.collectAsState()
    val cargando by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // --- 1. CONFIGURACIÓN DE TEMA SEGÚN ROL ---
    data class TemaRegistro(
        val colorPrincipal: Color,
        val tituloRol: String,
        val icono: ImageVector
    )

    val tema = when (rol) {
        RolUsuario.ADMINISTRADOR -> TemaRegistro(
            colorPrincipal = Color(0xFFA3C9A8), // Verde Admin
            tituloRol = "Administrativo",
            icono = Icons.Default.Apartment
        )
        RolUsuario.DOCTOR -> TemaRegistro(
            colorPrincipal = Color(0xFFB0C4DE), // Azul Doctor
            tituloRol = "Médico",
            icono = Icons.Default.HealthAndSafety
        )
        RolUsuario.PACIENTE -> TemaRegistro(
            colorPrincipal = Color(0xFFFFC0CB), // Rosado Pastel (Paciente)
            tituloRol = "Paciente",
            icono = Icons.Default.ChildCare
        )
    }

    // --- 2. UI ESTILO CLEAN/HTML ---
    Scaffold(
        containerColor = FondoBlancoClean // Fondo BLANCO
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // [ELIMINADO] TÍTULO "LactaCare"

            // SUBTÍTULO
            Text(
                text = "Crear Cuenta",
                fontSize = 28.sp, // Aumentado un poco para compensar
                fontWeight = FontWeight.Bold,
                color = TextoOscuroClean
            )
            Text(
                text = "Registro de ${tema.tituloRol}",
                fontSize = 16.sp,
                color = tema.colorPrincipal, // Color dinámico
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // SWITCH (Registrarse Activo)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BordeGrisClean.copy(alpha = 0.5f))
                    .padding(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Iniciar Sesión (Gris)
                    Box(
                        modifier = Modifier
                            .weight(1f).fillMaxHeight()
                            .clickable { onIrALogin() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Iniciar Sesión", fontWeight = FontWeight.SemiBold, color = TextoOscuroClean.copy(alpha = 0.6f))
                    }
                    // Registrarse (Blanco con Sombra)
                    Box(
                        modifier = Modifier
                            .weight(1f).fillMaxHeight()
                            .shadow(2.dp, RoundedCornerShape(8.dp))
                            .background(Color.White, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Registrarse", fontWeight = FontWeight.Bold, color = TextoOscuroClean)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- 3. FORMULARIO COMPLETO ---

            // A. DATOS PERSONALES
            SeccionFormulario("Datos Personales", tema.colorPrincipal)

            // Nota: Pasamos 'tema.colorPrincipal' al colorFocus para pintar borde E ICONO
            CampoRegistroClean(label = "Cédula / DNI", valor = datos.cedula, placeholder = "110...", icon = Icons.Outlined.Badge, teclado = KeyboardType.Number, colorFocus = tema.colorPrincipal) { viewModel.onCedulaChange(it) }

            // Fila Nombres
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) {
                    CampoRegistroClean(label = "Primer Nombre", valor = datos.primerNombre, placeholder = "Juan", icon = Icons.Outlined.Person, colorFocus = tema.colorPrincipal) { viewModel.onPrimerNombreChange(it) }
                }
                Box(Modifier.weight(1f)) {
                    CampoRegistroClean(label = "Segundo Nombre", valor = datos.segundoNombre, placeholder = "Carlos", icon = Icons.Outlined.Person, colorFocus = tema.colorPrincipal) { viewModel.onSegundoNombreChange(it) }
                }
            }

            // Fila Apellidos
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(Modifier.weight(1f)) {
                    CampoRegistroClean(label = "Primer Apellido", valor = datos.primerApellido, placeholder = "Pérez", icon = Icons.Outlined.Person, colorFocus = tema.colorPrincipal) { viewModel.onPrimerApellidoChange(it) }
                }
                Box(Modifier.weight(1f)) {
                    CampoRegistroClean(label = "Segundo Apellido", valor = datos.segundoApellido, placeholder = "Gómez", icon = Icons.Outlined.Person, colorFocus = tema.colorPrincipal) { viewModel.onSegundoApellidoChange(it) }
                }
            }

            CampoRegistroClean(label = "Fecha Nacimiento", valor = datos.fechaNacimiento, placeholder = "DD/MM/AAAA", icon = Icons.Outlined.DateRange, colorFocus = tema.colorPrincipal) { viewModel.onFechaNacimientoChange(it) }

            // B. DATOS ESPECÍFICOS (Según Rol)
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = BordeGrisClean)
            Spacer(modifier = Modifier.height(16.dp))

            when (rol) {
                RolUsuario.DOCTOR -> {
                    SeccionFormulario("Credenciales Médicas", tema.colorPrincipal)
                    CampoRegistroClean(label = "Licencia Médica", valor = datos.licenciaMedica, placeholder = "Nro. Licencia", icon = Icons.Outlined.HealthAndSafety, colorFocus = tema.colorPrincipal) { viewModel.onLicenciaChange(it) }
                    CampoRegistroClean(label = "Especialidad", valor = datos.especialidad, placeholder = "Ej. Pediatría", icon = Icons.Outlined.WorkspacePremium, colorFocus = tema.colorPrincipal) { viewModel.onEspecialidadChange(it) }
                }
                RolUsuario.ADMINISTRADOR -> {
                    SeccionFormulario("Datos Corporativos", tema.colorPrincipal)
                    CampoRegistroClean(label = "Código Empleado", valor = datos.codigoEmpleado, placeholder = "ADM-001", icon = Icons.Outlined.Badge, colorFocus = tema.colorPrincipal) { viewModel.onCodigoEmpleadoChange(it) }
                    CampoRegistroClean(label = "Departamento", valor = datos.departamento, placeholder = "Recursos Humanos", icon = Icons.Outlined.Apartment, colorFocus = tema.colorPrincipal) { viewModel.onDepartamentoChange(it) }
                }
                RolUsuario.PACIENTE -> {
                    SeccionFormulario("Información Adicional", tema.colorPrincipal)
                    CampoRegistroClean(label = "Discapacidad (Opcional)", valor = datos.discapacidad, placeholder = "Detalle si aplica", icon = Icons.Outlined.Accessible, colorFocus = tema.colorPrincipal) { viewModel.onDiscapacidadChange(it) }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = BordeGrisClean)
            Spacer(modifier = Modifier.height(16.dp))

            // C. CUENTA
            SeccionFormulario("Cuenta", tema.colorPrincipal)

            CampoRegistroClean(label = "Correo Electrónico", valor = datos.correo, placeholder = "tu@email.com", icon = Icons.Outlined.Email, teclado = KeyboardType.Email, colorFocus = tema.colorPrincipal) { viewModel.onCorreoChange(it) }

            // Password
            Text(
                text = "Contraseña",
                fontWeight = FontWeight.SemiBold,
                color = TextoOscuroClean,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Mínimo 8 caracteres", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = inputColorsClean(tema.colorPrincipal),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                // ICONO DEL OJO TAMBIÉN COLOREADO
                trailingIcon = {
                    val imagen = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imagen, contentDescription = null, tint = tema.colorPrincipal)
                    }
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            // MENSAJES
            if (mensaje != null) {
                Text(
                    text = mensaje ?: "",
                    color = if (mensaje!!.contains("Exitoso", ignoreCase = true)) tema.colorPrincipal else Color.Red,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            // BOTÓN CONFIRMAR
            Button(
                onClick = { viewModel.registrar() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = tema.colorPrincipal,
                    contentColor = if (rol == RolUsuario.PACIENTE) TextoOscuroClean else Color.White // Contraste texto
                ),
                enabled = !cargando
            ) {
                if (cargando) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Confirmar Registro", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // FOOTER LEGAL
            val textoLegal = buildAnnotatedString {
                append("Al registrarte aceptas los ")
                withStyle(SpanStyle(color = tema.colorPrincipal, fontWeight = FontWeight.Bold)) { append("Términos") }
                append(" y ")
                withStyle(SpanStyle(color = tema.colorPrincipal, fontWeight = FontWeight.Bold)) { append("Política de Privacidad") }
            }
            Text(text = textoLegal, fontSize = 11.sp, color = TextoOscuroClean.copy(alpha = 0.7f), textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// --- COMPONENTES AUXILIARES ---

@Composable
fun SeccionFormulario(titulo: String, color: Color) {
    Text(
        text = titulo,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
    )
}

@Composable
fun CampoRegistroClean(
    label: String,
    valor: String,
    placeholder: String,
    icon: ImageVector,
    teclado: KeyboardType = KeyboardType.Text,
    colorFocus: Color,
    onCambio: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = TextoOscuroClean,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = valor,
            onValueChange = onCambio,
            placeholder = { Text(placeholder, color = TextoOscuroClean.copy(alpha = 0.4f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = inputColorsClean(colorFocus),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = teclado),
            // ICONO COLOREADO
            trailingIcon = { Icon(icon, contentDescription = null, tint = colorFocus) }
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun inputColorsClean(colorFocus: Color) = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    focusedBorderColor = colorFocus, // Borde de color al enfocar
    unfocusedBorderColor = BordeGrisClean,
    cursorColor = TextoOscuroClean,
    focusedTextColor = TextoOscuroClean,
    unfocusedTextColor = TextoOscuroClean
)