package com.example.lactacare.vistas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lactacare.theme.TextoOscuroClean

@Composable
fun DashboardPaciente(
    colorPrimary: Color,
    colorAccent: Color,
    onNavReservas: () -> Unit,
    onNavBebe: () -> Unit,
    onNavInfo: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // 1. FONDO DECORATIVO (LIRIOS LINE ART)
        // Dibujamos un diseño de líneas finas más elegante
        FondoFloralLirio(color = colorPrimary.copy(alpha = 0.4f)) // Alpha un poco mayor porque son líneas finas

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // --- TARJETA PRINCIPAL (LACTARIO) ---
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text("Lactario Principal", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextoOscuroClean.copy(alpha = 0.5f))
                            Box(modifier = Modifier.size(8.dp).background(Color(0xFF4CAF50), CircleShape))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(56.dp).background(colorPrimary.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.CheckCircle, null, tint = colorPrimary, modifier = Modifier.size(28.dp))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("Sala 3A", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = TextoOscuroClean)
                                Text("Disponible ahora", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF4CAF50))
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onNavReservas,
                            colors = ButtonDefaults.buttonColors(containerColor = colorAccent, contentColor = TextoOscuroClean),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                            elevation = ButtonDefaults.buttonElevation(0.dp)
                        ) {
                            Text("Reservar Sala", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // --- MI BEBÉ (Con patrón floral) ---
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = colorAccent.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth().clickable { onNavBebe() },
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Box {
                        PatronFlores(color = colorPrimary.copy(alpha = 0.25f))

                        Column(
                            modifier = Modifier.padding(24.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(Color.White, CircleShape)
                                    .shadow(4.dp, CircleShape, spotColor = colorPrimary.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.ChildCare, null, tint = colorPrimary, modifier = Modifier.size(40.dp))
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Mi Bebé", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextoOscuroClean)
                            Text("Registra su crecimiento ❤️", fontSize = 14.sp, color = TextoOscuroClean.copy(alpha = 0.7f))

                            Spacer(modifier = Modifier.height(20.dp))
                            Surface(
                                color = Color.White.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.height(40.dp).clickable { onNavBebe() }
                            ) {
                                Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 24.dp)) {
                                    Text("Ver Perfil", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colorPrimary)
                                }
                            }
                        }
                    }
                }
            }

            // --- INFORMATIVO ---
            item {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.LocalFlorist, contentDescription = null, tint = colorPrimary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Biblioteca de Lactancia", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoOscuroClean)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        val infoItems = listOf(
                            Triple(Icons.Outlined.FavoriteBorder, "Beneficios", "Para ti y tu bebé"),
                            Triple(Icons.Outlined.WaterDrop, "Extracción", "Técnicas sin dolor"),
                            Triple(Icons.Outlined.MenuBook, "Guía 101", "Primeros pasos")
                        )
                        items(infoItems) { (icon, title, subtitle) ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .size(width = 160.dp, height = 180.dp) // Tarjetas un poco más grandes
                                    .clickable { onNavInfo(title) },
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize().padding(16.dp),
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // ICONO MEJORADO: Fondo más grande, Icono más grande
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp) // Antes 40dp
                                            .background(colorPrimary.copy(alpha = 0.1f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(icon, null, tint = colorPrimary, modifier = Modifier.size(32.dp)) // Antes 20dp
                                    }
                                    Column {
                                        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextoOscuroClean)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(subtitle, fontSize = 13.sp, color = TextoOscuroClean.copy(alpha = 0.6f), lineHeight = 15.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- CONSEJO DEL DÍA ---
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        CaritaOsoMinimalista(color = Color(0xFFFFB74D), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Consejo de hoy", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037), fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "¡Hola mamá! 🐻 Recuerda beber agua. Tu hidratación es clave para la producción de leche.",
                                fontSize = 14.sp,
                                color = Color(0xFF5D4037).copy(alpha = 0.9f),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// ELEMENTOS DE DISEÑO ARTÍSTICO (LIRIOS Y FLORES)
// -------------------------------------------------------------------------

@Composable
fun FondoFloralLirio(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val scale = width * 0.45f

        // Posicionamiento en esquina superior derecha
        translate(left = width - scale * 0.5f, top = 20f) {
            // Estilo de línea (Stroke) en lugar de relleno
            val strokeStyle = Stroke(width = 5f, cap = androidx.compose.ui.graphics.StrokeCap.Round)

            // Pétalo Central (Hacia abajo-izquierda)
            val pathCentral = Path().apply {
                moveTo(0f, 0f)
                cubicTo(-scale * 0.3f, scale * 0.2f, -scale * 0.6f, scale * 0.8f, -scale * 0.2f, scale * 1.2f)
                cubicTo(0f, scale * 0.9f, scale * 0.1f, scale * 0.5f, 0f, 0f)
            }

            // Pétalo Izquierdo (Curvado)
            val pathIzq = Path().apply {
                moveTo(0f, 0f)
                cubicTo(-scale * 0.4f, -scale * 0.1f, -scale * 0.9f, scale * 0.2f, -scale * 1.1f, scale * 0.6f)
                quadraticBezierTo(-scale * 0.5f, scale * 0.5f, 0f, 0f)
            }

            // Pétalo Derecho (Curvado)
            val pathDer = Path().apply {
                moveTo(0f, 0f)
                cubicTo(scale * 0.4f, scale * 0.1f, scale * 0.8f, scale * 0.6f, scale * 0.5f, scale * 1.1f)
                quadraticBezierTo(scale * 0.2f, scale * 0.6f, 0f, 0f)
            }

            drawPath(pathCentral, color, style = strokeStyle)
            drawPath(pathIzq, color, style = strokeStyle)
            drawPath(pathDer, color, style = strokeStyle)

            // Estambres (Detalle fino)
            val stamenStroke = Stroke(width = 3f, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            drawLine(color, Offset(0f, 0f), Offset(-scale*0.2f, scale*0.8f), strokeWidth = 3f)
            drawCircle(color, 6f, Offset(-scale*0.2f, scale*0.8f))

            drawLine(color, Offset(0f, 0f), Offset(scale*0.15f, scale*0.9f), strokeWidth = 3f)
            drawCircle(color, 6f, Offset(scale*0.15f, scale*0.9f))
        }
    }
}

@Composable
fun PatronFlores(color: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val step = 80.dp.toPx()
        for (x in 0..size.width.toInt() step step.toInt()) {
            for (y in 0..size.height.toInt() step step.toInt()) {
                val offsetX = if ((y / step.toInt()) % 2 == 0) 0f else step / 2
                val center = Offset(x + offsetX, y.toFloat())

                // Dibujar una flor simple de 4 pétalos
                val petalRadius = 5f
                val offset = 5f
                drawCircle(color, petalRadius, center + Offset(0f, -offset))
                drawCircle(color, petalRadius, center + Offset(0f, offset))
                drawCircle(color, petalRadius, center + Offset(-offset, 0f))
                drawCircle(color, petalRadius, center + Offset(offset, 0f))
                drawCircle(color.copy(alpha = 0.8f), 3f, center) // Centro
            }
        }
    }
}

@Composable
fun CaritaOsoMinimalista(color: Color, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = size.width / 2

        // Orejas
        drawCircle(color = color, radius = radius * 0.35f, center = Offset(centerX - radius * 0.7f, centerY - radius * 0.7f))
        drawCircle(color = color, radius = radius * 0.35f, center = Offset(centerX + radius * 0.7f, centerY - radius * 0.7f))

        // Cabeza
        drawCircle(color = color, radius = radius, center = Offset(centerX, centerY))

        // Hocico (Blanco)
        drawCircle(color = Color.White.copy(alpha = 0.6f), radius = radius * 0.4f, center = Offset(centerX, centerY + radius * 0.2f))

        // Nariz (Marrón oscuro)
        drawCircle(color = Color(0xFF5D4037), radius = radius * 0.15f, center = Offset(centerX, centerY + radius * 0.1f))

        // Ojos
        drawCircle(color = Color(0xFF5D4037), radius = radius * 0.1f, center = Offset(centerX - radius * 0.35f, centerY - radius * 0.2f))
        drawCircle(color = Color(0xFF5D4037), radius = radius * 0.1f, center = Offset(centerX + radius * 0.35f, centerY - radius * 0.2f))
    }
}