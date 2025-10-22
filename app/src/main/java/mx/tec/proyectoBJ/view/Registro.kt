package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.proyectoBJ.morado

/**
 * Pantalla de registro que permite al usuario elegir si se registra como "Usuario" o como "Negocio".
 *
 * @param onNavigateToRegistroUsuario Lambda que se invoca cuando el usuario hace clic en la opción de registro de usuario.
 * @param onNavogateToSolicitudNegocio Lambda que se invoca cuando el usuario hace clic en la opción de registro de negocio.
 * @param modifier Modificador de Compose para personalizar la apariencia y el comportamiento del componente.
 */
@Composable
fun Registro(onNavigateToRegistroUsuario: () -> Unit, onNavogateToSolicitudNegocio: () -> Unit, modifier: Modifier = Modifier) {
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
            LogoYTextoGrande()
            Spacer(modifier = modifier.size(64.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TextoTitularRegistro("¿Eres dueño de algún negocio o eres" +
                        " usuario de Beneficio Joven?")
                Spacer(modifier = modifier.size(32.dp))
                Row(
                    modifier = modifier.fillMaxHeight()
                ) {
                    BotonCircular(
                        icono = Icons.Outlined.AccountCircle,
                        modifier = modifier.clickable { onNavigateToRegistroUsuario() },
                        texto = "Usuario",
                        tamano = 150,
                    )
                    Spacer(modifier = modifier.size(16.dp))
                    BotonCircular(
                        icono = Icons.Outlined.ShoppingCart,
                        modifier = modifier.clickable{ onNavogateToSolicitudNegocio() },
                        texto = "Negocio",
                        tamano = 150
                    )
                }
            }
        }
    }
}

/**
 * Función de previsualización para el composable [Registro].
 * Muestra cómo se verá la pantalla de registro en el editor de Android Studio.
 */
@Preview(showBackground = true)
@Composable
fun RegistroPreview() {
    Registro( onNavigateToRegistroUsuario = { }, onNavogateToSolicitudNegocio = { })
}
