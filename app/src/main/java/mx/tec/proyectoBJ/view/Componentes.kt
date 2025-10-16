package mx.tec.proyectoBJ.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.proyectoBJ.R
import mx.tec.ptoyectobj.blanco
import mx.tec.ptoyectobj.morado
import mx.tec.ptoyectobj.naranja
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Un composable que muestra un `OutlinedTextField` estilizado con un borde redondeado.
 * Este componente está diseñado para la entrada de texto dentro de la aplicación.
 *
 * @param value El texto actual que se mostrará en el campo de texto.
 * @param modifier El modificador que se aplicará al `OutlinedTextField`. Por defecto es `Modifier`.
 * @param etiqueta El texto que se mostrará como etiqueta para el campo de texto.
 * @param onValueChange Una función callback que se activa cuando el usuario modifica el texto en el campo. Proporciona el nuevo valor de texto como un `String`.
 */
@Composable
fun CampoDeTexto(value: String,
                 modifier: Modifier = Modifier,
                 etiqueta: String,
                 onValueChange: (String) -> Unit) {
    // 1. You need a state to hold the text field's value.

    OutlinedTextField(
        // 2. The `value` parameter expects the current string to display.
        value = value,
        // 3. The `onValueChange` lambda gives you the new string when the user types.
        //    You must update your state here.
        onValueChange = onValueChange,
        // The label parameter was already correct!
        label = { Text(etiqueta) },
        modifier = modifier
            .border(
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(1.dp, Color.Black)
            )
            .background(color = blanco,
                shape = RoundedCornerShape(32.dp)), // It's good practice to apply the modifier.
        shape = RoundedCornerShape(32.dp)
    )
}

// Guardado por si se utiliza para escoger fecha.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    menuFecha: Boolean = false
) {
    val datePickerState = rememberDatePickerState()
    if (menuFecha) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Format the date to YYYY-MM-DD
                        val sdf = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                        val formattedDate = sdf.format(Date(millis))
                        onDateSelected(formattedDate)
                    }
                    onDismiss()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

/**
 * Un composable que muestra un botón circular con un icono y un texto opcional debajo de él.
 * El botón tiene una forma de círculo y organiza su contenido (icono y texto) en una columna vertical centrada.
 *
 * @param icono El `ImageVector` que se mostrará como el icono principal dentro del botón.
 * @param modifier El modificador que se aplicará al `Button`. Por defecto es `Modifier`.
 * @param texto El texto opcional que se mostrará debajo del icono. Por defecto es una cadena vacía.
 * @param tamano El tamaño (ancho y alto) del botón en dp. Por defecto es 100.
 */
@Composable
fun BotonCircular(icono: ImageVector,
                  modifier: Modifier = Modifier,
                  texto: String = "",
                  tamano: Int = 100) {
    Button(onClick = {},
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .padding(0.dp)
            .size(tamano.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            Icon(
                icono,
                contentDescription = null,
                modifier = modifier
                    .padding(0.dp)
                    .size(50.dp)
            )
            Text(
                texto,
                modifier = modifier
                    .padding(0.dp)
                    .size(55.dp)
            )
        }
    }
}

@Composable
fun LogoYTextoPequeño() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp), // Padding superior para el logo
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Contenedor circular blanco para el logo 'b'
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            //logo de beneficio
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de Beneficio Joven",
                modifier = Modifier.size(55.dp)
            )

        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("BENEFICIO")
                }
                    append("JOVEN")
            },
            color = blanco,
            fontSize = 20.sp,
        )
    }
}

@Composable
fun LogoYTextoGrande(){
    // Logo y texto "BENEFICIO JOVEN" (Centrado en la parte superior)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Contenedor circular blanco para el logo 'b'
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            //logo de beneficio
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de Beneficio Joven",
                modifier = Modifier.size(75.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("BENEFICIO")
                }
                append("JOVEN")
            },
            color = blanco,
            fontSize = 24.sp
        )
    }
}

@Composable
fun TextoTitularRegistro(texto: String, modifier: Modifier = Modifier) {
    Text(texto,
        modifier = modifier.padding(16.dp),
        color = blanco,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold)
}

// ---------------------------------------------------------------------
// 1. BARRA SUPERIOR (HEADER)
// ---------------------------------------------------------------------

@Composable
fun ParteSuperior(userName: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(morado)
            .padding(bottom = 16.dp) // Padding dentro del morado
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
            // Fila del Título y Foto de Perfil
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Icono de Menú y Título
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { /* TODO: Abrir Drawer de Navegación */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = White,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Bienvenido $userName",
                            color = White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Foto de Perfil
                FotoPerfil(
                    // Aquí se usaría un recurso de imagen real (R.drawable.profile_pic)
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Barra de Búsqueda
            BarraBusqueda(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun FotoPerfil(modifier: Modifier = Modifier) {
    // Placeholder para la imagen de perfil (usando un Box circular)
    Box(
        modifier = modifier
            .clip(CircleShape)
            // Se puede simular la foto con un color o un icono
            .background(Color.LightGray)
            .border(2.dp, White, CircleShape)
    ) {
        // Si tuvieras un recurso de imagen, lo usarías aquí:
        /*
        Image(
            painter = painterResource(id = R.drawable.tu_foto_perfil),
            contentDescription = "Foto de perfil",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        */
    }
}

@Composable
fun BarraBusqueda(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = { Text("Busca tu negocio favorito") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        shape = RoundedCornerShape(28.dp), // Esquinas muy redondeadas
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = White,
            unfocusedContainerColor = White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedLeadingIconColor = morado,
            unfocusedLeadingIconColor = morado,
            cursorColor = morado
        ),
        modifier = modifier.height(56.dp)
    )
}

// ---------------------------------------------------------------------
// 2. TARJETAS DE PROMOCIÓN
// ---------------------------------------------------------------------

@Composable
fun TarjetasPromocion(promo: Promotion) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, naranja, RoundedCornerShape(12.dp)) // Borde punteado simulado
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = promo.title.uppercase(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = morado
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = promo.description,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = naranja
                )
            }

            // Icono de corazón (Favorito)
            Icon(
                imageVector = Icons.Default.Star, // Usamos Star como sustituto de corazón simple
                contentDescription = "Favorito",
                tint = naranja.copy(alpha = 0.8f),
                modifier = Modifier
                    .size(36.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(naranja.copy(alpha = 0.1f)) // Fondo sutil para el corazón
            )
        }
    }
}

// ---------------------------------------------------------------------
// 3. BARRA DE NAVEGACIÓN INFERIOR
// ---------------------------------------------------------------------

data class NavItem(val label: String, val icon: ImageVector)

val navItems = listOf(
    NavItem("Inicio", Icons.Default.Home),
    NavItem("Mapa", Icons.Default.LocationOn),
    NavItem("Promociones", Icons.Default.ShoppingBag),
    NavItem("ID digital", Icons.Default.CreditCard),
)

@Composable
fun BarraNavegacion() {
    var selectedItem by remember { mutableStateOf(navItems[0].label) }

    NavigationBar(
        containerColor = White,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        navItems.forEach { item ->
            val isSelected = selectedItem == item.label
            NavigationBarItem(
                selected = isSelected,
                onClick = { selectedItem = item.label /* TODO: Navegación real */ },
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label,
                        // El icono se ve envuelto en un círculo blanco si está seleccionado
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
                label = {
                    Text(
                        item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = morado,
                    selectedTextColor = morado,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent // El indicador es transparente ya que el círculo es el ícono
                )
            )
        }
    }
}
