package com.example.lactacare.vistas.lactarios

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.lactacare.dominio.Lactario

// Importamos tus colores del tema
import com.example.lactacare.theme.MintPrimary
import com.example.lactacare.theme.LactarioBg
import com.example.lactacare.theme.StatusGreen
import com.example.lactacare.theme.StatusRed
import com.example.lactacare.theme.StatusYellow
import com.example.lactacare.theme.TextoOscuroClean
import com.example.lactacare.theme.SoftPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaReservas(
    onVolver: () -> Unit,
    viewModel: LactarioViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = LactarioBg, // #f6f8f8
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Lactarios Disponibles",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextoOscuroClean
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = TextoOscuroClean)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = LactarioBg.copy(alpha = 0.95f)
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {

            // 1. Buscador Redondeado (Estilo HTML)
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                BuscadorEstiloHtml()
            }

            // 2. Filtros (Chips Horizontales)
            FiltrosChipsHtml()

            // 3. Contenido Principal (Mapa + Lista)
            when (val estado = uiState) {
                is LactarioUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MintPrimary)
                    }
                }
                is LactarioUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Ocurrió un error: ${estado.mensaje}", color = StatusRed)
                    }
                }
                is LactarioUiState.Success -> {
                    ListaLactariosHtml(lactarios = estado.lactarios)
                }
            }
        }
    }
}

// --- LISTA PRINCIPAL ---
@Composable
fun ListaLactariosHtml(lactarios: List<Lactario>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Ítem 0: Tarjeta de Mapa Grande
        item {
            Card(
                shape = RoundedCornerShape(24.dp), // Bordes muy redondeados como el diseño
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth().height(220.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter("https://img.freepik.com/free-vector/city-map-background-concept_23-2148003732.jpg"), // Placeholder mapa estilo vector
                    contentDescription = "Mapa Interactivo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Ítem 1...N: Tarjetas de Lactarios o Empty State
        if (lactarios.isEmpty()) {
            item { EmptyStateHtml() }
        } else {
            items(lactarios) { lactario ->
                CardSalaHtml(lactario)
            }
        }
    }
}

// --- TARJETA DE LACTARIO (Estilo Exacto HTML) ---
@Composable
fun CardSalaHtml(lactario: Lactario) {
    // Simulamos estados aleatorios para que se vea variado como el diseño
    val (statusColor, statusText) = when (lactario.id % 3) {
        0 -> StatusRed to "Ocupado"
        1 -> StatusYellow to "Reservado"
        else -> StatusGreen to "Disponible ahora" // Por defecto
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(20.dp), // Bordes suaves
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp) // Altura fija para layout consistente
            .clickable { /* Navegar al detalle */ }
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(12.dp)) {

            // COLUMNA IZQUIERDA (Info)
            Column(
                modifier = Modifier.weight(1.6f).fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Estado (Punto + Texto)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(statusColor, CircleShape))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(statusText, color = statusColor, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }

                // 2. Título y Dirección
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = lactario.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextoOscuroClean,
                        maxLines = 2,
                        lineHeight = 20.sp
                    )
                    Text(
                        text = lactario.direccion, // "Piso 3, junto a RRHH"
                        fontSize = 13.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }

                // 3. Horario (Icono + Texto)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Schedule, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("L-V, 9:00 - 18:00", fontSize = 12.sp, color = Color.Gray)
                }

                // 4. Badge Distancia (Pastilla redondeada)
                Surface(
                    color = MintPrimary.copy(alpha = 0.15f), // Fondo menta muy suave
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "a 500m", // Dato simulado
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MintPrimary, // Texto menta
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // COLUMNA DERECHA (Imagen Vertical)
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).fillMaxHeight()
            ) {
                Image(
                    // Imagen estilo interior minimalista
                    painter = rememberAsyncImagePainter("https://images.unsplash.com/photo-1519494026892-80bbd2d6fd0d?q=80&w=400&auto=format&fit=crop"),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

// --- COMPONENTES AUXILIARES ESTILO HTML ---

@Composable
fun BuscadorEstiloHtml() {
    Surface(
        shape = RoundedCornerShape(50), // Completamente redondo (Pill shape)
        shadowElevation = 2.dp,
        color = Color.White,
        modifier = Modifier.fillMaxWidth().height(54.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp) // Padding interno generoso
        ) {
            Icon(Icons.Default.Search, null, tint = Color.LightGray, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Buscar por lugar o dirección...", color = Color.LightGray, fontSize = 15.sp)
        }
    }
}

@Composable
fun FiltrosChipsHtml() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item { ChipHtml("Disponible", MintPrimary, true) }
        item { ChipHtml("Ocupado", StatusRed, false) }
        item { ChipHtml("Reservado", StatusYellow, false) }
    }
}

@Composable
fun ChipHtml(texto: String, colorBase: Color, seleccionado: Boolean) {
    // Si está seleccionado: Fondo Color, Texto Blanco
    // Si no: Fondo Blanco, Texto Color (o gris) -> En tu diseño parece que usan botones sólidos de color
    // Usaremos el estilo "Sólido" de la imagen para los botones activos

    val background = if (seleccionado) colorBase else colorBase.copy(alpha = 0.8f) // Un poco más suave si no es el principal
    // O según la imagen: Rojo solido, Amarillo solido...

    Surface(
        color = colorBase,
        shape = RoundedCornerShape(50),
        shadowElevation = 2.dp,
        modifier = Modifier.height(36.dp).clickable { }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = texto,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (colorBase == StatusYellow) TextoOscuroClean else Color.White // El amarillo lleva texto oscuro
            )
        }
    }
}

@Composable
fun EmptyStateHtml() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Círculo rosa con icono
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(SoftPink.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.SearchOff, null, tint = SoftPink, modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("No encontramos lactarios", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextoOscuroClean)
        Text(
            "Parece que no hay lactarios cerca con estos filtros.",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Rosa "Ampliar Búsqueda"
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = SoftPink),
            shape = RoundedCornerShape(50),
            modifier = Modifier.height(48.dp)
        ) {
            Text("Ampliar Búsqueda", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}