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
import androidx.compose.ui.semantics.error
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.proyectoBJ.model.Usuario
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.ptoyectobj.fondoGris

@Composable
fun PantallaIDDigital(appVM: AppVM) {

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
            // --- Parte Superior (Reutilizada) ---
            ParteSuperior(
                userName = usuario?.nombre ?: "Usuario",
                modifier = Modifier.padding(bottom = 0.dp),
                onClick = {}
            )

            // Contenido principal (centrado horizontalmente)
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

// ---------------------------------------------------------------------
// TARJETA DE ID DIGITAL
// ---------------------------------------------------------------------

@Composable
fun IDCard(data: Usuario?, appVM : AppVM ) {
    LaunchedEffect(Unit) {
        appVM.generarQR()
    }
    val qrBitmap by appVM.qrBitmap.collectAsState()
    val isLoading by appVM.cargandoQR.collectAsState()
    val error by appVM.errorMensaje.observeAsState() // Si usas LiveData para errores

    Card(
        modifier = Modifier
            .width(300.dp) // Ancho fijo para parecer una tarjeta física
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
            // Fila: Foto de perfil y Texto (Beneficio Joven / ID)
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
                        text = data?.id.toString(),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            when {
                isLoading -> {
                    // Muestra un indicador de carga mientras se genera el QR
                    CircularProgressIndicator()
                    Text("Generando tu código QR...")
                }
                qrBitmap != null -> {
                    // Si el bitmap no es nulo, muestra la imagen
                    Text("¡Tu QR ha sido generado!", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        bitmap = qrBitmap!!, // El !! es seguro por la comprobación qrBitmap != null
                        contentDescription = "Código QR del usuario",
                        modifier = Modifier.size(250.dp)
                    )
                }
                error != null -> {
                    // Si hay un error, muéstralo
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tipo de Usuario (etiqueta inferior)
            Text(
                text = data?.tipoUsuario ?: "Tipo de Usuario",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}


// ---------------------------------------------------------------------
// PREVIEW
// ---------------------------------------------------------------------

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun IDDigitalScreenPreview() {
    PantallaIDDigital( appVM = AppVM())
}