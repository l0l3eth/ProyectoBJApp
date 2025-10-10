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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import mx.tec.proyectoBJ.model.Genero
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.ptoyectobj.morado
import mx.tec.ptoyectobj.view.CampoDeTexto
import mx.tec.ptoyectobj.view.DatePickerModal
import mx.tec.ptoyectobj.view.LogoYTextoGrande
import mx.tec.ptoyectobj.view.TextoTitular

@Composable
fun IngresoDeDatos(viewModel: AppVM = AppVM(), modifier: Modifier = Modifier) {
    var pantallaActual by remember { mutableStateOf(0) }

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
            NombreYCurp(mostrarFormulario = pantallaActual == 0,
                modifier = modifier.padding(horizontal = 32.dp))
            Correo(mostrarCorreo = pantallaActual == 1)
            Direccion(mostrarDireccion = pantallaActual == 2)
            Contrasena(mostrar = pantallaActual == 3)
            Enviado(mostrar = pantallaActual == 4) {nombre, apellido, correo, contrasena, direccion, fechaNacimiento, curp, numeroTelefono, genero ->
                viewModel.enviarUsuario(
                    nombre = nombre,
                    apellido = apellido,
                    correo = correo,
                    contrasena = contrasena,
                    direccion = direccion,
                    fechaNacimiento = fechaNacimiento,
                    curp = curp,
                    numeroTelefono = numeroTelefono,
                    sexo = genero)
            }
            Botones(cambiar = { cambiarPantalla(it) })
        }
    }
}

@Composable
fun Botones(modifier: Modifier = Modifier, cambiar: (Int) -> Unit = {}) {
    Row {
        TextButton(
            onClick = { cambiar(-1) }
        ) {
            Text(
                "Regresar",
                textAlign = TextAlign.Center,
                style = TextStyle(color = blanco)
            )
        }
        Spacer(modifier = modifier.weight(0.8f))
        Button(
            onClick = { cambiar(1) }
        ) {
            Text(
                "Siguiente",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun Contrasena(mostrar: Boolean = false, modifier: Modifier = Modifier) {
    if (mostrar) {
        CampoDeTexto("Contraseña")
        CampoDeTexto("Confirmar contraseña")
    }
}

@Composable
fun Correo(mostrarCorreo: Boolean = true, modifier: Modifier = Modifier) {
    if (mostrarCorreo) {
        CampoDeTexto("Correo electrónico")
    }
}

@Composable
fun Direccion(mostrarDireccion: Boolean = false, modifier: Modifier = Modifier) {
    if (mostrarDireccion) {
        CampoDeTexto("Calle y número")
        CampoDeTexto("Colonia o Fraccionamiento")
        CampoDeTexto("Ciudad")
        CampoDeTexto("CP")
    }
}

@Composable
fun Enviado(mostrar: Boolean = false,
            modifier: Modifier = Modifier,
            enviar: (nombre: String,
                     apellido: String,
                     correo: String,
                     contrasena: String,
                     direccion: String,
                     curp: String,
                     fechaNacimiento: String,
                     numeroTelefono: String,
                     genero: Genero) -> Unit =
                         { nombre, apellido, correo, contrasena, direccion, curp, fechaNacimiento, numeroTelefono, sexo -> }
) {
    if (mostrar) {
        TextoTitular("Enviado")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NombreYCurp(mostrarFormulario: Boolean = true, modifier: Modifier = Modifier) {
    var menuFecha by remember { mutableStateOf(false) }
    var nombres by remember { mutableStateOf("") }
    var apellidoPat by remember { mutableStateOf("") }
    var apellidoMat by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }

    if (mostrarFormulario) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            CampoDeTexto("Nombre(s)") { nombres = it }
            CampoDeTexto("Apellido Paterno")
            CampoDeTexto("Apellido Materno")
            Button(onClick = { menuFecha = true }) {
                Text(fechaNacimiento.ifEmpty { "Fecha de nacimiento" })
            }
            DatePickerModal(onDateSelected = {},
                onDismiss = { menuFecha = false },
                menuFecha = menuFecha)
            MenuSexos(mostrar = menuFecha)

        }
    }

}
@Composable
fun MenuSexos(mostrar: Boolean = false) {
    var expanded by remember { mutableStateOf(false) }
    if (mostrar) {
        Box(
            modifier = Modifier
                .padding(16.dp)
        ) {
            IconButton(onClick = { expanded = !expanded }) {
                Row {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More options")
                    Text("Género")
                }
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Hombre") },
                    onClick = { /* Do something... */ }
                )
                DropdownMenuItem(
                    text = { Text("Mujer") },
                    onClick = { /* Do something... */ }
                )
            }
        }
    }
}

@Composable
fun MenuEstado() {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Row {
                Text("Estado")
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Estado de México") },
                onClick = { /* Do something... */ }
            )
            DropdownMenuItem(
                text = { Text("Ciudad de México") },
                onClick = { /* Do something... */ }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun IngresoDeDatosPreview() {
    IngresoDeDatos()
}
