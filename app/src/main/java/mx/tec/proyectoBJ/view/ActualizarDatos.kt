package mx.tec.proyectoBJ.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.ptoyectobj.morado
import mx.tec.ptoyectobj.naranja
import mx.tec.ptoyectobj.rosa

/**
 * Modelo de datos que representa una opción configurable en la pantalla de perfil del usuario.
 *
 * @property label El texto que se muestra en la interfaz para esta opción (e.g., "NOMBRE: Juan").
 * @property hasEditIcon Booleano que indica si se debe mostrar un ícono de edición junto a la opción.
 * @property action La función lambda que se ejecuta cuando el usuario presiona sobre esta opción.
 */
data class UserOption(
    val label: String,
    val hasEditIcon: Boolean,
    val action: () -> Unit = {}
)

/**
 * Pantalla principal para la gestión de datos del perfil de usuario.
 *
 * Permite al usuario ver su información, iniciar el proceso de edición de datos
 * como nombre, apellidos y correo, y gestionar su sesión (regresar o cerrar sesión).
 * Utiliza diálogos para la edición de campos y para confirmar el cierre de sesión.
 *
 * @param appVM La instancia del ViewModel [AppVM] que proporciona los datos del usuario y la lógica de negocio.
 * @param onBack Función lambda para navegar hacia atrás, fuera de esta pantalla.
 * @param onLogoutSuccess Función lambda que se invoca cuando el cierre de sesión (borrado) es exitoso,
 *                        permitiendo navegar a la pantalla de inicio.
 */
@Composable
fun ActualizarDatos( appVM: AppVM,
                     onBack: () -> Unit = {},
                     onLogoutSuccess: () -> Unit = {}
) {
    // Estado para controlar la visibilidad del diálogo de confirmación de salida.
    var mostrarDialogoSalida by remember { mutableStateOf(false) }

    // Observa el estado de carga desde el ViewModel para mostrar un indicador visual.
    val estaBorrando by appVM.estaBorrando.collectAsState()

    // Estados para gestionar la edición de un campo específico.
    var campoEnEdicion by remember { mutableStateOf<String?>(null) } // Qué campo se edita (e.g., "nombre")
    var valorActual by remember { mutableStateOf("") } // Valor actual del campo a editar
    val errorMensaje by appVM.errorMensaje.observeAsState()

    // Efecto que se ejecuta una sola vez para observar el flujo de borrado exitoso.
    LaunchedEffect(Unit) {
        appVM.borradoExitoso.collect { exito ->
            if (exito) {
                // Si el borrado fue exitoso, se invoca el callback para navegar fuera.
                onLogoutSuccess()
            }
        }
    }

    // Obtiene los datos del usuario logueado desde el ViewModel.
    val usuario by appVM.usuarioLogeado.observeAsState()

    // Lista dinámica de opciones a mostrar, basada en los datos del usuario.
    val userOptions = listOfNotNull(
        usuario?.nombre?.let { nombre ->
            UserOption(label = "NOMBRE: $nombre", hasEditIcon = true, action = {
                campoEnEdicion = "nombre"
                valorActual = nombre
            })
        },
        usuario?.apellidos?.let { apellidos ->
            UserOption(label = "APELLIDO: $apellidos", hasEditIcon = true, action = {
                campoEnEdicion = "apellidos"
                valorActual = apellidos
            })
        },
        usuario?.correo?.let { correo ->
            UserOption(label = "Correo: $correo", hasEditIcon = true, action = {
                campoEnEdicion = "correo"
                valorActual = correo
            })
        },
        usuario?.id?.let {
            UserOption(label = "FOLIO: $it", hasEditIcon = false)
        },
        UserOption(label = "CONTRASEÑA", hasEditIcon = false)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(morado)
    ) {
        // --- Decoraciones de fondo (formas circulares y bordes) ---
        Box(modifier = Modifier.offset(x = (-40).dp, y = (-120).dp).size(400.dp).clip(CircleShape).background(rosa))
        Box(modifier = Modifier.offset(x = 5.dp, y = (-190).dp).size(width = 800.dp, height = 400.dp).clip(CircleShape).background(naranja))
        Box(modifier = Modifier.offset(x = (-150).dp, y = 700.dp).size(400.dp).clip(CircleShape).background(rosa))
        Box(modifier = Modifier.offset(x = 20.dp, y = 700.dp).size(400.dp).clip(CircleShape).background(naranja))
        Box(modifier = Modifier.align(Alignment.TopCenter).offset(x = 8.dp, y = (-170).dp).size(400.dp).clip(CircleShape).border(1.dp, White, CircleShape))
        Box(modifier = Modifier.align(Alignment.BottomEnd).offset(x = 100.dp, y = 200.dp).size(400.dp).clip(CircleShape).border(1.dp, White, CircleShape))

        // --- Contenido principal con scroll ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 100.dp, bottom = 48.dp)
        ) {
            // Renderiza la lista de opciones de perfil.
            items(userOptions.size) { index ->
                UserSettingItem(option = userOptions[index])
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Renderiza las acciones de "Regresar" y "Cerrar sesión".
            item {
                Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                    Text(
                        text = "Regresar",
                        color = White.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable(onClick = onBack)
                            .padding(vertical = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable(enabled = !estaBorrando) { mostrarDialogoSalida = true }
                            .padding(vertical = 8.dp)
                    ) {
                        Text(text = "Cerrar sesión", color = naranja, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        if (estaBorrando) {
                            Spacer(modifier = Modifier.width(8.dp))
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = naranja, strokeWidth = 2.dp)
                        }
                    }
                    // Muestra un mensaje de error si existe uno.
                    if (errorMensaje != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = errorMensaje!!, color = Color(0xFFD32F2F), fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }

    // --- DIÁLOGOS MODALES ---

    // Diálogo para confirmar el cierre de sesión.
    if (mostrarDialogoSalida) {
        ConfirmarSalida(
            appVM = appVM,
            onDismissRequest = { if (!estaBorrando) mostrarDialogoSalida = false }
        )
    }

    // Diálogo para editar un campo del usuario. Se muestra si `campoEnEdicion` no es nulo.
    if (campoEnEdicion != null) {
        EditDialog(
            valorActual = valorActual,
            onDismiss = { campoEnEdicion = null },
            onSave = { nuevoValor ->
                usuario?.let { usr ->
                    usr.id?.let { idSeguro ->
                        // Crea una copia del usuario con el campo modificado.
                        val usuarioActualizado = when (campoEnEdicion) {
                            "nombre" -> usr.copy(nombre = nuevoValor, contrasena = usr.contrasena ?: "")
                            "apellidos" -> usr.copy(apellidos = nuevoValor, contrasena = usr.contrasena ?: "")
                            "correo" -> usr.copy(correo = nuevoValor, contrasena = usr.contrasena ?: "")
                            else -> usr
                        }
                        // Llama al ViewModel para ejecutar la actualización.
                        appVM.actualizarUsuario(idSeguro, usuarioActualizado)
                    }
                }
                campoEnEdicion = null // Cierra el diálogo.
            }
        )
    }
}

/**
 * Composable que representa un único ítem en la lista de configuraciones del perfil.
 * Muestra una etiqueta y, opcionalmente, un ícono de edición.
 *
 * @param option El objeto [UserOption] que contiene los datos y la acción para este ítem.
 */
@Composable
fun UserSettingItem(option: UserOption) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = option.action) // Indica que el ítem es clickeable.
            .padding(horizontal = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = option.label, color = White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            if (option.hasEditIcon) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar ${option.label}", tint = White, modifier = Modifier.size(20.dp))
            }
        }
        HorizontalDivider(thickness = 1.dp, color = White.copy(alpha = 0.3f))
    }
}

/**
 * Diálogo modal para editar un campo de texto.
 *
 * Presenta un campo de texto para que el usuario ingrese un nuevo valor y
 * botones para guardar o cancelar la operación.
 *
 * @param valorActual El valor inicial que se mostrará en el campo de texto.
 * @param onDismiss Función lambda que se invoca cuando el usuario cierra el diálogo (e.g., tocando fuera o cancelando).
 * @param onSave Función lambda que se invoca con el nuevo valor cuando el usuario presiona "Guardar".
 */
@Composable
fun EditDialog(
    valorActual: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var nuevoValor by remember { mutableStateOf(valorActual) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Actualizar Información") },
        text = {
            OutlinedTextField(
                value = nuevoValor,
                onValueChange = { nuevoValor = it },
                label = { Text("Nuevo valor") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = { onSave(nuevoValor) }) { Text("Guardar") }
        },
        dismissButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)) { Text("Cancelar") }
        }
    )
}

// ---------------------------------------------------------------------
// PREVIEW
// ---------------------------------------------------------------------

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun UserSettingsScreenPreview() {
    ActualizarDatos( appVM = AppVM())
}
