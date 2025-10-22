package mx.tec.proyectoBJ.view

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.proyectoBJ.R
import mx.tec.proyectoBJ.model.Negocio
import mx.tec.proyectoBJ.fondoGris
import mx.tec.proyectoBJ.morado

@Composable
fun NegocioEdicionPerfil(modifier: Modifier = Modifier) {
    // Objeto provisional
    var negocio: Negocio by remember { mutableStateOf(Negocio(
            nombreNegocio = "JÖTUNHEIM",
        numeroTelefono = "555-1234567",
        ubicacion = "Ciudad de México",
        correo = "jottunheim@gmail.com"
        ))
    }

    fun actualizar(nombreNegocio: String = "",
                   numeroTelefono: String = "",
                   correo: String = "",
                   icono: String = "",
                   portada: String = "") {
        // Lógica para actualizar los datos del negocio
        negocio.nombreNegocio = nombreNegocio
        negocio.numeroTelefono = numeroTelefono
        negocio.correo = correo
        negocio.icono = icono
        negocio.portada = portada
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoGris)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente
        ) {
            EditarPerfilHeader(modifier, innerPadding)

            // Logos flotantes (Alineados al centro)
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                // El Box ahora es el que tiene el tamaño y el botón se alineará a él
                EditarIcono(
                    contenido = {
                        EditarIcono()
                    },
                    actualizar = { actualizar() }
                )
            }

            BusinessInfoSectionEditable(
                negocio = negocio,
                modifier = modifier
            ) {
                actualizar()
            }

            ElevatedButton(
                onClick = {  },
                colors = ButtonDefaults.buttonColors(
                    containerColor = White, // Fondo blanco
                    contentColor = morado // Color del texto
            )) {
                Text("Guardar configuración")
            }
        }
    }
}

@Composable
private fun EditarPerfilHeader(
    modifier: Modifier,
    innerPadding: PaddingValues,
    actualizar: () -> Unit = {}
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
            BarraArmaPerfil(
                modifier = modifier.padding(innerPadding)
            )

            // Espacio negro para el banner/imagen del negocio
            EditarPortada(
                contenido = { Portada() },
                actualizar = {
                    actualizar()
                }
            )
        }
    }
    // Relleno para que el contenido de abajo inicie después de los logos flotantes
    Spacer(modifier = Modifier.height(16.dp))
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
    actualizar: () -> Unit,
    contenido: @Composable () -> Unit
) {
    // 1. Box permite superponer elementos unos encima de otros.
    Box (
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
                containerColor = White, // Fondo blanco
                contentColor = morado // Color del texto
            ),
            modifier = modifier.padding(4.dp)
        ) {
            Text(text = "Cambiar Portada")
        }
    }
}

/**
 * Contenedor para el icono que superpone un botón de edición.
 * @param contenido El composable que se mostrará como el icono.
 */
@Composable
fun EditarIcono(
    modifier: Modifier = Modifier,
    actualizar: () -> Unit,
    contenido: @Composable () -> Unit
) {
    // 1. Box para superponer el botón sobre el icono.
    // LA CLAVE: El Box ahora define el tamaño del área.
    Box(
        modifier = modifier
            .size(100.dp) // <-- El tamaño ahora se define aquí
    ) {
        // 2. Dibuja el contenido (el icono) que pasamos como parámetro.
        contenido()

        // 3. Superpone un IconButton para la edición.
        IconButton(
            onClick = { /* TODO: Lógica para cambiar el icono */ },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.LightGray, // Fondo blanco para que el lápiz sea visible
                contentColor = morado // Color del icono de lápiz
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd) // Ahora se alineará al Box de 100.dp
                .size(32.dp) // Damos un tamaño fijo al botón
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Editar Icono",
                modifier = Modifier.size(20.dp) // Hacemos el icono de lápiz un poco más pequeño
            )
        }
    }
}

@Composable
fun BusinessInfoSectionEditable(negocio: Negocio,
                                modifier: Modifier = Modifier,
                                actualizar: () -> Unit) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(fondoGris) // Asegura que el fondo sea gris
    ) {
        item {
            CampoDeTexto(
                value = negocio.nombreNegocio,
                onValueChange = { /* Actualiza el nombre */ },
                etiqueta = "Nombre del negocio",
                modifier = modifier.padding(vertical = 8.dp)
            )
        }

        item {
            CampoDeTexto(
                value = negocio.numeroTelefono,
                onValueChange = { /* Actualiza el número de teléfono */ },
                etiqueta = "Número de teléfono",
                modifier = modifier.padding(vertical = 8.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
fun EditarIcono(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_background), // Reemplaza con tu imagen
        contentDescription = "Icono del negocio",
        contentScale = ContentScale.Crop, // Escala la imagen para que llene el espacio
        modifier = modifier
            .fillMaxSize() // Ocupa todo el espacio del Box padre (100.dp)
            .clip(CircleShape) // Recorta la imagen en forma de círculo
    )
}

@Preview(showBackground = true)
@Composable
fun NegocioEdicionPerfilPreview() {
    NegocioEdicionPerfil()
}

