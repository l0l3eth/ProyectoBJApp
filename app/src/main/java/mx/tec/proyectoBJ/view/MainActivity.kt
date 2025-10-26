package mx.tec.proyectoBJ.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import mx.tec.proyectoBJ.model.TipoUsuario
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    LaunchedEffect(usuarioLogeado) {
        if (usuarioLogeado != null) {
            val destino = when (usuarioLogeado?.tipoUsuario) {
                // Comparamos con los valores del enum, no con Strings
                TipoUsuario.NEGOCIO -> "PantallaPrincipalNegocio"
                TipoUsuario.JOVEN -> "PromocionesScreen"
                else -> null
            }

            if (destino != null) {
                navController.navigate(destino) {
                    // Limpia la pila para no volver al login
                    popUpTo("InicioSesion") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    // --- CONTROL DE VISIBILIDAD DE LA BARRA DE NAVEGACIÓN ---
    // Observa la ruta actual para decidir si se muestra o no la barra de navegación inferior.
    // Listas que definen en QUÉ pantallas se muestra cada barra.
    val rutasConBarraNegocio = listOf("PantallaPrincipalNegocio", "EscanearQR", "QR")
    val rutasConBarraUsuario = listOf("PromocionesScreen", "HomeUsuario", "ID", "Mapa", "EscanearQR", "QR")

    // Determina si la barra debe mostrarse y de qué tipo debe ser.
    val mostrarBarra: Boolean
    val esUsuarioNegocio: Boolean

    if (usuarioLogeado?.tipoUsuario == TipoUsuario.NEGOCIO) {
        mostrarBarra = rutaActual in rutasConBarraNegocio
        esUsuarioNegocio = true
    } else { // Si no es NEGOCIO, o no está logueado, se asumen las reglas de JOVEN/invitado
        mostrarBarra = rutaActual in rutasConBarraUsuario
        esUsuarioNegocio = false
    }
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
                if (mostrarBarra) {
                    // Pasamos un booleano para que la barra sepa qué botones mostrar
                    BarraNavegacion(
                        navController = navController,
                        esNegocio = esUsuarioNegocio
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

        //////Pantalla de Promociones//////
        composable("PromocionesScreen") {
            PromocionesScreen(
                appVM = appVM,
                onNavigateToCreatePromocion = {
                    navController.navigate("crear_promocion")
                }
            )
        }

        composable("crear_promocion") {
            Box (modifier=Modifier.fillMaxSize(),contentAlignment = Alignment.Center){
                Text("Pantalla para Crear/Editar Promoción (En construcción)")
            }
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
                appVM = appVM,
                navController = navController
            )
        }

        composable("EdicionNegocio") {
            // Aquí llamas a la pantalla de edición del perfil.
            NegocioEdicionPerfil(
                appVM = appVM,
                navController = navController
            )
        }


        composable("QR") {
            EscaneoQR(
                paddingValues = PaddingValues()
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

        composable("Mapa") {
            Mapa(
                appVM = appVM
            )
        }

        composable("PantallaEdicionNegocio") {
            NegocioEdicionPerfil(
                appVM = appVM,
                navController = navController
            )
        }
    }
}

