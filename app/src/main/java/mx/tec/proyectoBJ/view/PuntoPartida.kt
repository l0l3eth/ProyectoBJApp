package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.proyectoBJ.model.Usuario
import mx.tec.ptoyectobj.blanco
import mx.tec.ptoyectobj.morado

@Composable
fun PuntoPartida(onNavigateToUsuario: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(morado)
    ) {
        // --- Contenido de la Pantalla ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            LogoYTextoGrande()

            Spacer(modifier = Modifier.height(48.dp))

            // Título "¿Eres dueño de algún negocio o usuario de beneficio joven?"
            Text(
                text = "¿Eres dueño de algún negocio o usuario de beneficio joven?",
                color = blanco,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Botones para Usuario y Negocio
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center)
            {

                BotonCircular(
                    icono = Icons.Default.AccountCircle,
                    modifier = Modifier.padding(top = 8.dp),
                    texto = "Usuario"
                )

                Spacer(modifier = Modifier.width(20.dp))

                BotonCircular(
                    icono = Icons.Default.ShoppingCart,
                    modifier = Modifier.padding(top = 8.dp),
                    texto = "Negocio"
                )
            }

        }

        Text(
            text = "Regresar",
            color = blanco,
            fontSize = 15.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp)
                .clickable { /* TODO: Regresar a página inicio de sesión */ }
                .align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PuntoPartidaPreview() {
    PuntoPartida( onNavigateToUsuario = { })
}