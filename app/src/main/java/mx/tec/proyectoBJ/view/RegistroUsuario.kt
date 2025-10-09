package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            LogoYTextoPequeño()
            TextoTitular("Cuéntanos un poco sobre tí")
            Formulario(modifier = modifier.padding(horizontal = 32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Formulario(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        CampoDeTexto("Nombre(s)")
        CampoDeTexto("Apellido Paterno")
        CampoDeTexto("Apellido Materno")
        DatePickerModal(onDateSelected = {}, onDismiss = {})
        MenuGeneros()

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
            Icon(Icons.Default.MoreVert, contentDescription = "More options")
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
