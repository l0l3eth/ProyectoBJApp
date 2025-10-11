package mx.tec.ptoyectobj.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.proyectoBJ.view.BotonCircular
import mx.tec.proyectoBJ.view.LogoYTextoPequeño

@Composable
fun Registro(modifier: Modifier = Modifier) {
    Column() {
        LogoYTextoPequeño()
        Text("¿Eres dueño de algún negocio o eres" +
                " usuario de Beneficio Joven?",
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxSize()
                .size(60.dp))
        Row {
            BotonCircular(
                icono = Icons.Outlined.AccountCircle,
                modifier = modifier,
                texto = "Usuario"
            )
            BotonCircular(
                icono = Icons.Outlined.ShoppingCart,
                modifier = modifier,
                texto = "Negocio"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroPreview() {
    Registro()
}