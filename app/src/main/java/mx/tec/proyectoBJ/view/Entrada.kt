<<<<<<<< HEAD:app/src/main/java/mx/tec/proyectoBJ/view/view/Entrada.kt
package mx.tec.proyectoBJ.view.view
========
package mx.tec.proyectoBJ.view
>>>>>>>> Registro:app/src/main/java/mx/tec/proyectoBJ/view/Entrada.kt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.proyectoBJ.viewmodel.PantallaSplash
import mx.tec.proyectoBJ.R

@Composable
fun Entrada(navController: NavController, appVM: AppVM)
{
    // 1. Observa el evento de navegación del ViewModel.
    val eventoNavegacion = appVM.NavegarAInicio.collectAsState(initial = null).value

    // 2. Usar LaunchedEffect para reaccionar al evento de navegación
    LaunchedEffect(eventoNavegacion) {
        if (eventoNavegacion == PantallaSplash.NavegarAInicio) {
            // 3. Realizar la navegación al destino (Pantalla 2: Inicio)
            navController.navigate("InicioRoute")
        }
    }

    val morado = Color(0xFF38156E) // Color de fondo morado

    // Un Box para centrar todo el contenido en la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(morado),
        contentAlignment = Alignment.Center
    ) {
        // Un Column para organizar el logo y el texto verticalmente
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Contenedor circular blanco para el logo
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White, shape = CircleShape)
                    .padding(5.dp),
                contentAlignment = Alignment.Center
            ) {
                // imagen del logo.
                Image(
                     painter = painterResource(id = R.drawable.logo),
                     contentDescription = "Logo de Beneficio Joven",
                     modifier = Modifier.size(500.dp)
                 )

            }

            // Espacio vertical entre el logo y el texto
            Spacer(modifier = Modifier.height(20.dp))

            // Texto "BENEFICIO JOVEN"
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("BENEFICIO ")
                    }
                        append("JOVEN")

                },
                color = Color.White,
                fontSize = 35.sp
            )
        }
    }
}
