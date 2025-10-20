package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.ptoyectobj.morado

/**
 * Clase de datos que representa un único elemento en la barra de navegación.
 *
 * @property label El texto que se mostrará debajo del ícono en la barra de navegación.
 * @property icon El vector de imagen [ImageVector] que se usará para el ícono.
 */
data class NavItem(val label: String, val icon: ImageVector)

/**
 * Lista predefinida de objetos [NavItem] que representan las opciones
 * en la barra de navegación inferior de la aplicación.
 * Cada elemento define una sección principal de la app.
 */
val navItems = listOf(
    NavItem("Inicio", Icons.Default.Home),
    NavItem("Mapa", Icons.Default.LocationOn),
    // Iconos cambiados porque decía que no existían
    NavItem("Promociones", Icons.Default.ShoppingCart),
    NavItem("ID digital", Icons.Default.Info),
)

/**
 * Composable que renderiza la barra de navegación inferior de la aplicación.
 *
 * Esta barra de navegación muestra los elementos definidos en la lista [navItems]
 * y gestiona el estado de selección del elemento actual. El elemento seleccionado
 * se resalta visualmente con un cambio de color y un fondo circular.
 *
 * La navegación real entre pantallas debe ser implementada en la acción `onClick`
 * de cada `NavigationBarItem`.
 */
@Composable
fun BarraNavegacion(onNavigateToInicio: () -> Unit,
                    onNavigateToMapa: () -> Unit,
                    onNavigateToPromociones: () -> Unit,
                    onNavigateToID: () -> Unit) {
    // Estado para recordar cuál es el elemento seleccionado actualmente. Se inicializa con el primer elemento.
    var selectedItem by remember { mutableStateOf(navItems[0].label) }

    NavigationBar(
        containerColor = Color.Companion.White,
        modifier = Modifier
            // Aplica bordes redondeados solo en las esquinas superiores.
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            // Añade un borde sutil en la parte superior para separar visualmente.
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        // Itera sobre la lista de elementos de navegación para crear cada ítem en la barra.
        navItems.forEach { item ->
            val isSelected = selectedItem == item.label
            NavigationBarItem(
                selected = isSelected,
                onClick = { selectedItem = item.label
                    // Llama a la función de navegación correspondiente al item pulsado.
                    when (item.label) {
                        "Inicio" -> onNavigateToInicio()
                        "Mapa" -> onNavigateToMapa()
                        "Promociones" -> onNavigateToPromociones()
                        "ID digital" -> onNavigateToID()
                    }
                          },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        // Modificador para aplicar un fondo circular si el ítem está seleccionado.
                        modifier = Modifier
                            .run {
                                if (isSelected)
                                // El fondo es de color morado con baja opacidad y forma de círculo.
                                    background(morado.copy(alpha = 0.1f), CircleShape)
                                else
                                    this
                            }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                },
                label = {
                    Text(
                        item.label,
                        fontSize = 10.sp,
                        // El texto se pone en negrita si el ítem está seleccionado.
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                // Personalización de los colores para los estados seleccionado y no seleccionado.
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = morado,
                    selectedTextColor = morado,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    // Se hace transparente el indicador por defecto para usar el fondo circular del ícono como indicador.
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
