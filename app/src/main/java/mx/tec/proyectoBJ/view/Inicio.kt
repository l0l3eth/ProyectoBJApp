package mx.tec.proyectoBJ.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.proyectoBJ.blanco
import mx.tec.proyectoBJ.morado
import mx.tec.proyectoBJ.naranja
import mx.tec.proyectoBJ.rosa

@Composable
fun Inicio(onNavigateToInicioSesion: () -> Unit, onNavigateToRegistro: () -> Unit ,appVM: AppVM) {
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
            LogoYTextoPequeño()

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
                onClick = { onNavigateToInicioSesion() },
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
                Text(
                    text = "Regístrate",
                    color = blanco,
                    fontSize = 16.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { onNavigateToRegistro() }
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

