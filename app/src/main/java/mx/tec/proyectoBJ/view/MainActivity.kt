package mx.tec.proyectoBJ.view

import Inicio // Asegúrate de que Inicio.kt también esté en el paquete 'view'
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.tec.proyectoBJ.ui.theme.PtoyectoBJTheme
import mx.tec.proyectoBJ.viewmodel.AppVM

/**
 * MainActivity es la actividad principal y el punto de entrada de la aplicación.
 * Configura la navegación y el tema general.
 */

class MainActivity : ComponentActivity() {
    val viewModel: AppVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Contenedor principal que configura el NavController y el tema.
            AppPrincipal(viewModel)
        }
    }
}

@Composable
fun AppPrincipal(appVM: AppVM) {
    val navController = rememberNavController()
    val usuarioLogeado by appVM.usuarioLogeado.observeAsState()

    LaunchedEffect(usuarioLogeado) {
        if (usuarioLogeado != null) {
            // ÉXITO: Navegar a la pantalla principal de la app
            navController.navigate("PromocionesScreen") { // <-- Define el nombre de tu pantalla principal
                // Limpia la pila para que no se pueda volver al login con el botón de atrás
                popUpTo("InicioSesion") { inclusive = true }
            }
        }
    }

    PtoyectoBJTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            // Host de navegación que gestiona las pantallas de la app.
            AppNavHost(
                navController,
                appVM,
                modifier = Modifier
                    .padding(innerPadding)
                    .background(Color(0xFFFFF9ED)) // Color de fondo general
            )
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    appVM: AppVM,
    modifier: Modifier
) {
    // NavHost define el grafo de navegación de la aplicación.
    NavHost(
        navController = navController,
        startDestination = "Entrada", // La app siempre inicia en la pantalla de Entrada, que es la splash
        modifier = modifier.fillMaxSize()
    ) {
        // Define la ruta "Entrada" y le asigna el Composable 'Entrada' (de Entrada.kt).
        composable("Entrada") {
            Entrada(
                navController = navController,
                appVM = appVM
            )
        }

        composable("Inicio") {
            Inicio(
                onNavigateToInicioSesion = { navController.navigate("InicioSesion")},
                onNavigateToRegistro = { navController.navigate("Registro") },
                appVM = appVM
            )
        }

        // Define la ruta "Inicio" y le asigna el Composable 'Inicio' (de Inicio.kt).
        composable("InicioSesion") {
            InicioSesion(
                onNavigateToRegistro = { navController.navigate("Registro") },
                appVM = appVM
            )
        }

        // Define la ruta "Registro" y le asigna el Composable 'Registro' (de Registro.kt).
        composable("Registro") {
            Registro(
                onNavigateToRegistroUsuario = { navController.navigate("registro_usuario") },
            )
        }

        // Define la ruta "RegistroUsuario" y le asigna el Composable 'RegistroUsuario' (de RegistroUsuario.kt).
        composable("registro_usuario") {
            IngresoDeDatos(
                appVM = appVM
            )
        }

        composable("PromocionesScreen") {
            PantallaPrincipalUsuario(
                appVM = appVM
            )
        }
    }
}
