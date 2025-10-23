package mx.tec.proyectoBJ.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.proyectoBJ.blanco
import mx.tec.proyectoBJ.morado
import mx.tec.proyectoBJ.naranja

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun AppMenuLateral(
    navController: NavHostController,
    appVM: AppVM, // El ViewModel se pasa, no se crea aquí
    closeDrawer: () -> Unit // Acción para cerrar el menú
) {
    // Estado para controlar la visibilidad del diálogo de confirmación
    var mostrarDialogo by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = navBackStackEntry?.destination?.route

    // Efecto para navegar al cerrar sesión exitosamente
    LaunchedEffect(Unit) {
        appVM.borradoExitoso.collect { exito ->
            if (exito) {
                // Navegar a la pantalla de inicio y limpiar el historial
                navController.navigate("Inicio") {
                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                }
            }
        }
    }

    // Este Composable ahora SOLO es la "hoja" del menú
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
                navController.navigate("ActualizarDatos")
                closeDrawer() // Cierra el menú después de navegar
            },
            colors = NavigationDrawerItemDefaults.colors(
                selectedContainerColor = naranja.copy(alpha = 0.3f),
                unselectedContainerColor = blanco
            )
        )

        // --- Item para cerrar sesión (abre el diálogo) ---
        NavigationDrawerItem(
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión") },
            label = { Text("Cerrar Sesión") },
            selected = false,
            onClick = {
                mostrarDialogo = true // Muestra el diálogo
                closeDrawer() // Cierra el menú
            },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = blanco
            )
        )
    }

    // Mostramos el diálogo de confirmación si es necesario
    if (mostrarDialogo) {
        val estaBorrando by appVM.estaBorrando.collectAsState(false)
        ConfirmarSalida(
            appVM = appVM,
            onDismissRequest = {
                if (!estaBorrando) {
                    mostrarDialogo = false
                }
            },
            onConfirmar = {
                appVM.eliminarUsuario(appVM.usuarioLogeado.value?.idUsuario ?: 0)
                mostrarDialogo = false
            }
        )
    }
}

@Composable
@Preview
fun AppMenuLateralPreview() {
    AppMenuLateral(
        navController = rememberNavController(),
        appVM = viewModel(),
        closeDrawer = {}
    )
}
