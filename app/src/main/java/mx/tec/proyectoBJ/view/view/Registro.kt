package mx.tec.proyectoBJ.view.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import blanco
import morado

@Composable
fun Registro(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(morado)
    ){
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            LogoYTexto()
            Spacer(modifier = modifier.size(64.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TextoTitular("¿Eres dueño de algún negocio o eres" +
                        " usuario de Beneficio Joven?")
                Spacer(modifier = modifier.size(32.dp))
                Row(
                    modifier = modifier.fillMaxHeight()
                ) {
                    BotonCircular(
                        icono = Icons.Outlined.AccountCircle,
                        modifier = modifier,
                        texto = "Usuario",
                        tamano = 150,
                    )
                    Spacer(modifier = modifier.size(16.dp))
                    BotonCircular(
                        icono = Icons.Outlined.ShoppingCart,
                        modifier = modifier,
                        texto = "Negocio",
                        tamano = 150
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistroPreview() {
    Registro()
}