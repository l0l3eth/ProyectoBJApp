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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import mx.tec.ptoyectobj.morado
import mx.tec.ptoyectobj.view.CampoDeTexto
import mx.tec.ptoyectobj.view.DatePickerModal
import mx.tec.ptoyectobj.view.LogoYTextoGrande
import mx.tec.ptoyectobj.view.LogoYTextoPequeño
import mx.tec.ptoyectobj.view.TextoTitular

@Composable
fun IngresoDeDatos(modifier: Modifier = Modifier) {
    var pantallaActual by remember { mutableStateOf(0) }
    var mostrarFormulario by remember { mutableStateOf(true) }
    var mostrarCorreo by remember { mutableStateOf(false) }

    fun cambiarPantalla(pantalla: Int = 0) {
        pantallaActual += pantalla
        if (pantalla == 0) {
            mostrarFormulario = true
            mostrarCorreo = false
        } else if (pantalla == 1) {
            mostrarFormulario = false
            mostrarCorreo = true
        } else {
            mostrarFormulario = false
            mostrarCorreo = false
        }
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
            Formulario(mostrarFormulario = mostrarFormulario,
                modifier = modifier.padding(horizontal = 32.dp))
            Correo(mostrarCorreo = mostrarCorreo)
            Botones(cambiar = { cambiarPantalla(it) })
        }
    }
}

@Composable
fun Botones(modifier: Modifier = Modifier, cambiar: (Int) -> Unit = {}) {
    Row {
        TextButton(
            onClick = { cambiar(1) }
        ) {
            Text(
                "Regresar",
                textAlign = TextAlign.Center,
                style = TextStyle(color = blanco)
            )
        }
        Spacer(modifier = modifier.weight(0.8f))
        Button(
            onClick = { cambiar(-1) }
        ) {
            Text(
                "Siguiente",
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Formulario(mostrarFormulario: Boolean = true, modifier: Modifier = Modifier) {
    var menuFecha by remember { mutableStateOf(false) }
    if (mostrarFormulario) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            CampoDeTexto("Nombre(s)")
            CampoDeTexto("Apellido Paterno")
            CampoDeTexto("Apellido Materno")
            Button(onClick = { menuFecha = !menuFecha }) {
                Text("Fecha de nacimiento")
            }
            DatePickerModal(
                onDateSelected = {},
                onDismiss = { menuFecha = !menuFecha },
                menuFecha = menuFecha
            )
            MenuGeneros()

        }
    }

}

@Composable
fun Correo(mostrarCorreo: Boolean = true, modifier: Modifier = Modifier) {
    if (mostrarCorreo) {
        CampoDeTexto("Correo electrónico")
    }
}

@Composable
fun MenuGeneros() {
    var expanded by remember { mutableStateOf(false) }
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
            DropdownMenuItem(
                text = { Text("No binario") },
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
