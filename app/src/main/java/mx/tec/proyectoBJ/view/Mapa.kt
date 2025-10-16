package mx.tec.proyectoBJ.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import mx.tec.ptoyectobj.fondoGris
import mx.tec.ptoyectobj.rosa

data class NegocioUbicacion(
    val address: String,
    val name: String,
    val hours: String,
    val distance: String,
    val latLng: LatLng
)

val NegociosUbicacion = listOf(
    NegocioUbicacion(
        "AV. RUIZ CORTINES",
        "LASHER SPA",
        "Abierto hasta 20:00",
        "429 mts de ti",
        LatLng(19.4326, -99.1332) // CDMX
    ),
    NegocioUbicacion(
        "LÁZARO CÁRDENAS",
        "NAILS & LASHES JIMÉNEZ",
        "Abierto hasta 20:00",
        "1.38 km de ti",
        LatLng(19.4300, -99.1300)
    )
)

@SuppressLint("UnrememberedMutableState")
@Composable
fun MapaScreen(
    //appVM: AppVM,
    modifier: Modifier = Modifier
) {

    var searchText by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = { BarraNavegacion( onNavigateToInicio = {},
                                        onNavigateToMapa = {},
                                        onNavigateToPromociones = {},
                                        onNavigateToID = {}) },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoGris)
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                ParteSuperior(
                    userName = "Usuario",
                    modifier = Modifier.padding(bottom = 0.dp)
                )
            }

            item {
                // --- Título "Mapa" ---
                Text(
                    text = "Mapa",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }

            item {
                // Google Map Composables
                // Simulación de la ubicación del usuario (puedes cambiar LatLng por la ubicación real)
                val userLocation = LatLng(19.4326, -99.1332)

                // Configuración inicial de la cámara
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(userLocation, 12f)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // Altura fija para el mapa
                        .padding(horizontal = 16.dp)
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        uiSettings = MapUiSettings(zoomControlsEnabled = true)
                    ) {
                        // Marcador de ubicación del usuario (el punto azul)
                        Marker(
                            state = MarkerState(position = userLocation),
                            title = "Tu Ubicación",
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        )

                        // Añadir marcadores para los negocios
                        NegociosUbicacion.forEach { location ->
                            Marker(
                                state = MarkerState(position = location.latLng),
                                title = location.name
                            )
                        }
                    }
                }
            }

            item {
                // --- Título "Cerca de ti" ---
                Text(
                    text = "CERCA DE TI",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
                Divider(color = Color.LightGray, thickness = 1.dp)
            }

            // --- Lista de Negocios Cercanos ---
            items(NegociosUbicacion.filter {
                // Filtrado simple por nombre del negocio
                it.name.contains(searchText, ignoreCase = true)
            }) { location ->
                BusinessLocationItem(location = location)
            }
        }
    }
}

// Componente para cada negocio en la lista "Cerca de ti"
@Composable
fun BusinessLocationItem(location: NegocioUbicacion) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .clickable { /* TODO: Navegar o centrar mapa en este negocio */ }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = location.address.uppercase(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = location.name.uppercase(),
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = location.hours,
                    fontSize = 12.sp,
                    color = rosa,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = location.distance,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            // Icono de información/detalle (el pequeño círculo con la "i")
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Detalle del negocio",
                tint = Color.Gray,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}


@Preview(showBackground = true)
@Composable
fun MapaScreenPreview() {
    MapaScreen()
}