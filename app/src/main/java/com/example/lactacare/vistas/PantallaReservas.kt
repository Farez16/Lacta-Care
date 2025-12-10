package com.example.lactacare.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lactacare.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaReservas(onVolver: () -> Unit) {
    Scaffold(
        containerColor = FondoBlancoClean,
        topBar = {
            TopAppBar(
                title = { Text("Reservar Lactario", fontWeight = FontWeight.Bold, color = TextoOscuroClean) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = TextoOscuroClean)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Selecciona un Horario", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextoOscuroClean)
            }

            // Simulación de horarios
            items(5) { index ->
                val hora = "${8 + index}:00 AM"
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.AccessTime, null, tint = MomPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(hora, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextoOscuroClean)
                        }
                        Button(
                            onClick = { /* Lógica futura */ },
                            colors = ButtonDefaults.buttonColors(containerColor = MomPrimary),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Disponible", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}