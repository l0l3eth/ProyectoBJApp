package mx.tec.proyectoBJ.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import mx.tec.proyectoBJ.R
import mx.tec.proyectoBJ.model.Producto
import mx.tec.proyectoBJ.view.outlinedTextFieldColors
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.ptoyectobj.blanco
import mx.tec.ptoyectobj.morado
import mx.tec.ptoyectobj.naranja
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Un composable que muestra un `OutlinedTextField` estilizado para la entrada de texto.
 * Presenta esquinas redondeadas y un fondo de color `blanco`.
 *
 * @param value El texto actual a mostrar en el campo.
 * @param modifier Modificador para personalizar el layout y la apariencia.
 * @param etiqueta El texto que se muestra como placeholder o etiqueta flotante.
 * @param onValueChange Callback que se invoca cuando el valor del texto cambia.
 * Autores: Estrella Lolbeth Téllez Rivas A01750496
 *          Allan Mauricio Brenes Castro  A01750747
 */
@Composable
fun CampoDeTexto(value: String,
                 modifier: Modifier = Modifier,
                 etiqueta: String,
                 onValueChange: (String) -> Unit) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(etiqueta) },
        shape = RoundedCornerShape(28.dp),
        colors = outlinedTextFieldColors(),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun outlinedTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = blanco.copy(alpha = 0.9f),
    unfocusedContainerColor = blanco.copy(alpha = 0.9f),
    focusedBorderColor = Color.Transparent,
    unfocusedBorderColor = Color.Transparent,
    focusedLabelColor = blanco,
    unfocusedLabelColor = morado.copy(alpha = 0.7f),
    cursorColor = morado
)

/**
 * Un diálogo modal que muestra un selector de fechas (`DatePicker`).
 * Permite al usuario elegir una fecha y devuelve la fecha seleccionada en formato "yyyy-MM-dd".
 *
 * @param onDateSelected Callback que se invoca con la fecha seleccionada como un String.
 * @param onDismiss Callback que se invoca cuando el diálogo se cierra sin seleccionar una fecha.
 * @param menuFecha Controla la visibilidad del diálogo. Si es `true`, el diálogo se muestra.
 */
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
 * Un composable que muestra un botón de forma circular con un icono y un texto opcional debajo.
 * El `onClick` actualmente no tiene una acción definida.
 *
 * @param icono El [ImageVector] a mostrar dentro del botón.
 * @param modifier Modificador para personalizar el layout y la apariencia.
 * @param texto Texto opcional que se muestra debajo del icono.
 * @param tamano El diámetro del botón en `dp`.
 */
@Composable
fun BotonCircular(icono: ImageVector,
                  modifier: Modifier = Modifier,
                  texto: String = "",
                  tamano: Int = 100) {
    Button(onClick = { /* TODO: Implementar acción de clic */ },
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .padding(0.dp)
            .size(tamano.dp)
            .background(White)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            Icon(
                icono,
                contentDescription = null, // Descripción de contenido decorativa
                modifier = modifier
                    .padding(0.dp)
                    .size(50.dp)
            )
            Text(
                texto,
                modifier = modifier
                    .padding(0.dp)
                    .size(55.dp) // Este tamaño podría causar que el texto se corte si es muy largo
            )
        }
    }
}

/**
 * Muestra el logo de la aplicación junto al nombre "BENEFICIO JOVEN" en un tamaño pequeño.
 * Diseñado para cabeceras o espacios reducidos.
 */
@Composable
fun LogoYTextoPequeño() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
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

/**
 * Muestra el logo de la aplicación junto al nombre "BENEFICIO JOVEN" en un tamaño grande.
 * Ideal para pantallas de bienvenida o de inicio de sesión.
 */
@Composable
fun LogoYTextoGrande(){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
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

/**
 * Un `Text` composable estilizado para ser usado como título principal en secciones,
 * como en las pantallas de registro.
 *
 * @param texto El texto a mostrar.
 * @param modifier Modificador para personalizar el layout.
 */
@Composable
fun TextoTitularRegistro(texto: String, modifier: Modifier = Modifier) {
    Text(texto,
        modifier = modifier.padding(16.dp),
        color = blanco,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold)
}

/**
 * Header o parte superior principal de la aplicación, que incluye un saludo al usuario,
 * un botón de menú, la foto de perfil y una barra de búsqueda.
 *
 * @param userName El nombre del usuario a mostrar en el saludo.
 * @param modifier Modificador para personalizar el layout del contenedor principal.
 */
@Composable
fun ParteSuperior(userName: String, modifier: Modifier = Modifier, onClick: () -> Unit,) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(morado)
            .padding(bottom = 16.dp)
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
                    IconButton(onClick = onClick ) {
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
            }
            Spacer(modifier = Modifier.height(24.dp))
            BarraBusqueda(modifier = Modifier.fillMaxWidth())
        }
    }
}

/**
 * Un `OutlinedTextField` estilizado para funcionar como una barra de búsqueda.
 * Tiene fondo blanco, esquinas redondeadas y un icono de búsqueda.
 *
 * @param modifier Modificador para personalizar el layout.
 */
@Composable
fun BarraBusqueda(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = { Text("Busca tu negocio favorito") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
        shape = RoundedCornerShape(28.dp),
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

/**
 * Un `Card` que muestra la información de una promoción.
 * Incluye un título, una descripción y un icono para marcar como favorito.
 * Está estilizada con colores específicos de la marca (`morado` y `naranja`).
 *
 * @param promo El objeto `Promotion` (se asume que es una data class) que contiene los datos a mostrar.
 */
@Composable
fun TarjetasPromocion(promo: Producto) { // Se asume la existencia de data class Promotion(val title: String, val description: String)
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
                .border(2.dp, naranja, RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = promo.nombre.uppercase(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = morado
                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = promo.esta_activo,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.SemiBold,
//                    color = naranja
//                )
            }
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Favorito",
                tint = naranja.copy(alpha = 0.8f),
                modifier = Modifier
                    .size(36.dp)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(naranja.copy(alpha = 0.1f))
            )
        }
    }
}

/**
 * Muestra un diálogo de confirmación para una acción destructiva, como borrar un usuario.
 *
 * @param viewModel La instancia del ViewModel (AppVM) para poder llamar a sus funciones.
 * @param onDismissRequest La acción a ejecutar cuando el diálogo se cierra (ya sea
 *   presionando fuera o usando el botón de cancelar).
 *
 */
@Composable
fun ConfirmarSalida(
    appVM: AppVM, // 1. Recibimos el ViewModel como parámetro
    onDismissRequest: () -> Unit
) {
    // --- Lógica para obtener el ID del usuario ---
    // 2. Observamos el LiveData del usuario logueado para obtener su ID.
    // Usamos ?.id para manejar de forma segura el caso en que no haya usuario (aunque
    // en esta pantalla siempre debería haber uno).
    val usuarioId = appVM.usuarioLogeado.observeAsState().value?.id ?: -1

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = morado // Color de fondo de la tarjeta
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¿ESTÁS SEGURO?",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Esta acción no se puede deshacer.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Botón de Cancelar
                    Button(
                        onClick = onDismissRequest, // Cierra el diálogo
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, Color.White, RoundedCornerShape(50))
                    ) {
                        Text("CANCELAR", color = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    // Botón de Confirmar (Sí, borrar)
                    Button(
                        onClick = {
                            // 3. Llamamos a la función del ViewModel con el ID del usuario
                            if (usuarioId != -1) {
                                appVM.eliminarUsuario(usuarioId)
                            }
                            onDismissRequest() // Cierra el diálogo después de iniciar la acción
                        },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = naranja // Color destacado para la acción
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("SI, TOTALMENTE SEGUR@", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun BarraSuperior(nombreNegocio: String, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* TODO: Abrir Drawer de Navegación */ }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menú",
                tint = White,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Bienvenido $nombreNegocio",
            color = White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun Portada() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp) // Altura del espacio negro
            .background(Black)
    )
}

@Composable
fun BoxScope.Icono() {
    Column(
        modifier = Modifier
            .align(Alignment.Center)
            .offset(y = 100.dp) // Baja los logos para que queden sobre la transición de color
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Placeholder del logo principal del negocio
        Box(
            modifier = Modifier
                .size(80.dp) // Tamaño del logo
                .clip(CircleShape)
                .background(White) // Círculo blanco de fondo
                .border(2.dp, White, CircleShape), // Borde blanco (si fuera necesario)
            contentAlignment = Alignment.Center
        ) {
            // Placeholder para el logo 'JOTUNHEIM'
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red.copy(alpha = 0.5f)) // Simulación del logo interno
            )
            /* Si tuvieras el asset:
                Image(
                    painter = painterResource(id = R.drawable.logo_negocio),
                    contentDescription = "Logo del negocio",
                    modifier = Modifier.fillMaxSize()
                )
                */
        }
    }
}
