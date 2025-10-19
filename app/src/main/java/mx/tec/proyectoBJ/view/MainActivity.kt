package mx.tec.proyectoBJ.view

import Inicio
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
import androidx.compose.material3.Scaffold
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
 * `MainActivity` es la actividad principal y el punto de entrada de la aplicación.
 * Se encarga de configurar la ventana, inicializar el ViewModel principal y establecer
 * el contenido de la UI con Jetpack Compose.
 */
class MainActivity : ComponentActivity() {
    // Inicializa el ViewModel principal que será compartido a través de la app.
    private val viewModel: AppVM by viewModels()

    /**
     * Se llama cuando la actividad es creada por primera vez.
     * Configura el edge-to-edge display y establece el Composable raíz de la aplicación.
     *
     * @param savedInstanceState Si la actividad se está recreando después de haber sido
     * cerrada por el sistema, este Bundle contiene el estado más reciente.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita el modo de pantalla completa (edge-to-edge).
        enableEdgeToEdge()
        setContent {
            // Aplica el tema personalizado de la aplicación.
            PtoyectoBJTheme {
                // Llama al Composable principal que construye la UI y la navegación.
                AppPrincipal(viewModel)
            }
        }
    }
}

/**
 * Composable raíz que estructura la navegación principal y el menú lateral.
 * Gestiona el estado del `NavController` y del menú de navegación (`ModalNavigationDrawer`).
 *
 * @param appVM La instancia del ViewModel [AppVM] que contiene la lógica de negocio y el estado global.
 */
@Composable
fun AppPrincipal(appVM: AppVM) {
    val navController = rememberNavController()
    // 1. ESTADO Y CONTROL DEL MENÚ LATERAL
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
    // Si el usuario se logea, navega a la pantalla principal y limpia el backstack.
    val usuarioLogeado by appVM.usuarioLogeado.observeAsState()
    LaunchedEffect(usuarioLogeado) {
        if (usuarioLogeado != null) {
            navController.navigate("PromocionesScreen") {
                popUpTo("InicioSesion") { inclusive = true }
            }
        }
    }

    // 1. Observa la ruta actual desde el NavController
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 2. Define las rutas donde la barra de navegación NO debe ser visible
    val rutasSinBarraNav =
        listOf("Entrada", "Inicio", "InicioSesion", "Registro", "registro_usuario")
    val mostrarBarraNav = currentRoute !in rutasSinBarraNav

    // 2. CONTENEDOR PRINCIPAL CON MENÚ LATERAL
    // ModalNavigationDrawer permite mostrar un menú deslizable desde el lateral.
    ModalNavigationDrawer(
        drawerState = estadoMenu,
        drawerContent = {
            // Contenido del menú lateral.
            AppMenuLateral(
                navController = navController,
                appVM = appVM,
                closeDrawer = cerrarMenu // Pasa la función para que el menú pueda cerrarse.
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                // 3. Muestra la BarraNavegacion solo si la condición se cumple
                if (mostrarBarraNav) {
                    BarraNavegacion(
                        navController = navController
                    )
                }
            }
        ) { innerPadding ->
            // Contenido principal de la aplicación, gestionado por AppNavHost.
            AppNavHost(
                navController = navController,
                appVM = appVM,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF9ED)),
                onMenuClick = abrirMenu // Pasa la función para abrir el menú al contenido principal
            )
        }
    }
}
/**
 * Gestiona el grafo de navegación de la aplicación usando un [NavHost].
 * Define todas las rutas (pantallas) y las transiciones entre ellas.
 *
 * @param navController El controlador de navegación para gestionar las rutas.
 * @param appVM El ViewModel global [AppVM].
 * @param modifier El modificador de Compose para aplicar al [NavHost].
 * @param onMenuClick La función lambda que se ejecutará para abrir el menú lateral.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    appVM: AppVM,
    modifier: Modifier,
    onMenuClick: () -> Unit,
//    onBack: () -> Unit,
//    onLogoutSuccess: () -> Unit = {}
) {
    // NavHost define el grafo de navegación.
    NavHost(
        navController = navController,
        startDestination = "Entrada", // La pantalla inicial de la app.
        modifier = modifier.fillMaxSize()
    ) {
        // Define la pantalla "Entrada"
        composable("Entrada") {
            Entrada(
                navController = navController,
                appVM = appVM
            )
        }

        // Define la pantalla "Inicio"
        composable("Inicio") {
            Inicio(
                onNavigateToInicioSesion = { navController.navigate("InicioSesion") },
                onNavigateToRegistro = { navController.navigate("Registro") },
                appVM = appVM
            )
        }

        // Define la pantalla "InicioSesion"
        composable("InicioSesion") {
            InicioSesion(
                onNavigateToRegistro = { navController.navigate("Registro") },
                onNavigateToPrincipal = {
                    navController.navigate("Pruebas")
                },
                appVM = appVM
            )
        }

        composable("Pruebas"){
            Promociones( appVM = appVM) //TODO Borrar cuando acabe de probar todo
        }

        // Define la pantalla "Registro"
        composable("Registro") {
            Registro(
                onNavigateToRegistroUsuario = { navController.navigate("registro_usuario") },
            )
        }

        // Define la pantalla "registro_usuario" para el ingreso de datos
        composable("registro_usuario") {
            IngresoDeDatos(
                appVM = appVM,
                onNavigateToLogin = {
                    navController.navigate("InicioSesion") {
                        popUpTo("Inicio") { inclusive = true }
                    }
                }
            )
        }

        // Define la pantalla para actualizar datos con acceso al menú
        composable("ActualizarDatos") {
            ActualizarDatos(
                appVM = appVM,
                onBack = { navController.popBackStack() },
                onLogoutSuccess = {
                    navController.navigate("Inicio") {
                        popUpTo("Inicio") { inclusive = true }
                    }
                }
            )
        }

        // Define la pantalla para confirmar la salida (logout)
        composable("ConfirmarSalida") {
            ConfirmarSalida(
                appVM = appVM,
                onDismissRequest = { navController.popBackStack() },
            )
        }

        composable("Mapa") {
            Mapa(
                appVM = appVM,
            )
        }

        composable("Promociones"){
            Promociones(
                appVM = appVM
            )
        }

        composable("ID"){
            PantallaIDDigital(
                appVM = appVM
            )
        }
    }
}
