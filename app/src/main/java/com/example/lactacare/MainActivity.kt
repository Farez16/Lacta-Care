package com.example.lactacare

// NOTA: No importamos 'android.os.Bundle' aquí para evitar conflictos.

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Imports de tus vistas y dominio
import com.example.lactacare.dominio.RolUsuario
// import com.example.lactacare.theme.LactaCareTheme // <--- Eliminado, usamos MaterialTheme directo
import com.example.lactacare.vistas.*

class MainActivity : ComponentActivity() {

    // Usamos el nombre completo 'android.os.Bundle' para evitar ambigüedades
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // CORRECCIÓN: Usamos MaterialTheme directo (el estándar de Android)
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "bienvenida") {

                        // 1. PANTALLA DE BIENVENIDA
                        composable("bienvenida") {
                            PantallaBienvenida(
                                onRolSeleccionado = { rol ->
                                    navController.navigate("login/${rol.name}")
                                }
                            )
                        }

                        // 2. PANTALLA DE LOGIN
                        composable(
                            route = "login/{rolTexto}",
                            arguments = listOf(navArgument("rolTexto") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val rolTexto = backStackEntry.arguments?.getString("rolTexto") ?: "PACIENTE"
                            val rolEnum = RolUsuario.valueOf(rolTexto)
                            val loginViewModel: LoginViewModel = viewModel()

                            LaunchedEffect(rolEnum) { loginViewModel.setRol(rolEnum) }

                            val loginExitoso by loginViewModel.loginExitoso.collectAsState()

                            LaunchedEffect(loginExitoso) {
                                if (loginExitoso) {
                                    navController.navigate("home/$rolTexto") {
                                        popUpTo("bienvenida") { inclusive = false }
                                    }
                                }
                            }

                            PantallaLogin(
                                viewModel = loginViewModel,
                                onIrARegistro = { rol ->
                                    navController.navigate("registro/${rol.name}")
                                }
                            )
                        }

                        // 3. PANTALLA DE REGISTRO
                        composable(
                            route = "registro/{rolTexto}",
                            arguments = listOf(navArgument("rolTexto") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val rolTexto = backStackEntry.arguments?.getString("rolTexto") ?: "PACIENTE"
                            val rolEnum = RolUsuario.valueOf(rolTexto)
                            val registroViewModel: RegistroPersonaViewModel = viewModel()

                            LaunchedEffect(rolEnum) { registroViewModel.setRol(rolEnum) }

                            val mensaje by registroViewModel.mensaje.collectAsState()

                            LaunchedEffect(mensaje) {
                                if (mensaje?.contains("Exitoso", ignoreCase = true) == true) {
                                    navController.navigate("home/$rolTexto") {
                                        popUpTo("bienvenida") { inclusive = false }
                                    }
                                }
                            }

                            PantallaRegistroPersona(
                                viewModel = registroViewModel,
                                onIrALogin = { navController.popBackStack() }
                            )
                        }

                        // 4. PANTALLA HOME PRINCIPAL
                        composable(
                            route = "home/{rolTexto}",
                            arguments = listOf(navArgument("rolTexto") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val rolTexto = backStackEntry.arguments?.getString("rolTexto") ?: "PACIENTE"
                            val rolEnum = RolUsuario.valueOf(rolTexto)

                            PantallaHome(
                                rolUsuario = rolEnum,
                                onLogout = {
                                    navController.navigate("bienvenida") {
                                        popUpTo(0)
                                    }
                                },
                                // CONEXIÓN DE RUTAS DEL PACIENTE
                                onNavReservas = { navController.navigate("reservas") },
                                onNavBebe = { navController.navigate("mi_bebe") },
                                onNavInfo = { titulo -> navController.navigate("info/$titulo") }
                            )
                        }

                        // 5. SUB-PANTALLAS DEL PACIENTE
                        composable("reservas") {
                            PantallaReservas(onVolver = { navController.popBackStack() })
                        }

                        composable("mi_bebe") {
                            PantallaMiBebe(onVolver = { navController.popBackStack() })
                        }

                        composable(
                            route = "info/{titulo}",
                            arguments = listOf(navArgument("titulo") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val titulo = backStackEntry.arguments?.getString("titulo") ?: "Información"
                            PantallaDetalleInfo(
                                titulo = titulo,
                                onVolver = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}