package com.example.lactacare.vistas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
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
fun PantallaMiBebe(onVolver: () -> Unit) {
    Scaffold(
        containerColor = FondoBlancoClean,
        topBar = {
            TopAppBar(
                title = { Text("Perfil del Bebé", fontWeight = FontWeight.Bold, color = TextoOscuroClean) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, null, tint = TextoOscuroClean)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto Gigante
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(MomAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AddAPhoto, null, tint = MomPrimary, modifier = Modifier.size(40.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Formulario simple simulado
            OutlinedTextField(
                value = "", onValueChange = {},
                label = { Text("Nombre del Bebé") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = "", onValueChange = {},
                label = { Text("Fecha de Nacimiento") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onVolver() }, // Simular guardar y volver
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MomPrimary)
            ) {
                Text("Guardar Datos", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}