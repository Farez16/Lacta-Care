package com.example.lactacare.vistas.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lactacare.dominio.RolUsuario

// COLORES EXTRAÍDOS DE TU HTML/TAILWIND
val SlateGray = Color(0xFF546E7A)      // "text-dark-slate-gray"
val LightGray = Color(0xFFE0E0E0)      // "bg-[#E0E0E0]/50" y bordes
val OffWhite = Color(0xFFFEFEFE)       // "bg-off-white"
val White = Color.White

@Composable
fun PantallaLogin(
    viewModel: LoginViewModel = viewModel(),
    onIrARegistro: (RolUsuario) -> Unit
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.mensajeError.collectAsState()
    val exito by viewModel.loginExitoso.collectAsState()
    val rol by viewModel.rolActual.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }

    // --- LÓGICA DE COLORES (INTACTA) ---
    data class TemaRol(
        val titulo: String,
        val icono: ImageVector,
        val colorPrincipal: Color,
        val colorAcento: Color,
        val colorIcono: Color,
        val colorFondoPantalla: Color = OffWhite // Usamos el off-white del HTML por defecto
    )

    val tema = when (rol) {
        RolUsuario.ADMINISTRADOR -> TemaRol(
            titulo = "Portal Administrativo",
            icono = Icons.Default.Apartment,
            colorPrincipal = Color(0xFFA3C9A8), // Verde Admin
            colorAcento = Color(0xFFA3C9A8).copy(alpha = 0.3f),
            colorIcono = Color(0xFF2E7D32)
        )
        RolUsuario.DOCTOR -> TemaRol(
            titulo = "Portal Médicos",
            icono = Icons.Default.HealthAndSafety,
            colorPrincipal = Color(0xFFB0C4DE), // Azul Doctor
            colorAcento = Color(0xFFB0C4DE).copy(alpha = 0.3f),
            colorIcono = Color(0xFF1565C0)
        )
        RolUsuario.PACIENTE -> TemaRol(
            titulo = "Bienvenido", // Texto exacto del HTML
            icono = Icons.Default.ChildCare,
            colorPrincipal = Color(0xFFFFC0CB), // Rosado (Usuario)
            colorAcento = Color(0xFFFFDDE2),
            colorIcono = SlateGray
        )
    }

    // --- UI: CLONANDO EL HTML ---
    Scaffold(
        containerColor = tema.colorFondoPantalla
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp) // px-4 del HTML
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp)) // pt-8

            // 1. HEADER: "LactaCare" (text-4xl font-bold)
            Text(
                text = "LactaCare",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = SlateGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 2. IMAGEN (Square aspect ratio)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 8.dp)
                    .aspectRatio(1f) // Cuadrado perfecto como en el HTML
                    .background(tema.colorAcento, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Aquí iría tu imagen real. Usamos el icono como placeholder visual.
                Icon(
                    imageVector = tema.icono,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp),
                    tint = tema.colorIcono
                )
            }

            // 3. TÍTULOS (text-[32px] font-bold)
            Text(
                text = tema.titulo,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = SlateGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 0.dp, bottom = 8.dp)
            )

            Text(
                text = "Cuidado inteligente, lactancia tranquila.",
                fontSize = 16.sp,
                color = SlateGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 4. TOGGLE SWITCH (Estilo HTML: Fondo Gris, Botón Blanco)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // h-12 (approx 48-56dp)
                    .clip(RoundedCornerShape(8.dp)) // rounded-lg
                    .background(LightGray.copy(alpha = 0.5f)) // bg-[#E0E0E0]/50
                    .padding(4.dp) // p-1
            ) {
                Row(modifier = Modifier.fillMaxSize()) {
                    // Botón Activo (Login) -> Blanco con sombra
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .shadow(1.dp, RoundedCornerShape(8.dp))
                            .background(White, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Iniciar Sesión", fontWeight = FontWeight.SemiBold, color = SlateGray)
                    }
                    // Botón Inactivo (Registro) -> Transparente
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onIrARegistro(rol) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Registrarse", fontWeight = FontWeight.SemiBold, color = SlateGray.copy(alpha = 0.7f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. INPUTS (Estilo HTML: Label fuera, Input blanco con borde)

            // Email
            CampoLoginHtml(
                label = "Email",
                valor = email,
                placeholder = "tu.email@ejemplo.com",
                onChange = { viewModel.onEmailChange(it) },
                colorFocus = tema.colorPrincipal
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Contraseña",
                    fontWeight = FontWeight.SemiBold,
                    color = SlateGray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.onPasswordChange(it) },
                    placeholder = { Text("Tu contraseña", color = SlateGray.copy(alpha = 0.5f)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp), // rounded-lg
                    colors = inputHtmlColors(tema.colorPrincipal),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(icon, contentDescription = null, tint = SlateGray.copy(alpha = 0.5f))
                        }
                    }
                )
            }

            // Enlace Olvidó Contraseña
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    text = "¿Olvidaste tu contraseña?",
                    color = tema.colorPrincipal, // text-soft-blue (dinámico)
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mensajes de error/exito
            if (error != null) Text(error!!, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
            if (exito) Text("¡Acceso Concedido!", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

            // 6. BOTÓN PRINCIPAL (h-14, rounded-lg)
            Button(
                onClick = { viewModel.login() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // h-14
                shape = RoundedCornerShape(8.dp), // rounded-lg
                colors = ButtonDefaults.buttonColors(
                    containerColor = tema.colorPrincipal, // bg-soft-blue (dinámico)
                    contentColor = White
                ),
                enabled = !isLoading
            ) {
                if (isLoading) CircularProgressIndicator(color = White, modifier = Modifier.size(24.dp))
                else Text("Iniciar Sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 7. DIVIDER CON "O"
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = LightGray)
                Text(
                    text = "O",
                    fontSize = 14.sp,
                    color = SlateGray.copy(alpha = 0.7f),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = LightGray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 8. BOTÓN GOOGLE (Blanco, Borde Gris)
            OutlinedButton(
                onClick = { /* Google Login */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, LightGray),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = White)
            ) {
                // Simulamos el icono de Google con texto coloreado por simplicidad
                Text("G ", color = Color.Red, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Continuar con Google", color = SlateGray, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 9. FOOTER
            val footerText = buildAnnotatedString {
                append("Al continuar, aceptas nuestra ")
                withStyle(SpanStyle(color = tema.colorPrincipal, fontWeight = FontWeight.SemiBold)) {
                    append("Política de Privacidad")
                }
                append(" y ")
                withStyle(SpanStyle(color = tema.colorPrincipal, fontWeight = FontWeight.SemiBold)) {
                    append("Términos de Servicio")
                }
                append(".")
            }
            Text(
                text = footerText,
                fontSize = 12.sp,
                color = SlateGray.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// --- COMPONENTES AUXILIARES PARA ESTILO HTML ---

@Composable
fun CampoLoginHtml(
    label: String,
    valor: String,
    placeholder: String,
    onChange: (String) -> Unit,
    colorFocus: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Label Arriba y en Negrita (font-semibold)
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = SlateGray,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Input Blanco con Borde
        OutlinedTextField(
            value = valor,
            onValueChange = onChange,
            placeholder = { Text(placeholder, color = SlateGray.copy(alpha = 0.5f)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp), // rounded-lg
            colors = inputHtmlColors(colorFocus),
            singleLine = true
        )
    }
}

@Composable
fun inputHtmlColors(colorFocus: Color) = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = White,
    unfocusedContainerColor = White,
    focusedBorderColor = colorFocus.copy(alpha = 0.5f), // focus:ring-soft-blue/50
    unfocusedBorderColor = LightGray,
    cursorColor = SlateGray,
    focusedTextColor = SlateGray,
    unfocusedTextColor = SlateGray
)