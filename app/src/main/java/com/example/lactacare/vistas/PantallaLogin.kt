package com.example.lactacare.vistas

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

// Colores Base
val ColorTexto = Color(0xFF546E7A)
val ColorFondoToggle = Color(0xFFE0E0E0)
val ColorFondoApp = Color(0xFFFEFEFE)

@Composable
fun PantallaLogin(
    viewModel: LoginViewModel = viewModel(),
    onIrARegistro: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.mensajeError.collectAsState()
    val exito by viewModel.loginExitoso.collectAsState()

    // OBTENEMOS EL ROL ACTUAL DEL VIEWMODEL
    val rol by viewModel.rolActual.collectAsState()

    // --- LÓGICA DE ADAPTACIÓN ("El Camaleón") ---
    // Dependiendo del rol, elegimos colores, textos e iconos diferentes
    val (tituloBienvenida, iconoRol, colorTema, colorIcono) = when (rol) {
        RolUsuario.ADMINISTRADOR -> Quadruple(
            "Portal Administrativo",
            Icons.Default.Apartment,
            Color(0xFFA3C9A8), // Verde Admin
            Color(0xFF2E7D32)  // Verde oscuro para icono
        )
        RolUsuario.DOCTOR -> Quadruple(
            "Portal Médicos",
            Icons.Default.HealthAndSafety,
            Color(0xFFB0C4DE), // Azul Doctor
            Color(0xFF1565C0)  // Azul oscuro para icono
        )
        RolUsuario.PACIENTE -> Quadruple(
            "Bienvenido",
            Icons.Default.ChildCare,
            Color(0xFFA0D2DB), // Azul/Verde Usuario (original)
            Color(0xFFE65100)  // Naranja para icono
        )
    }

    // Estado local para ver password
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorFondoApp)
            .padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // 1. TÍTULO APP
            Text("LactaCare", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = ColorTexto)

            Spacer(modifier = Modifier.height(20.dp))

            // 2. ILUSTRACIÓN DINÁMICA
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(colorTema.copy(alpha = 0.3f), shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconoRol,
                    contentDescription = null,
                    modifier = Modifier.size(90.dp),
                    tint = colorIcono
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. TÍTULO DINÁMICO
            Text(tituloBienvenida, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = ColorTexto, textAlign = TextAlign.Center)

            Text(
                "Ingresa tus credenciales para continuar.",
                fontSize = 16.sp,
                color = ColorTexto.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4. TOGGLE SWITCH
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(ColorFondoToggle.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f).fillMaxHeight()
                            .background(Color.White, shape = RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Iniciar Sesión", fontWeight = FontWeight.Bold, color = ColorTexto)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f).fillMaxHeight()
                            .clickable { onIrARegistro() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Registrarse", fontWeight = FontWeight.Medium, color = ColorTexto.copy(alpha = 0.6f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. FORMULARIO
            CampoLogin(label = "Email", valor = email, onChange = { viewModel.onEmailChange(it) }, colorFocus = colorTema, esPassword = false)
            Spacer(modifier = Modifier.height(16.dp))

            // Password con icono de ojo
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Contraseña", fontWeight = FontWeight.SemiBold, color = ColorTexto, modifier = Modifier.padding(bottom = 8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    placeholder = { Text("••••••••", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorTema, // El borde cambia según el rol
                        unfocusedBorderColor = ColorFondoToggle,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(icon, contentDescription = null, tint = Color.Gray)
                        }
                    }
                )
            }

            Text(
                "¿Olvidaste tu contraseña?",
                color = colorTema, // Link del color del rol
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.End).padding(top = 8.dp).clickable { }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (error != null) Text(error!!, color = Color.Red, fontSize = 14.sp)
            if (exito) Text("¡Acceso Concedido!", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)

            // 6. BOTÓN LOGIN DINÁMICO
            Button(
                onClick = { viewModel.login() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorTema), // Botón del color del rol
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                else Text("Ingresar como $tituloBienvenida", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón Google y Footer (Simplificados para ahorrar espacio visual, código igual al anterior)
            OutlinedButton(onClick = {}, modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, ColorFondoToggle)) {
                Text("G ", color = Color.Red, fontWeight = FontWeight.Bold); Text("Continuar con Google", color = ColorTexto)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Clase auxiliar para guardar los 4 valores de configuración
data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun CampoLogin(label: String, valor: String, onChange: (String) -> Unit, colorFocus: Color, esPassword: Boolean) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontWeight = FontWeight.SemiBold, color = ColorTexto, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = valor, onValueChange = onChange,
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorFocus, unfocusedBorderColor = ColorFondoToggle, focusedContainerColor = Color.White, unfocusedContainerColor = Color.White),
            singleLine = true
        )
    }
}