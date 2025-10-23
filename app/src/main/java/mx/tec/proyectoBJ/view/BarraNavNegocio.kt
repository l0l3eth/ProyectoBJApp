package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import mx.tec.proyectoBJ.morado

/**
 * Clase de datos que representa un único elemento en la barra de navegación.
 *
 * @property label El texto que se mostrará debajo del ícono en la barra de navegación.
 * @property icon El vector de imagen [ImageVector] que se usará para el ícono.
 * @property route La ruta de navegación asociada al elemento.
 * Creado por: Estrella Lolbeth Téllez Rivas A01750496
 */
// MODIFICADO: Añadimos 'route' para asociar el ítem con una ruta del NavController
data class NavItemNegocio(val label: String, val icon: ImageVector, val route: String)

/**
 * Lista predefinida de objetos [NavItem] que representan las opciones
 * en la barra de navegación inferior de la aplicación.
 * Cada elemento define una sección principal de la app.
 */
val navItemsNegocio = listOf(
    NavItemNegocio("Inicio", Icons.Default.Store, "PaginaPrincipalNegocio"),
    NavItemNegocio("Promociones", Icons.Default.Discount, "PromocionesScreen"),
    NavItemNegocio("Escanear QR", Icons.Default.QrCode, "QR"),
)

/**
 * Composable que renderiza la barra de navegación inferior de la aplicación.
 *
 * Esta barra de navegación obtiene el estado de selección directamente del [navController],
 * asegurando que siempre refleje la pantalla actual. El elemento seleccionado
 * se resalta visualmente con un cambio de color y un fondo circular.
 *
 * La navegación se gestiona llamando a `navController.navigate`.
 *
 * @param navController El controlador de navegación que gestiona el estado y las acciones de navegación.
 */
@Composable
fun BarraNavegacionNegocios(navController: NavHostController) {
    // 1. OBTENEMOS LA RUTA ACTUAL DESDE EL NAVCONTROLLER
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        navItemsNegocio.forEach { item ->
            // 2. LA SELECCIÓN DEPENDE DE LA RUTA ACTUAL, NO DE UN ESTADO LOCAL
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    // 3. NAVEGAMOS USANDO EL NAVCONTROLLER Y SU RUTA ASOCIADA
                    navController.navigate(item.route) {
                        // Optimiza la pila de navegación para evitar duplicados
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        modifier = Modifier
                            .run {
                                if (isSelected)
                                    background(morado.copy(alpha = 0.1f), CircleShape)
                                else
                                    this
                            }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                },
                label = { Text(text = item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = morado,
                    selectedTextColor = morado,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
@Preview
fun BarraNavegacionNegocioPreview(){
    BarraNavegacion(NavHostController(LocalContext.current))
}
