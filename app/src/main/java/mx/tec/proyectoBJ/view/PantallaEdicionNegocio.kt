package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.proyectoBJ.model.Negocio
import mx.tec.ptoyectobj.fondoGris
import mx.tec.ptoyectobj.morado

@Composable
fun NegocioEdicionPerfil(modifier: Modifier = Modifier) {
    // Objeto provisional
    val negocio = Negocio(
        nombreNegocio = "JÖTUNHEIM",
        numeroTelefono = "555-1234567",
        ubicacion = "Ciudad de México",
        correo = "jottunheim@gmail.com",
        descripcion = "Academia de Artes Marciales"
    )
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoGris)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente
        ) {
            item {
                EditarPerfilHeader(modifier, innerPadding)
            }

            item {
                BusinessInfoSectionEditable(
                    name = negocio.nombreNegocio,
                    description = negocio.descripcion,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun EditarPerfilHeader(
    modifier: Modifier,
    innerPadding: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // No tiene las esquinas redondeadas en la parte inferior de la captura
            .background(morado)
            .padding(bottom = 0.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Fila superior (Menú y Título)
            BarraArmaPerfil(modifier = modifier.padding(innerPadding))

            // Espacio negro para el banner/imagen del negocio
            EditarPortada(
                contenido = { Portada() }
            )
        }

        // Logos flotantes (Alineados al centro)
        Icono()
    }
    // Relleno para que el contenido de abajo inicie después de los logos flotantes
    Spacer(modifier = Modifier.height(64.dp))
}

@Composable
fun BarraArmaPerfil(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Arma tu perfil",
            color = White,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Contenedor para la portada que superpone un botón "Cambiar Portada".
 * @param contenido El composable que se mostrará como fondo de la portada.
 */
@Composable
fun EditarPortada(
    modifier: Modifier = Modifier,
    contenido: @Composable () -> Unit
) {
    // 1. Box permite superponer elementos unos encima de otros.
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart // Centra el botón
    ) {
        // 2. El contenido de fondo que pasaste a la función.
        contenido()

        // 3. El botón que se mostrará encima del contenido.
        Button(
            onClick = { /* TODO: Lógica para cambiar la portada */ },
            shape = RoundedCornerShape(8.dp), // Esquinas redondeadas
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, // Fondo blanco
                contentColor = morado // Color del texto
            ),
            modifier = modifier.padding(4.dp)
        ) {
            Text(text = "Cambiar Portada")
        }
    }
}

@Composable
fun BusinessInfoSectionEditable(name: String,
                                description: String,
                                modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(fondoGris) // Asegura que el fondo sea gris
    ) {
        TextField(
            value = name,
            onValueChange = { /* Actualiza el nombre */ },
            label = { Text("Nombre del Negocio") },
            modifier = modifier.padding(vertical = 8.dp)
        )
        TextField(
            value = description,
            onValueChange = { /* Actualiza la descripción */ },
            label = { Text("Descripción") }
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun NegocioEdicionPerfilPreview() {
    NegocioEdicionPerfil()
}

