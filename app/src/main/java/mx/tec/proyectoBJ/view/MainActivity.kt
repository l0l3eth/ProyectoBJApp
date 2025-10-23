package mx.tec.proyectoBJ.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
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
 *
 * Se encarga de configurar la ventana principal, inicializar el [AppVM] (ViewModel principal)
 * y establecer el contenido de la UI utilizando Jetpack Compose. Actúa como el anfitrión
 * para toda la navegación y la estructura de la aplicación.
 *
 * Creado por: Estrella Lolbeth Téllez Rivas A01750496
 *
 */
class MainActivity : ComponentActivity() {
    // Inicializa el ViewModel principal usando la delegación de 'viewModels()'.
    // Esto asegura que el ViewModel sobreviva a cambios de configuración como rotaciones.
    private val viewModel: AppVM by viewModels()

    /**
     * Se llama cuando la actividad es creada por primera vez.
     * Aquí se configura la UI de la aplicación.
     *
     * @param savedInstanceState Si la actividad se está recreando, este Bundle contiene
     * el estado guardado previamente.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita que la UI se dibuje de borde a borde para un look moderno.
        enableEdgeToEdge()
        setContent {
            // Aplica el tema personalizado (colores, tipografía) a toda la aplicación.
            PtoyectoBJTheme {
                // Llama al Composable raíz que construye la UI y la navegación.
                AppPrincipal(viewModel)
            }
        }
    }
}

/**
 * Composable raíz que estructura la navegación principal y el menú lateral (`Drawer`).
 *
 * Este Composable es el núcleo de la UI. Gestiona:
 * 1.  El estado y control del menú de navegación lateral ([ModalNavigationDrawer]).
 * 2.  La navegación automática basada en el estado de autenticación del usuario.
 * 3.  La visibilidad condicional de la barra de navegación inferior (`BottomBar`).
 * 4.  La integración del `NavHost` que contiene todas las pantallas de la app.
 *
 * @param appVM La instancia del ViewModel [AppVM] que contiene la lógica de negocio y el estado global.
 */
@Composable
fun AppPrincipal(appVM: AppVM) {
    val navController = rememberNavController()
    // --- ESTADO Y CONTROL DEL MENÚ LATERAL ---
    val estadoMenu = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    // Función lambda para abrir el menú. Se pasará a los componentes que necesiten esta acción.
    val abrirMenu: () -> Unit = {
        coroutineScope.launch {
            estadoMenu.open()
        }
    }
    // Función lambda para cerrar el menú.
    val cerrarMenu: () -> Unit = {
        coroutineScope.launch {
            estadoMenu.close()
        }
    }

    // --- NAVEGACIÓN AUTOMÁTICA POR AUTENTICACIÓN ---
    // Observa el estado de la autenticación para navegar automáticamente al iniciar sesión.
    val usuarioLogeado by appVM.usuarioLogeado.observeAsState()
    LaunchedEffect(usuarioLogeado) {
        if (usuarioLogeado != null) {
            // Si el usuario se logea, navega a la pantalla principal y limpia el backstack
            // para evitar que el usuario regrese a la pantalla de login con el botón "Atrás".
            navController.navigate("PromocionesScreen") {
                popUpTo("InicioSesion") { inclusive = true }
            }
        }
    }

    // --- CONTROL DE VISIBILIDAD DE LA BARRA DE NAVEGACIÓN ---
    // Observa la ruta actual para decidir si se muestra o no la barra de navegación inferior.
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Define las rutas donde la barra de navegación NO debe ser visible (pantallas de flujo inicial).
    val rutasSinBarraNav =
        listOf("Entrada", "Inicio", "InicioSesion", "Registro",
            "registro_usuario", "SolicitudNegocio", "QR", "ActualizarDatos", "PantallaPrincipalNegocio")
    val mostrarBarraNav = currentRoute !in rutasSinBarraNav

//    val rutasSinBarraNavNegocio =
//        listOf("Entrada", "Inicio", "InicioSesion", "Registro",
//            "registro_usuario", "SolicitudNegocio", "ActualizarDatos",
//            "idDigital", "HomeUsuario", "Mapa")
//    val mostrarBarraNavNegocio = currentRoute !in rutasSinBarraNav


    // --- ESTRUCTURA PRINCIPAL DE LA UI ---
    // Contenedor principal que permite un menú deslizable desde el lateral.
    ModalNavigationDrawer(
        drawerState = estadoMenu,
        drawerContent = {
            // El contenido que se muestra dentro del menú lateral.
            AppMenuLateral(
                navController = navController,
                appVM = appVM,
                closeDrawer = cerrarMenu // Pasa la función para que el menú pueda cerrarse desde su interior.
            )
        }
    ) {
        // Scaffold proporciona la estructura básica de Material Design (app bar, bottom bar, etc.).
        Scaffold(
            bottomBar = {
                // Muestra la BarraNavegacion solo si la condición se cumple.
                if (mostrarBarraNav) {
                    BarraNavegacion(
                        navController = navController
                    )
                }
            }
        ) { innerPadding ->
            // El contenido principal de la aplicación, gestionado por AppNavHost.
            AppNavHost(
                navController = navController,
                appVM = appVM,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(Color(0xFFFFF9ED)),
                onMenuClick = abrirMenu // Pasa la función para abrir el menú a los componentes hijos.
            )
        }
    }
}
/**
 * Gestiona el grafo de navegación de la aplicación usando un [NavHost].
 *
 * Define todas las rutas (pantallas) y las transiciones entre ellas. Cada `composable`
 * dentro del `NavHost` representa una pantalla o destino en la aplicación.
 *
 * @param navController El controlador de navegación para gestionar las rutas.
 * @param appVM El ViewModel global [AppVM], pasado a cada pantalla que lo necesite.
 * @param modifier El modificador de Compose para aplicar al [NavHost].
 * @param onMenuClick La función lambda que se ejecutará para abrir el menú lateral desde una pantalla.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    appVM: AppVM,
    modifier: Modifier,
    onMenuClick: () -> Unit,
) {
    // NavHost define el contenedor para el grafo de navegación.
    NavHost(
        navController = navController,
        startDestination = "Inicio", // La pantalla con la que arranca la app.
        modifier = modifier.fillMaxSize()
    ) {
        // --- FLUJO DE AUTENTICACIÓN Y REGISTRO ---

        composable("InicioSesion") {
            InicioSesion(
                onNavigateToRegistro = { navController.navigate("Registro") },
                onNavigateToHomeJoven = { navController.navigate("PromocionesScreen") },
                onNavigateToHomeNegocio = { navController.navigate("PantallaPrincipalNegocio") },
//                onNavigateToPrueba = { navController.navigate("Prueba") },
//                onNavigateToPruebaUsuario = { navController.navigate("ID") },
                appVM = appVM
            )
        }

        composable("PruebaUsuario"){ //TODO: borrar

            HomeUsuario(
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

        composable("Registro") {
            Registro(
                onNavigateToRegistroUsuario = { navController.navigate("registro_usuario") },
                onNavogateToSolicitudNegocio = { navController.navigate("SolicitudNegocio") },
            )
        }

        composable("registro_usuario") {
            IngresoDeDatos(
                appVM = appVM,
                onNavigateToLogin = {
                    navController.navigate("InicioSesion") {
                        // Limpia el backstack hasta la pantalla de inicio para un flujo limpio.
                        popUpTo("Inicio") { inclusive = true }
                    }
                }
            )
        }

        composable("SolicitudNegocio") {
            RellenoDeSolicitud(
                appVM = appVM,
            )
        }

        // --- PANTALLAS PRINCIPALES (POST-AUTENTICACIÓN) ---

        composable("PromocionesScreen") {
            PromocionesScreen(
                appVM = appVM
            )
        }

        composable("HomeUsuario") {
            HomeUsuario(
                appVM = appVM
            )
        }

        composable("ID") {
            PantallaIDDigital(
                appVM = appVM
            )
        }

        composable("PantallaPrincipalNegocio") {
            NegocioProfileScreen(

            )
        }

        // --- PANTALLAS DEL MENÚ LATERAL ---

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

        composable("ConfirmarSalida") {
            ConfirmarSalida(
                appVM = appVM,
                onDismissRequest = { navController.popBackStack() }, // Cierra el diálogo al cancelar
                onConfirmar = {
                    navController.navigate("Inicio") {
                        popUpTo("Inicio") { inclusive = true }
                    }
                }
            )
        }

        composable("EscanearQR") {
            EscaneoQR(
                paddingValues = PaddingValues()
            )
        }

        composable("PantallaPrincipalNegocio") {
            NegocioProfileScreen(

            )
        }

        composable("Mapa") {
            Mapa(
                appVM = appVM
            )
        }

        composable("QR") {
            EscaneoQR(
                paddingValues = PaddingValues()
            )
        }
    }
}

