package mx.tec.proyectoBJ.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.proyectoBJ.model.Usuario
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.proyectoBJ.fondoGris

/**
 * Composable que construye la pantalla completa de la Identificación Digital.
 *
 * Esta pantalla muestra una barra superior con el nombre del usuario y, en el área
 * principal, presenta la tarjeta de identificación digital (`IDCard`).
 * Utiliza un `Scaffold` para una estructura de diseño estándar de Material Design.
 *
 * @param appVM La instancia del ViewModel [AppVM] que proporciona los datos del usuario
 *              y la lógica para generar el código QR.
 *
 * Creado por: Estrella Lolbeth Téllez Rivas A01750496
 *                Allan Mauricio Brenes Castro A01750747
 *                Carlos Antonio Tejero Andrade A01801062
 */
@Composable
fun PantallaIDDigital(appVM: AppVM) {

    // Observa el LiveData del usuario logeado para actualizar la UI cuando cambie.
    val usuario by appVM.usuarioLogeado.observeAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoGris)
                .padding(paddingValues)
        ) {
            // --- Barra superior personalizada ---
            ParteSuperior(
                userName = usuario?.nombre ?: "Usuario",
                modifier = Modifier.padding(bottom = 0.dp),
                onClick = {}
            )

            // Contenido principal: la tarjeta de ID, centrada.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f) // Ocupa el espacio restante
                    .padding(top = 40.dp, bottom = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                IDCard(data = usuario, appVM = appVM)
            }
        }
    }
}

/**
 * Composable que representa la tarjeta de identificación digital.
 *
 * Esta tarjeta muestra el nombre y el ID del usuario, junto con un código QR único.
 * Se encarga de solicitar la generación del QR a través del [AppVM] y de gestionar
 * los diferentes estados de la UI (carga, éxito, error) durante este proceso.
 *
 * @param data El objeto [Usuario] que contiene la información a mostrar. Puede ser nulo.
 * @param appVM La instancia del ViewModel [AppVM] para invocar la generación del QR
 *              y observar su estado.
 */
@Composable
fun IDCard(data: Usuario?, appVM : AppVM ) {
    // `LaunchedEffect(Unit)` ejecuta la generación del QR una sola vez cuando el Composable entra en la composición.
    LaunchedEffect(Unit) {
        appVM.generarQR()
    }
    // Observa los estados del ViewModel relacionados con el QR.
    val qrBitmap by appVM.qrBitmap.collectAsState()
    val isLoading by appVM.cargandoQR.collectAsState()
    val error by appVM.errorMensaje.observeAsState()

    Card(
        modifier = Modifier
            .width(300.dp) // Ancho fijo para simular una tarjeta física.
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cabecera de la tarjeta: Nombre e ID del usuario.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = data?.nombre ?: "Usuario",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "ID: ${data?.idUsuario?.toString() ?: "No disponible"}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Contenedor del código QR que gestiona los diferentes estados.
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(250.dp) // Tamaño fijo para el QR.
            ) {
                when {
                    isLoading -> {
                        // Estado de carga: Muestra un indicador de progreso.
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Generando tu código QR...")
                        }
                    }
                    qrBitmap != null -> {
                        // Estado de éxito: Muestra la imagen del código QR.
                        Image(
                            bitmap = qrBitmap!!, // El operador !! es seguro por la condición previa.
                            contentDescription = "Código QR del usuario",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    error != null -> {
                        // Estado de error: Muestra el mensaje de error.
                        Text(
                            text = "Error: $error",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                    else -> {
                        // Estado por defecto o si no hay datos.
                        Text(
                            text = "No se puede generar el QR.",
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Composable de previsualización para la pantalla `PantallaIDDigital`.
 *
 * Permite a Android Studio renderizar una vista previa del diseño sin necesidad
 * de ejecutar la aplicación completa en un emulador o dispositivo.
 * Crea una instancia simulada de `AppVM`.
 */
@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun IDDigitalScreenPreview() {
    PantallaIDDigital( appVM = AppVM())
}
