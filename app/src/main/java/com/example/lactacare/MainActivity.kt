package com.example.lactacare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Imports de tus vistas y dominio
import com.example.lactacare.dominio.RolUsuario
import com.example.lactacare.vistas.LoginViewModel
import com.example.lactacare.vistas.PantallaBienvenida
import com.example.lactacare.vistas.PantallaLogin
// CAMBIO 1: Importamos la nueva pantalla y su ViewModel
import com.example.lactacare.vistas.PantallaRegistroPersona
import com.example.lactacare.vistas.RegistroPersonaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

                    val navController = rememberNavController()

                    // Iniciamos en la BIENVENIDA
                    NavHost(navController = navController, startDestination = "bienvenida") {

                        // 1. PANTALLA DE BIENVENIDA
                        composable("bienvenida") {
                            PantallaBienvenida(
                                onRolSeleccionado = { rol ->
                                    // Navegamos al login enviando el nombre del rol (ej: "DOCTOR")
                                    navController.navigate("login/${rol.name}")
                                }
                            )
                        }

                        // 2. PANTALLA DE LOGIN (Recibe el Rol)
                        composable(
                            route = "login/{rolTexto}",
                            arguments = listOf(navArgument("rolTexto") { type = NavType.StringType })
                        ) { backStackEntry ->

                            val rolTexto = backStackEntry.arguments?.getString("rolTexto") ?: "PACIENTE"
                            val rolEnum = RolUsuario.valueOf(rolTexto)

                            val loginViewModel: LoginViewModel = viewModel()

                            LaunchedEffect(rolEnum) {
                                loginViewModel.setRol(rolEnum)
                            }

                            PantallaLogin(
                                viewModel = loginViewModel,
                                onIrARegistro = {
                                    // Pasamos el mismo rol al registro
                                    navController.navigate("registro/${rolTexto}")
                                }
                            )
                        }

                        // 3. PANTALLA DE REGISTRO (LA NUEVA)
                        composable(
                            route = "registro/{rolTexto}",
                            arguments = listOf(navArgument("rolTexto") { type = NavType.StringType })
                        ) { backStackEntry ->

                            // A. Recuperamos el rol
                            val rolTexto = backStackEntry.arguments?.getString("rolTexto") ?: "PACIENTE"
                            val rolEnum = RolUsuario.valueOf(rolTexto)

                            // B. Preparamos el ViewModel Nuevo (RegistroPersonaViewModel)
                            val registroViewModel: RegistroPersonaViewModel = viewModel()

                            // C. Seteamos el Rol para que el formulario cambie de forma
                            LaunchedEffect(rolEnum) {
                                registroViewModel.setRol(rolEnum)
                            }

                            // D. Mostramos la pantalla nueva
                            PantallaRegistroPersona(
                                viewModel = registroViewModel,
                                onIrALogin = { navController.popBackStack() } // Volver atrás
                            )
                        }
                    }
                }
            }
        }
    }
}