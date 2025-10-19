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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import mx.tec.proyectoBJ.ui.theme.PtoyectoBJTheme
import mx.tec.proyectoBJ.viewmodel.AppVM

/**
 * MainActivity es la actividad principal y el punto de entrada de la aplicación.
 * Configura la navegación y el tema general.
 */

class MainActivity : ComponentActivity() {
    private val viewModel: AppVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Contenedor principal que configura el NavController y el tema.
            PtoyectoBJTheme {
                AppPrincipal(viewModel)
            }
        }
    }
}

@Composable
fun AppPrincipal(appVM: AppVM) {
    val navController = rememberNavController()

    // 1. ESTADO Y CONTROL DEL MENÚ LATERAL
    // Se mueven aquí para que AppPrincipal pueda controlarlos.
    val estadoMenu = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    // Función para abrir el menú que se pasará a los componentes hijos.
    val abrirMenu: () -> Unit = {
        coroutineScope.launch {
            estadoMenu.open()
        }
    }

    // Función para cerrar el menú.
    val cerrarMenu: () -> Unit = {
        coroutineScope.launch {
            estadoMenu.close()
        }
    }

    // Observa el estado de la autenticación para navegar automáticamente.
    val usuarioLogeado by appVM.usuarioLogeado.observeAsState()
    LaunchedEffect(usuarioLogeado) {
        if (usuarioLogeado != null) {
            navController.navigate("PromocionesScreen") {
                popUpTo("InicioSesion") { inclusive = true }
            }
        }
    }

    // 2. SE USA ModalNavigationDrawer COMO CONTENEDOR PRINCIPAL
    // Envuelve el NavHost para que el menú esté disponible en todas las pantallas.
    ModalNavigationDrawer(
        drawerState = estadoMenu,
        drawerContent = {
            // El contenido del menú (el drawer)
            AppMenuLateral(
                navController = navController,
                appVM = appVM,
                closeDrawer = cerrarMenu // Le pasamos la función para cerrarse
            )
        }
    ) {
        // El contenido principal de la app (el NavHost).
        AppNavHost(
            navController = navController,
            appVM = appVM,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9ED)),
            onMenuClick = abrirMenu // 3. Se pasa la acción de abrir a NavHost
        )
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    appVM: AppVM,
    modifier: Modifier,
    onMenuClick: () -> Unit // Recibe la acción para abrir el menú
) {
    // NavHost define el grafo de navegación de la aplicación.
    NavHost(
        navController = navController,
        startDestination = "Entrada", // La app siempre inicia en la pantalla de Entrada
        modifier = modifier.fillMaxSize()
    ) {
        // ... (tus otras rutas como "Entrada", "Inicio", "InicioSesion", etc. van aquí sin cambios)
        composable("Entrada") {
            Entrada(
                navController = navController,
                appVM = appVM
            )
        }

        composable("Inicio") {
            Inicio(
                onNavigateToInicioSesion = { navController.navigate("InicioSesion") },
                onNavigateToRegistro = { navController.navigate("Registro") },
                appVM = appVM
            )
        }

        composable("InicioSesion") {
            InicioSesion(
                onNavigateToRegistro = { navController.navigate("Registro") },
                onNavigateToPrincipal = { navController.navigate("PromocionesScreen") {
                    popUpTo("InicioSesion") { inclusive = true }
                } },
                appVM = appVM
            )
        }

        composable("Registro") {
            Registro(
                onNavigateToRegistroUsuario = { navController.navigate("registro_usuario") },
            )
        }

        composable("registro_usuario") {
            IngresoDeDatos(
                appVM = appVM,
                onNavigateToLogin = {
                    navController.navigate("InicioSesion"){
                        popUpTo("Inicio"){ inclusive = true }
                    }
                }
            )
        }

        composable("PromocionesScreen") {
            PantallaPrincipalUsuario(
                appVM = appVM,
                onMenuClick = onMenuClick // Le pasamos el click del menú
            )
        }

        composable("ActualizarDatos") {
            ActualizarDatos(
                appVM = appVM,
                onMenuClick = onMenuClick // También aquí si quieres el menú
            )
        }

        composable("ConfirmarSalida") {
            ConfirmarSalida(
                appVM = appVM,
                onDismissRequest = { navController.popBackStack() },
            )
        }

        // Ya no necesitas las rutas "MenuLateralParteSuperior" y "MenuLateral"
        // porque ahora se gestionan directamente y no por navegación.
    }
}


