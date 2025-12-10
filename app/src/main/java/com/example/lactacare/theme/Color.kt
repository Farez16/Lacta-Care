package com.example.lactacare.theme

import androidx.compose.ui.graphics.Color

// --- 1. PALETAS POR ROL (Usadas en Home, Login y Registro) ---

// PACIENTE (MAMÁ) - Estilo Tailwind solicitado
val MomPrimary = Color(0xFFFFC0CB)       // #FFC0CB (Rosado Pastel)
val MomAccent = Color(0xFFFFDDE2)        // #FFDDE2 (Rosado Muy Claro)
val MomText = Color(0xFF545454)          // #545454 (Gris Oscuro)
val MomBackground = Color(0xFFFFF9FB)    // #FFF9FB (Blanco Rosado)

// DOCTOR (MÉDICO) - Azul Clínico
val DoctorPrimary = Color(0xFF42A5F5)    // Azul Botón
val DoctorText = Color(0xFF0D47A1)       // Azul Texto Fuerte
val DoctorBackground = Color(0xFFE3F2FD) // Azul Fondo Suave

// ADMINISTRADOR - Verde Gestión
val AdminPrimary = Color(0xFF66BB6A)     // Verde Botón
val AdminText = Color(0xFF1B5E20)        // Verde Texto Fuerte
val AdminBackground = Color(0xFFE8F5E9)  // Verde Fondo Suave

// --- 2. NEUTROS Y ESTILO "CLEAN" ---
val TextoOscuroClean = Color(0xFF546E7A)
val BordeGrisClean = Color(0xFFE0E0E0)
val FondoBlancoClean = Color(0xFFFEFEFE)
val FondoSwitch = Color(0xFFF5F5F5)

// --- 3. ALIAS DE COMPATIBILIDAD ---
// Estos alias aseguran que el código de Login (que usaba nombres en inglés) siga funcionando sin cambios
val SlateGray = TextoOscuroClean
val LightGray = BordeGrisClean
val OffWhite = FondoBlancoClean
val White = Color.White