package com.example.lactacare.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lactacare.dominio.RolUsuario

// --- COLORES GENERALES ---
val AzulFondo = Color(0xFFEBF5FB)
val AzulTitulo = Color(0xFF1A4356)
val GrisTexto = Color(0xFF757575)
val BordeInput = Color(0xFFE0E0E0)

@Composable
fun PantallaRegistroPersona(
    viewModel: RegistroPersonaViewModel = viewModel(),
    onIrALogin: () -> Unit
) {
    // Conectamos con los datos del ViewModel
    val datos by viewModel.uiState.collectAsState()
    val rol by viewModel.rolActual.collectAsState() // ¡Importante! Saber quién es
    val cargando by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // --- LÓGICA DE APARIENCIA SEGÚN ROL ---
    val colorTema = when (rol) {
        RolUsuario.ADMINISTRADOR -> Color(0xFFA3C9A8) // Verde Admin
        RolUsuario.DOCTOR -> Color(0xFFB0C4DE)        // Azul Doctor
        RolUsuario.PACIENTE -> Color(0xFF90D8B9)      // Verde Menta Paciente
    }

    val tituloRol = when (rol) {
        RolUsuario.ADMINISTRADOR -> "Administrativo"
        RolUsuario.DOCTOR -> "Médico"
        RolUsuario.PACIENTE -> "Paciente"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulFondo)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(30.dp))

            // 1. HEADER DINÁMICO
            Text("LactaCare", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = AzulTitulo)
            Text("Registro de $tituloRol", fontSize = 16.sp, color = GrisTexto, modifier = Modifier.padding(bottom = 24.dp))

            // 2. TARJETA FORMULARIO
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Datos Personales",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulTitulo,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 16.dp)
                    )

                    // --- CAMPOS COMUNES ---
                    CampoRegistro(label = "Cédula / DNI", valor = datos.cedula, placeholder = "Ej. 1104567890", icon = Icons.Outlined.Badge, teclado = KeyboardType.Number, colorFocus = colorTema) { viewModel.onCedulaChange(it) }

                    // ... (Cédula arriba) ...

// --- FILA DE NOMBRES (2 en 1) ---
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Campo: Primer Nombre (50% del ancho)
                        Box(modifier = Modifier.weight(1f)) {
                            CampoRegistro(
                                label = "Primer Nombre",
                                valor = datos.primerNombre,
                                placeholder = "Juan",
                                icon = Icons.Outlined.Person,
                                colorFocus = colorTema
                            ) { viewModel.onPrimerNombreChange(it) } // <--- Se guarda en su variable única
                        }

                        Spacer(modifier = Modifier.width(8.dp)) // Espacio en medio

                        // Campo: Segundo Nombre (50% del ancho)
                        Box(modifier = Modifier.weight(1f)) {
                            CampoRegistro(
                                label = "Segundo Nombre",
                                valor = datos.segundoNombre,
                                placeholder = "Carlos", // Opcional
                                icon = Icons.Outlined.Person,
                                colorFocus = colorTema
                            ) { viewModel.onSegundoNombreChange(it) } // <--- Se guarda en su variable única
                        }
                    }

// --- FILA DE APELLIDOS (2 en 1) ---
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Campo: Primer Apellido
                        Box(modifier = Modifier.weight(1f)) {
                            CampoRegistro(
                                label = "Primer Apellido",
                                valor = datos.primerApellido,
                                placeholder = "Pérez",
                                icon = Icons.Outlined.Person,
                                colorFocus = colorTema
                            ) { viewModel.onPrimerApellidoChange(it) }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Campo: Segundo Apellido
                        Box(modifier = Modifier.weight(1f)) {
                            CampoRegistro(
                                label = "Segundo Apellido",
                                valor = datos.segundoApellido,
                                placeholder = "Gómez",
                                icon = Icons.Outlined.Person,
                                colorFocus = colorTema
                            ) { viewModel.onSegundoApellidoChange(it) }
                        }
                    }

// ... (Resto de campos abajo) ...
                    Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))

                    // --- CAMPOS ESPECÍFICOS (AQUÍ ESTÁ LA MAGIA) ---
                    when (rol) {
                        RolUsuario.DOCTOR -> {
                            Text("Credenciales Médicas", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colorTema, modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp))

                            CampoRegistro(label = "Licencia Médica", valor = datos.licenciaMedica, placeholder = "Nro. Licencia / CMP", icon = Icons.Outlined.HealthAndSafety, colorFocus = colorTema) { viewModel.onLicenciaChange(it) }
                            CampoRegistro(label = "Especialidad", valor = datos.especialidad, placeholder = "Ej. Pediatría", icon = Icons.Outlined.WorkspacePremium, colorFocus = colorTema) { viewModel.onEspecialidadChange(it) }

                            Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        }

                        RolUsuario.ADMINISTRADOR -> {
                            Text("Datos Corporativos", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colorTema, modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp))

                            CampoRegistro(label = "Código Empleado", valor = datos.codigoEmpleado, placeholder = "Ej. ADM-001", icon = Icons.Outlined.Badge, colorFocus = colorTema) { viewModel.onCodigoEmpleadoChange(it) }
                            CampoRegistro(label = "Departamento", valor = datos.departamento, placeholder = "Ej. Recursos Humanos", icon = Icons.Outlined.Apartment, colorFocus = colorTema) { viewModel.onDepartamentoChange(it) }

                            Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        }

                        RolUsuario.PACIENTE -> {
                            // Solo el paciente ve la fecha de nacimiento y discapacidad en el registro inicial
                            CampoRegistro(label = "Fecha Nacimiento", valor = datos.fechaNacimiento, placeholder = "DD/MM/AAAA", icon = Icons.Outlined.DateRange, colorFocus = colorTema) { viewModel.onFechaNacimientoChange(it) }
                            CampoRegistro(label = "Discapacidad (Opcional)", valor = datos.discapacidad, placeholder = "Detalle si aplica", icon = Icons.Outlined.Accessible, colorFocus = colorTema) { viewModel.onDiscapacidadChange(it) }

                            Divider(modifier = Modifier.padding(vertical = 16.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }

                    // --- DATOS DE CUENTA ---
                    Text("Cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AzulTitulo, modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp))

                    CampoRegistro(label = "Correo Electrónico", valor = datos.correo, placeholder = "ejemplo@correo.com", icon = Icons.Outlined.Email, teclado = KeyboardType.Email, colorFocus = colorTema) { viewModel.onCorreoChange(it) }

                    // Contraseña
                    LabelPersonalizado("Contraseña")
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Mínimo 8 caracteres", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = inputColors(colorTema), // Usamos el color del tema
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        trailingIcon = {
                            val imagen = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(imagen, contentDescription = null, tint = Color(0xFF90CAE8))
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Mensaje de éxito/error
                    if (mensaje != null) {
                        Text(
                            text = mensaje ?: "",
                            color = if (mensaje!!.contains("Exitoso")) colorTema else Color.Red,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    // BOTÓN REGISTRAR (Color Dinámico)
                    Button(
                        onClick = { viewModel.registrar() },
                        colors = ButtonDefaults.buttonColors(containerColor = colorTema),
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !cargando
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Registrar $tituloRol", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val textoLegal = buildAnnotatedString {
                        append("Al registrarte aceptas los ")
                        withStyle(style = SpanStyle(color = Color(0xFF81D4FA))) { append("Términos") }
                        append(" y ")
                        withStyle(style = SpanStyle(color = Color(0xFF81D4FA))) { append("Política de Privacidad") }
                    }
                    Text(text = textoLegal, fontSize = 11.sp, color = GrisTexto, textAlign = TextAlign.Center)
                }
            }

            // LOGIN LINK
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.padding(bottom = 30.dp)) {
                Text("¿Ya tienes una cuenta? ", color = GrisTexto)
                Text(
                    "Inicia sesión",
                    color = colorTema, // Link del mismo color que el tema
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onIrALogin() }
                )
            }
        }
    }
}

// --- COMPONENTES AUXILIARES ---

@Composable
fun CampoRegistro(
    label: String,
    valor: String,
    placeholder: String,
    icon: ImageVector,
    teclado: KeyboardType = KeyboardType.Text,
    colorFocus: Color, // Nuevo parámetro para el color
    onCambio: (String) -> Unit
) {
    LabelPersonalizado(label)
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        placeholder = { Text(placeholder, color = Color.LightGray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = inputColors(colorFocus), // Pasamos el color
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        trailingIcon = { Icon(icon, contentDescription = null, tint = Color(0xFF90CAE8)) }
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun LabelPersonalizado(texto: String) {
    Text(
        text = texto,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF424242),
        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
        textAlign = TextAlign.Start
    )
}

// Modificamos para recibir el color dinámico
@Composable
fun inputColors(colorBorde: Color) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = colorBorde,
    unfocusedBorderColor = BordeInput,
    focusedContainerColor = Color.White,
    unfocusedContainerColor = Color.White,
    cursorColor = colorBorde,
    focusedTextColor = Color.Black,
    unfocusedTextColor = Color.Black
)

@Preview(showSystemUi = true)
@Composable
fun PrevisualizacionRegistro() {
    PantallaRegistroPersona(onIrALogin = {})
}