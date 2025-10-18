package mx.tec.proyectoBJ.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.ptoyectobj.morado
import mx.tec.ptoyectobj.naranja

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AppMenuLateral(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    appVM: AppVM // Obtenemos la instancia del ViewModel
) {
    // Estado para controlar la visibilidad del diálogo
    var mostrarDialogo by remember { mutableStateOf(false) }

    val estadoMenu = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    // Efecto para navegar cuando el borrado sea exitoso
    LaunchedEffect(Unit) {
        appVM.borradoExitoso.collect { exito ->
            if (exito) {
                // Navegar a la pantalla de login y limpiar el historial
                navController.navigate("Inicio") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        }
    }


    ModalNavigationDrawer(
        drawerState = estadoMenu,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    "Mi Cuenta",
                    modifier = Modifier.padding(16.dp),
                )
                HorizontalDivider()
                Spacer(modifier = Modifier.height(12.dp))

                // --- Item para ir a los ajustes del usuario ---
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes") },
                    label = { Text("Ajustes de Cuenta") },
                    selected = rutaActual == "ActualizarDatos",
                    onClick = {
                        // Navega a la pantalla de ActualizarDatos.kt
                        navController.navigate("ActualizarDatos")
                        coroutineScope.launch { estadoMenu.close() }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = naranja.copy(alpha = 0.3f),
                        unselectedContainerColor = morado
                    )
                )

                // --- Item para cerrar sesión (abre el diálogo) ---
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar Sesión") },
                    label = { Text("Cerrar Sesión") },
                    selected = false, // Esta acción no selecciona una pantalla
                    onClick = {
                        // Muestra el diálogo de confirmación
                        mostrarDialogo = true
                        // Cierra el menú
                        coroutineScope.launch { estadoMenu.close() }
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = morado
                    )
                )
            }
        }
    ) {
        // El contenido principal de la app
        AppPrincipal(appVM = appVM)
    }

    // Mostramos el diálogo si el estado es true
    if (mostrarDialogo) {
        val estaBorrando by appVM.estaBorrando.collectAsState(false)
        ConfirmarSalida(
            appVM = appVM,
            onDismissRequest = {
                // Solo permite cerrar el diálogo si no se está ejecutando la acción
                if (!estaBorrando) {
                    mostrarDialogo = false
                }
            }
        )
    }
}
