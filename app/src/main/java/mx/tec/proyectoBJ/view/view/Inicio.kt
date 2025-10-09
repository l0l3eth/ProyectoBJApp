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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import mx.tec.proyectoBJ.viewmodel.AppVM

// Colores
val morado = Color(0xFF38156E)
val rosa = Color(24326138) // Rosa
val naranja = Color(25077103) // Naranja
val blanco = Color(0xFFFFFFFF)

@Composable
fun Inicio() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(morado)
    ){
        // Formas de fondo
        //Forma rosa inferior
        Box(
            modifier = Modifier
                //.align(Alignment.TopCenter)
                .offset(x = (-40).dp, y = (-120).dp) // El offset negativo mueve el óvalo hacia arriba, fuera de la pantalla
                .size(400.dp) // Define un tamaño de óvalo (casi un círculo en este caso)
                .clip(CircleShape) // La forma circular crea un óvalo al ajustar el tamaño
                .background(rosa)

        )
        // Líneas blancas decorativas
        // Línea superior
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = 8.dp, y = (-170).dp) // El offset negativo mueve el óvalo hacia arriba, fuera de la pantalla
                .size(400.dp) // Define un tamaño de óvalo (casi un círculo en este caso)
                .clip(CircleShape) // La forma circular crea un óvalo al ajustar el tamaño
                //.background(Color.White.copy(alpha = 0.5f))
                .border(1.dp, Color.White, CircleShape)
        )
        // Línea inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 200.dp)
                .size(400.dp)
                .clip(CircleShape)
                //.background(Color.White.copy(alpha = 0.25f))
                .border(1.dp, Color.White, CircleShape)
        )

        // Contenido principal (Logo, texto, botones)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround // Para distribuir el espacio
        ) {
            // Sección superior (Logo y texto "BENEFICIO JOVEN")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp), // Padding superior para el logo
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Contenedor circular blanco para el logo 'b'
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.White, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    // Placeholder del logo 'b' con degradado (si no tienes el asset)
                    val logoBrush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF9966CC), Color(0xFFDDA0DD)) // Colores del logo 'b'
                    )
                    Box(
                        modifier = Modifier
                            .size(35.dp) // Tamaño de la 'b' dentro del círculo
                            .background(logoBrush, shape = CircleShape)
                    )
                    // Si tienes el asset del logo, usa Image:
                    /*
                    Image(
                        painter = painterResource(id = R.drawable.your_logo_b_asset),
                        contentDescription = "Logo de Beneficio Joven",
                        modifier = Modifier.size(35.dp)
                    )
                    */
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append("BENEFICIO")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("JOVEN")
                        }
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
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón "Iniciar sesión"
            Button(
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

            // Texto "¿Eres nuevo aquí? Regístrate"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Eres nuevo aquí? ",
                    color = blanco.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
                // Usamos un Text simple para "Regístrate" para que parezca un enlace
                // Puedes envolverlo en un ClickableText si necesitas una acción de click
                Text(
                    text = "Regístrate",
                    color = rosa, // Color rosa para el enlace
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                    // Si quieres que sea clicable:
                    // modifier = Modifier.clickable { /* TODO: Navegar a la pantalla de registro */ }
                )
            }
            // Espacio flexible para empujar el contenido central
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InicioPreview() {
    Inicio()
}
