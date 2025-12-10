package com.example.lactacare.vistas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lactacare.theme.BordeGrisClean
import com.example.lactacare.theme.TextoOscuroClean

@Composable
fun DashboardAdmin(colorPrimary: Color, colorAccent: Color) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Tarjeta Principal de Bienvenida/Estado
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = colorPrimary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Panel de Control", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Text("Resumen diario del sistema", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
                    }
                    Icon(Icons.Outlined.Analytics, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                }
            }
        }

        // Grid de Estadísticas
        item {
            Column {
                Text("Estadísticas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoOscuroClean)
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DashboardStatCard(Modifier.weight(1f), "Usuarios", "1,240", Icons.Outlined.Person, colorPrimary)
                    DashboardStatCard(Modifier.weight(1f), "Doctores", "45", Icons.Outlined.MedicalServices, colorPrimary)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DashboardStatCard(Modifier.weight(1f), "Citas Hoy", "89", Icons.Outlined.Event, colorPrimary)
                    DashboardStatCard(Modifier.weight(1f), "Alertas", "3", Icons.Outlined.Warning, Color(0xFFEF5350))
                }
            }
        }

        // Acciones Rápidas
        item {
            Text("Gestión Rápida", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoOscuroClean)
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text("Registrar Nuevo Empleado", fontWeight = FontWeight.SemiBold) },
                        leadingContent = { Icon(Icons.Outlined.PersonAdd, null, tint = colorPrimary) },
                        trailingContent = { Icon(Icons.Default.ChevronRight, null) },
                        modifier = Modifier.clickable { }
                    )
                    Divider(color = BordeGrisClean.copy(alpha = 0.5f))
                    ListItem(
                        headlineContent = { Text("Ver Reportes Mensuales", fontWeight = FontWeight.SemiBold) },
                        leadingContent = { Icon(Icons.Outlined.Description, null, tint = colorPrimary) },
                        trailingContent = { Icon(Icons.Default.ChevronRight, null) },
                        modifier = Modifier.clickable { }
                    )
                }
            }
        }
    }
}