package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.proyectoBJ.model.EstadoDeRegistro
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.ptoyectobj.morado
import mx.tec.ptoyectobj.view.CampoDeTexto
import mx.tec.ptoyectobj.view.LogoYTextoGrande
import mx.tec.ptoyectobj.view.TextoTitular

@Composable
fun IngresoDeDatos(modifier: Modifier = Modifier, viewModel: AppVM = AppVM()) {
    val totalPantallas = 4
    var pantallaActual by remember { mutableStateOf(0) }

    var estadoDeRegistro by remember { mutableStateOf(EstadoDeRegistro()) }

    // This function now updates the central state
    fun onStateChange(newState: EstadoDeRegistro) {
        estadoDeRegistro = newState
    }

    fun cambiarPantalla(pantalla: Int = 0) {
        pantallaActual += pantalla
    }

    Box(
        modifier = modifier.background(morado)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            LogoYTextoGrande()
            TextoTitular("Cuéntanos un poco sobre tí")

            // Pass the current state and the update function to each screen
            DatosPersonales(
                mostrar = pantallaActual == 0,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange, // Pass the function reference
                modifier = modifier.padding(horizontal = 32.dp)
            )
            DatosDeContacto(
                mostrar = pantallaActual == 1,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange
            )
            Direccion(
                mostrar = pantallaActual == 2,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange
            )
            Contrasena(
                mostrar = pantallaActual == 3,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange
            )
            if (pantallaActual == 4) {
                // Here you can add validation before sending
                val isValid = estadoDeRegistro.nombre != null &&
                        estadoDeRegistro.apellido != null &&
                        estadoDeRegistro.correo != null &&
                        estadoDeRegistro.contrasena != null &&
                        estadoDeRegistro.direccion != null &&
                        estadoDeRegistro.curp != null &&
                        estadoDeRegistro.numeroTelefono != null

                if (isValid) {
                    TextoTitular("¡Todo listo! Enviando...")
                    viewModel.enviarUsuario(
                        nombre = estadoDeRegistro.nombre!!,
                        apellido = estadoDeRegistro.apellido!!,
                        correo = estadoDeRegistro.correo!!,
                        contrasena = estadoDeRegistro.contrasena!!,
                        direccion = estadoDeRegistro.direccion!!,
                        curp = estadoDeRegistro.curp!!,
                        numeroTelefono = estadoDeRegistro.numeroTelefono!!
                    )
                } else {
                    TextoTitular("Faltan datos por llenar.")
                    // Optionally, you could show a button to go back
                }
            }
            Botones(pantallaActual = pantallaActual,
                totalPantallas = totalPantallas,
                onPantallaCambia = { cambiarPantalla(it) })
        }
    }
}

@Composable
fun Botones(pantallaActual : Int,
            modifier: Modifier = Modifier,
            totalPantallas: Int,
            onPantallaCambia: (Int) -> Unit = {}) {
    Row {
        if (pantallaActual > 0) {
            TextButton(
                onClick = { onPantallaCambia(-1) }
            ) {
                Text(
                    "Regresar",
                    textAlign = TextAlign.Center,
                    style = TextStyle(color = blanco)
                )
            }
        }
        Spacer(modifier = modifier.weight(0.8f))
        if (pantallaActual < totalPantallas) {
            Button(
                onClick = { onPantallaCambia(1) }
            ) {
                Text(
                    "Siguiente",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun Contrasena(mostrar: Boolean = false,
               state: EstadoDeRegistro,
               onStateChange: (EstadoDeRegistro) -> Unit = {}) {
    if (mostrar) {
        TextoTitular("Crea una contraseña.")
        // Resaltar los requerimientos de seguridad de contraseña al usuario
        CampoDeTexto(label = "Contraseña",
            value = state.contrasena ?: "",
            onValueChange = { onStateChange(state.copy(contrasena = it)) })
        // CampoDeTexto("Confirmar contraseña")
    }
}

@Composable
fun DatosDeContacto(mostrar: Boolean = true,
                    state: EstadoDeRegistro,
                    onStateChange: (EstadoDeRegistro) -> Unit = {}) {
    if (mostrar) {
        CampoDeTexto(label = "Correo electrónico",
            value = state.correo ?: "",
            onValueChange = { onStateChange(state.copy(correo = it)) })
        CampoDeTexto(label = "Número de teléfono",
            value = state.numeroTelefono ?: "",
            onValueChange = { onStateChange(state.copy(numeroTelefono = it)) })
    }
}

@Composable
fun Direccion(mostrar: Boolean = false,
              state: EstadoDeRegistro,
              onStateChange: (EstadoDeRegistro) -> Unit = {}) {
    if (mostrar) {
        TextoTitular("¿En dónde vives?")
        TextoTitular("Sólo se aceptan direcciones de Atizapán.")
        CampoDeTexto(label = "Calle, número y colonia o fraccionamiento",
            value = state.direccion ?: "",
            onValueChange = { onStateChange(state.copy(direccion = it)) })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatosPersonales(modifier: Modifier = Modifier,
                    mostrar: Boolean = true,
                    state: EstadoDeRegistro,
                    onStateChange: (EstadoDeRegistro) -> Unit = {}) {
//    var menuFecha by remember { mutableStateOf(false) }
//    var nombres by remember { mutableStateOf("") }
//    var apellidos by remember { mutableStateOf("") }
//    var fechaNacimiento by remember { mutableStateOf("") }
//    var sexo by remember { mutableStateOf("") }

    if (mostrar) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            CampoDeTexto(label = "Nombre(s)",
                value = state.nombre ?: "",
                onValueChange = { onStateChange(state.copy(nombre = it)) })
            CampoDeTexto(label = "Apellidos",
                value = state.apellido ?: "",
                onValueChange = { onStateChange(state.copy(apellido = it)) })
            CampoDeTexto(label = "CURP",
                value = state.curp ?: "",
                onValueChange = { onStateChange(state.copy(curp = it)) })
//            Button(onClick = { menuFecha = true }) {
//                Text(fechaNacimiento.ifEmpty { "Fecha de nacimiento" })
//            }
//            DatePickerModal(onDateSelected = { onStateChange(state.copy(fechaNacimiento = it)) },
//                onDismiss = { menuFecha = false },
//                menuFecha = menuFecha)
        }
    }

}
//@Composable
//fun MenuSexos(mostrar: Boolean = false) {
//    var expanded by remember { mutableStateOf(false) }
//    if (mostrar) {
//        Box(
//            modifier = Modifier
//                .padding(16.dp)
//        ) {
//            IconButton(onClick = { expanded = !expanded }) {
//                Row {
//                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More options")
//                    Text("Género")
//                }
//            }
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }
//            ) {
//                DropdownMenuItem(
//                    text = { Text("Hombre") },
//                    onClick = { /* Do something... */ }
//                )
//                DropdownMenuItem(
//                    text = { Text("Mujer") },
//                    onClick = { /* Do something... */ }
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun MenuEstado() {
//    var expanded by remember { mutableStateOf(false) }
//    Box(
//        modifier = Modifier
//            .padding(16.dp)
//    ) {
//        IconButton(onClick = { expanded = !expanded }) {
//            Row {
//                Text("Estado")
//            }
//        }
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            DropdownMenuItem(
//                text = { Text("Estado de México") },
//                onClick = { /* Do something... */ }
//            )
//            DropdownMenuItem(
//                text = { Text("Ciudad de México") },
//                onClick = { /* Do something... */ }
//            )
//        }
//    }
//}


@Preview(showBackground = true)
@Composable
fun IngresoDeDatosPreview() {
    IngresoDeDatos()
}
