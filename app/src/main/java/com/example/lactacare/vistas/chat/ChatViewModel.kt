package com.example.lactacare.vistas.chat

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MensajeChat(
    val texto: String,
    val esUsuario: Boolean, // true = Yo, false = IA
    val esAnimacionEscribiendo: Boolean = false
)

class ChatViewModel : ViewModel() {
    // Usamos mutableStateListOf para actualizaciones automáticas en la UI
    val mensajes = mutableStateListOf<MensajeChat>()

    init {
        // Mensaje de bienvenida inicial
        mensajes.add(MensajeChat("¡Hola! Soy tu asistente de lactancia. ¿En qué puedo apoyarte hoy?", false))
    }

    fun enviarMensaje(texto: String) {
        if (texto.isBlank()) return

        // 1. Agregar mensaje del usuario
        mensajes.add(MensajeChat(texto, true))

        // 2. Simular "Escribiendo..."
        viewModelScope.launch {
            val loadingMsg = MensajeChat("", false, true)
            mensajes.add(loadingMsg)

            // Simular tiempo de red
            delay(2000)

            // 3. Quitar loading y responder
            mensajes.remove(loadingMsg)
            mensajes.add(MensajeChat("Entiendo tu duda sobre \"$texto\". Aquí tienes algunos consejos...", false))
        }
    }
}