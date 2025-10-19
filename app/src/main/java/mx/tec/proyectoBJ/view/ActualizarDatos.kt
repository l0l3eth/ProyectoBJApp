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
import androidx.compose.material.icons.filled.Edit
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

// --- Modelo de Datos para las Opciones ---
data class UserOption(
    val label: String,
    val hasEditIcon: Boolean,
    val action: () -> Unit = {}
)

@Composable
fun ActualizarDatos( appVM: AppVM,
                     onMenuClick: () -> Unit = {},
    // onBack ahora es para navegación simple, no para un logout completo
                     onBack: () -> Unit = {},
    // onLogoutSuccess nos permite navegar fuera después de que el VM confirme el borrado
                     onLogoutSuccess: () -> Unit = {}
) {
    // 1. Estado para controlar la visibilidad del diálogo de confirmación de salida
    var mostrarDialogoSalida by remember { mutableStateOf(false) }

    // 2. Observamos el estado de carga desde el ViewModel
    val estaBorrando by appVM.estaBorrando.collectAsState()

    // --- NUEVOS ESTADOS PARA LA EDICIÓN ---
    // Estado para saber qué campo estamos editando. Null si no hay ninguno.
    var campoEnEdicion by remember { mutableStateOf<String?>(null) }
    // Estado para almacenar el valor actual del campo que se edita.
    var valorActual by remember { mutableStateOf("") }
    //errores
    val errorMensaje by appVM.errorMensaje.observeAsState()

    // 3. Efecto para reaccionar cuando el usuario ha sido borrado con éxito
    LaunchedEffect(Unit) {
        appVM.borradoExitoso.collect { exito ->
            if (exito) {
                // Si el borrado fue exitoso, navegamos a la pantalla de inicio
                onLogoutSuccess()
            }
        }
    }

    // Obtenemos los datos del usuario logeado desde el ViewModel
    val usuario by appVM.usuarioLogeado.observeAsState()

    // --- MODIFICACIÓN CLAVE: Creamos la lista de opciones con acciones de edición ---
    val userOptions = listOfNotNull(
        usuario?.nombre?.let { nombre ->
            UserOption(
                label = "NOMBRE: $nombre",
                hasEditIcon = true,
                action = {
                    // Al hacer click, guardamos el tipo y el valor actual
                    campoEnEdicion = "nombre"
                    valorActual = nombre
                }
            )
        },
        usuario?.apellidos?.let { apellidos ->
            UserOption(
                label = "APELLIDO: $apellidos",
                hasEditIcon = true,
                action = {
                    campoEnEdicion = "apellidos"
                    valorActual = apellidos
                }
            )
        },
        usuario?.correo?.let { correo ->
            UserOption(
                label = "Correo: $correo",
                hasEditIcon = true,
                action = {
                    campoEnEdicion = "correo"
                    valorActual = correo
                }
            )
        },
        usuario?.id?.let {
            UserOption(
                label = "FOLIO: $it",
                hasEditIcon = false,
                action = {} // No hace nada al hacer click
            )
        },
        UserOption(
            label = "CONTRASEÑA",
            hasEditIcon = false,
            action = { /* TODO: Navegar a la pantalla de cambiar contraseña */ }
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(morado) // Fondo principal morado
    ) {
        // Formas de fondo (sin cambios)
        //Forma rosa inferior de arriba
        Box(
            modifier = Modifier
                .offset(x = (-40).dp, y = (-120).dp)
                .size(400.dp)
                .clip(CircleShape)
                .background(rosa)

        )
        //Forma naranja superior de arriba
        Box(
            modifier = Modifier
                .offset(x = 5.dp, y = (-190).dp)
                .size(width = 800.dp, height = 400.dp)
                .clip(CircleShape)
                .background(naranja)
        )
        //Forma rosa inferior de abajo
        Box(
            modifier = Modifier
                .offset(x = (-150).dp, y = 700.dp)
                .size(400.dp)
                .clip(CircleShape)
                .background(rosa)
        )
        //Forma naranja superior de abajo
        Box(
            modifier = Modifier
                .offset(x = 20.dp, y = 700.dp)
                .size(400.dp)
                .clip(CircleShape)
                .background(naranja)
        )
        // Líneas blancas decorativas (sin cambios)
        // Línea superior
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = 8.dp, y = (-170).dp)
                .size(400.dp)
                .clip(CircleShape)
                .border(1.dp, White, CircleShape)
        )
        // Línea inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 200.dp)
                .size(400.dp)
                .clip(CircleShape)
                .border(1.dp, White, CircleShape)
        )

        // --- Contenido Central (Opciones y Botones) ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 100.dp, bottom = 48.dp) // Ajuste para evitar el header
        ) {
            // Opciones del perfil (Usuario, Correo, Folio, Contraseña)
            items(userOptions.size) { index ->
                UserSettingItem(option = userOptions[index])
            }
            // Espacio de separación
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
            // Opciones de acción (Regresar, Cerrar sesión)
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
                            .clickable(
                                enabled = !estaBorrando, // Deshabilita el click mientras carga
                                onClick = {
                                    mostrarDialogoSalida = true
                                } // Al hacer click, muestra el diálogo de salida
                            )
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Cerrar sesión",
                            color = naranja,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (estaBorrando) {
                            Spacer(modifier = Modifier.width(8.dp))
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = naranja,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                    if (errorMensaje != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = errorMensaje!!, // Usamos !! porque ya comprobamos que no es nulo
                            color = Color(0xFFD32F2F), // Un color rojo para errores
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

        }
    }

    // --- DIÁLOGOS ---

    // 1. Diálogo para confirmar la salida/borrado de cuenta
    if (mostrarDialogoSalida) {
        ConfirmarSalida(
            appVM = appVM,
            onDismissRequest = {
                if (!estaBorrando) {
                    mostrarDialogoSalida = false
                }
            }
        )
    }

    // 2. Diálogo para editar un campo
    if (campoEnEdicion != null) {
        EditDialog(
            valorActual = valorActual,
            onDismiss = {
                // Al cerrar el diálogo, reseteamos el estado de edición
                campoEnEdicion = null
            },
            onSave = { nuevoValor ->
                // Lógica de guardado
                usuario?.let { usr ->
                    usr.id?.let { idSeguro ->
                        val usuarioActualizado = when (campoEnEdicion) {
                            // Al hacer la copia, nos aseguramos de pasar un valor válido para la contraseña
                            "nombre" -> usr.copy(nombre = nuevoValor, contrasena = usr.contrasena ?: "")
                            "apellidos" -> usr.copy(apellidos = nuevoValor, contrasena = usr.contrasena ?: "")
                            "correo" -> usr.copy(correo = nuevoValor, contrasena = usr.contrasena ?: "")
                            else -> usr
                        }
                        appVM.actualizarUsuario(idSeguro, usuarioActualizado)
                    }
                }
                // Cierra el diálogo después de guardar
                campoEnEdicion = null
            }
        )
    }
}

// ---------------------------------------------------------------------
// COMPONENTE DE ITEM DE CONFIGURACIÓN
// ---------------------------------------------------------------------

@Composable
fun UserSettingItem(option: UserOption) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = option.action) // Hace todo el item clicable
            .padding(horizontal = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Etiqueta de la opción
            Text(
                text = option.label,
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Icono de Edición (solo si se especifica)
            if (option.hasEditIcon) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar ${option.label}",
                    tint = White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        // Divisor (Línea)
        HorizontalDivider(thickness = 1.dp, color = White.copy(alpha = 0.3f))
    }
}

// ---------------------------------------------------------------------
// NUEVO COMPONENTE DE DIÁLOGO DE EDICIÓN
// ---------------------------------------------------------------------
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
            Button(onClick = { onSave(nuevoValor) }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text("Cancelar")
            }
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
