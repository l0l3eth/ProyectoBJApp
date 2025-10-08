package mx.tec.ptoyectobj.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.navigation.NavController
import mx.tec.ptoyectobj.blanco
import mx.tec.ptoyectobj.morado

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
                Text(
                    "¿Eres dueño de algún negocio o eres" +
                            " usuario de Beneficio Joven?",
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 64.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = blanco
                )
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