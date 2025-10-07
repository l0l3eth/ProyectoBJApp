package mx.tec.ptoyectobj.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun Registro(modifier: Modifier = Modifier) {
    Column() {
        LogoYTexto()
        Text("¿Eres dueño de algún negocio o eres" +
                " usuario de Beneficio Joven?",
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxSize()
                .size(60.dp))
        Row {
            BotonCircular(icono = Icons.Outlined.AccountCircle,
                modifier = modifier,
                texto = "Usuario")
            BotonCircular(icono = Icons.Outlined.ShoppingCart,
                modifier = modifier,
                texto = "Negocio")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroPreview() {
    Registro()
}