//package mx.tec.proyectoBJ.view
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Info
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Color.Companion.White
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.google.android.gms.maps.model.CameraPosition
//import com.google.android.gms.maps.model.LatLng
//import com.google.maps.android.compose.*
//import mx.tec.ptoyectobj.fondoGris
//import mx.tec.ptoyectobj.morado
//import mx.tec.ptoyectobj.rosa
//
//data class BusinessLocation(
//    val address: String,
//    val name: String,
//    val hours: String,
//    val distance: String,
//    val latLng: LatLng
//)
//
//val sampleLocations = listOf(
//    BusinessLocation(
//        "AV. RUIZ CORTINES",
//        "LASHER SPA",
//        "Abierto hasta 20:00",
//        "429 mts de ti",
//        LatLng(19.4326, -99.1332) // CDMX
//    ),
//    BusinessLocation(
//        "LÁZARO CÁRDENAS",
//        "NAILS & LASHES JIMÉNEZ",
//        "Abierto hasta 20:00",
//        "1.38 km de ti",
//        LatLng(19.4300, -99.1300)
//    )
//    // Más negocios...
//)
//
//// --- Simulación de tus componentes reutilizados ---
//@Composable fun ParteSuperior(
//    userName: String,
//    searchText: String,
//    onSearchTextChange: (String) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    // Mantenemos la estructura visual de la barra superior sin el filtro de favoritos
//    // ... (Tu código de ParteSuperior simplificado para este ejemplo)
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(140.dp) // Altura aproximada del header
//            .background(morado)
//            .padding(horizontal = 24.dp)
//    ) {
//        // ... (Fila del menú, Bienvenido y foto de perfil)
//        // ... (SearchTextField)
//    }
//}
//@Composable fun SearchTextField(searchText: String, onTextChange: (String) -> Unit, modifier: Modifier) { /* ... */ }
//// --------------------------------------------------
//
//
//@Composable
//fun MapaScreen(
//    // Aquí podrías inyectar un ViewModel si manejaras la ubicación real y los datos
//    modifier: Modifier = Modifier
//) {
//    // Para la demo, el texto de búsqueda no afecta el mapa, solo la lista de negocios
//    var searchText by remember { mutableStateOf("") }
//
//    Scaffold(
//        bottomBar = { BarraNavegacion() },
//        modifier = modifier.fillMaxSize()
//    ) { paddingValues ->
//
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(fondoGris)
//                .padding(paddingValues),
//            contentPadding = PaddingValues(bottom = 16.dp)
//        ) {
//            item {
//                // --- Parte Superior (Reutilizada) ---
//                ParteSuperior(
//                    userName = "Usuario",
//                    searchText = searchText,
//                    onSearchTextChange = { searchText = it },
//                    // Usamos padding(bottom = 0.dp) para que la barra de búsqueda quede pegada al borde
//                    modifier = Modifier.padding(bottom = 0.dp)
//                )
//            }
//
//            item {
//                // --- Título "Mapa" ---
//                Text(
//                    text = "Mapa",
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black.copy(alpha = 0.8f),
//                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
//                )
//            }
//
//            item {
//                // --- Google Map Composables ---
//                // Simulación de la ubicación del usuario (puedes cambiar LatLng por la ubicación real)
//                val userLocation = LatLng(19.4326, -99.1332)
//
//                // Configuración inicial de la cámara
//                val cameraPositionState = rememberCameraPositionState {
//                    position = CameraPosition.fromLatLngZoom(userLocation, 12f)
//                }
//
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(300.dp) // Altura fija para el mapa
//                        .padding(horizontal = 16.dp)
//                ) {
//                    GoogleMap(
//                        modifier = Modifier.fillMaxSize(),
//                        cameraPositionState = cameraPositionState,
//                        uiSettings = MapUiSettings(zoomControlsEnabled = false) // Ocultar controles de zoom
//                    ) {
//                        // Marcador de ubicación del usuario (el punto azul)
//                        Marker(
//                            state = MarkerState(position = userLocation),
//                            title = "Tu Ubicación",
//                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
//                        )
//
//                        // Añadir marcadores para los negocios
//                        sampleLocations.forEach { location ->
//                            Marker(
//                                state = MarkerState(position = location.latLng),
//                                title = location.name
//                            )
//                        }
//                    }
//                }
//            }
//
//            item {
//                // --- Título "Cerca de ti" ---
//                Text(
//                    text = "CERCA DE TI",
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Gray,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(White)
//                        .padding(horizontal = 16.dp, vertical = 12.dp)
//                )
//                Divider(color = Color.LightGray, thickness = 1.dp)
//            }
//
//            // --- Lista de Negocios Cercanos ---
//            items(sampleLocations.filter {
//                // Filtrado simple por nombre del negocio
//                it.name.contains(searchText, ignoreCase = true)
//            }) { location ->
//                BusinessLocationItem(location = location)
//            }
//        }
//    }
//}
//
//// Componente para cada negocio en la lista "Cerca de ti"
//@Composable
//fun BusinessLocationItem(location: BusinessLocation) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(White)
//            .clickable { /* TODO: Navegar o centrar mapa en este negocio */ }
//            .padding(horizontal = 16.dp, vertical = 12.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = location.address.uppercase(),
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.Black
//                )
//                Text(
//                    text = location.name.uppercase(),
//                    fontSize = 14.sp,
//                    color = Color.Black.copy(alpha = 0.8f)
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = location.hours,
//                    fontSize = 12.sp,
//                    color = rosa,
//                    fontWeight = FontWeight.SemiBold
//                )
//                Text(
//                    text = location.distance,
//                    fontSize = 12.sp,
//                    color = Color.Gray
//                )
//            }
//            // Icono de información/detalle (el pequeño círculo con la "i")
//            Icon(
//                imageVector = Icons.Default.Info,
//                contentDescription = "Detalle del negocio",
//                tint = Color.Gray,
//                modifier = Modifier
//                    .size(20.dp)
//                    .align(Alignment.CenterVertically)
//            )
//        }
//        Spacer(modifier = Modifier.height(8.dp))
//        Divider(color = Color.LightGray, thickness = 1.dp)
//    }
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun MapaScreenPreview() {
//    MapaScreen()
//}