package mx.tec.ptoyectobj.view

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mx.tec.ptoyectobj.R
import mx.tec.ptoyectobj.viewmodel.AppVM
import mx.tec.ptoyectobj.viewmodel.PantallaSplash

@Composable
fun Entrada(appVM: AppVM = AppVM(),
            navController: NavController){

    val eventoNavegacion = appVM.NavegarAInicio.collectAsState(initial = null)

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

//@Preview(showBackground = true)
//@Composable
//fun EntradaPreview() {
//    Entrada((), NavController(LocalContext.current))
//}