import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mx.tec.ptoyectobj.R
import mx.tec.ptoyectobj.viewmodel.AppVM

// Colores
val morado = Color(0xFF38156E)
//val rosa = Color(0x1731CFA) // Rosa
val rosa = Color(red = 243, green = 26, blue = 138, alpha = 255)
val naranja = Color(red = 250, green = 77, blue = 103, alpha = 255)
val blanco = Color(0xFFFFFFFF)

@Composable
fun Inicio(navController: NavController, appVM: AppVM) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(morado)
    ){
        // Formas de fondo
        //Forma rosa inferior de arriba
        Box(
            modifier = Modifier
                .offset(x = (-40).dp, y = (-120).dp) // El offset negativo mueve el óvalo hacia arriba, fuera de la pantalla
                .size(400.dp)
                .clip(CircleShape)
                .background(rosa)

        )
        //Forma naranja superior de arriba
        Box(
            modifier = Modifier
                .offset(x = 5.dp, y = (-190).dp)
                .size(width = 800.dp, height = 400.dp) // Define un tamaño de óvalo (casi un círculo en este caso)
                .clip(CircleShape) // La forma circular crea un óvalo al ajustar el tamaño
                .background(naranja)

        )

        //Forma rosa inferior de abajo
        Box(
            modifier = Modifier
                .offset(x = (-150).dp, y = 700.dp)
                .size(400.dp)
                .clip(CircleShape)
                .background(rosa)

        )
        //Forma naranja superior de abajo
        Box(
            modifier = Modifier
                .offset(x = 20.dp, y = 700.dp)
                .size(400.dp)
                .clip(CircleShape)
                .background(naranja)

        )

        // Líneas blancas decorativas
        // Línea superior
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = 8.dp, y = (-170).dp)
                .size(400.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
        )
        // Línea inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 200.dp)
                .size(400.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
        )

        // Contenido principal (Logo, texto, botones)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Sección superior (Logo y texto "BENEFICIO JOVEN")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), // Padding superior para el logo
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Contenedor circular blanco para el logo 'b'
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // imagen del logo.
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo de Beneficio Joven",
                        modifier = Modifier.size(500.dp)
                    )

                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                            append("BENEFICIO")
                        }
                        append("JOVEN")
                    },
                    color = blanco,
                    fontSize = 20.sp
                )
            }

            // Espacio flexible para empujar el contenido central
            Spacer(modifier = Modifier.weight(1f))

            // Texto "DESBLOQUEA LOS BENEFICIOS"
            Text(
                text = "DESBLOQUEA LOS BENEFICIOS",
                color = blanco,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón "Iniciar sesión"
            Button(
                //Modificar cuando se tenga lo de inicio
                onClick = { /* TODO: Navegar a la pantalla de inicio de sesión */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = blanco),
                shape = RoundedCornerShape(28.dp) // Esquinas redondeadas
            ) {
                Text(
                    text = "Iniciar sesión",
                    color = morado,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Texto "¿Eres nuevo aquí?"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Eres nuevo aquí? ",
                    color = blanco.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                // Usamos un Text simple para "Regístrate" para que parezca un enlace
                // Puedes envolverlo en un ClickableText si necesitas una acción de click
                Text(
                    text = "Regístrate",
                    color = blanco,
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.Underline
                )
            }
            // Si quieres que sea clicable:

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioPreview() {
    Inicio(NavController(LocalContext.current), AppVM())
}