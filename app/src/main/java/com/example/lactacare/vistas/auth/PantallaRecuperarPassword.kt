package com.example.lactacare.vistas.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.MarkEmailRead
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lactacare.dominio.RolUsuario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRecuperarPassword(
    rolUsuario: RolUsuario,
    onVolver: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var emailEnviado by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }
    val isLoading by viewModel.isLoading.collectAsState()

    // Colores según rol
    val colorPrincipal = when (rolUsuario) {
        RolUsuario.ADMINISTRADOR -> Color(0xFFA3C9A8)
        RolUsuario.DOCTOR -> Color(0xFFB0C4DE)
        RolUsuario.PACIENTE -> Color(0xFFFFC0CB)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recuperar Contraseña") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF546E7A),
                    navigationIconContentColor = Color(0xFF546E7A)
                )
            )
        },
        containerColor = Color(0xFFFEFEFE)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (!emailEnviado) {
                // FORMULARIO PARA INGRESAR EMAIL
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = colorPrincipal,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¿Olvidaste tu contraseña?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF546E7A),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Ingresa tu correo electrónico y te enviaremos instrucciones para restablecer tu contraseña",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // CAMPO EMAIL
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        mensajeError = null
                    },
                    label = { Text("Correo Electrónico") },
                    placeholder = { Text("tu@email.com") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorPrincipal,
                        focusedLabelColor = colorPrincipal,
                        focusedLeadingIconColor = colorPrincipal,
                        cursorColor = colorPrincipal
                    ),
                    singleLine = true,
                    isError = mensajeError != null
                )

                if (mensajeError != null) {
                    Text(
                        text = mensajeError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // BOTÓN ENVIAR
                Button(
                    onClick = {
                        viewModel.enviarRecuperacionPassword(
                            email = email,
                            onSuccess = { emailEnviado = true },
                            onError = { error -> mensajeError = error }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorPrincipal,
                        contentColor = if (rolUsuario == RolUsuario.PACIENTE) Color(0xFF546E7A) else Color.White
                    ),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            "Enviar Instrucciones",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // BOTÓN VOLVER
                TextButton(
                    onClick = onVolver,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Volver al inicio de sesión",
                        color = colorPrincipal,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            } else {
                // PANTALLA DE CONFIRMACIÓN
                Icon(
                    imageVector = Icons.Outlined.MarkEmailRead,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(100.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¡Correo Enviado!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF546E7A),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Te hemos enviado un correo a:",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = email,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorPrincipal,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "📧 Revisa tu bandeja de entrada",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF546E7A)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "• Si no ves el correo, revisa tu carpeta de spam\n" +
                                    "• El enlace expirará en 24 horas\n" +
                                    "• Si no lo recibes, intenta nuevamente",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onVolver,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorPrincipal,
                        contentColor = if (rolUsuario == RolUsuario.PACIENTE) Color(0xFF546E7A) else Color.White
                    )
                ) {
                    Text(
                        "Volver al Inicio",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = { emailEnviado = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "Reenviar correo",
                        color = colorPrincipal,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
