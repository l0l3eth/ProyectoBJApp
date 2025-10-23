package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mx.tec.proyectoBJ.fondoGris
import mx.tec.proyectoBJ.model.Promocion
import mx.tec.proyectoBJ.viewmodel.AppVM


/**
 * Muestra la pantalla de promociones.
 *
 * Esta pantalla observa el estado del [AppVM] para mostrar una lista de promociones.
 * Gestiona y muestra diferentes estados de la UI:
 * - Un indicador de progreso mientras se cargan los datos.
 * - Un mensaje de error si la carga falla.
 * - Un mensaje indicando que no hay promociones si la lista está vacía.
 * - La lista de promociones utilizando un `LazyColumn`.
 *
 * @param appVM La instancia del ViewModel [AppVM] que proporciona el estado y los datos de las promociones.
 * Creado por: Carlos Antonio Tejero Andrade A01801062
 */
@Composable
fun PromocionesScreen(
    appVM: AppVM,
) {
    // Se suscribe a los flujos de estado del ViewModel para reaccionar a los cambios.
    val listaPromocion by appVM.promociones.collectAsState()
    val estaCargando by appVM.estaCargando.collectAsState()
    val error by appVM.error.collectAsState()
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

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                // Muestra un indicador de carga mientras se obtienen los datos.
                if (estaCargando) {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                }
                // Muestra un mensaje de error si ocurrió un problema.
                else if (error != null) {
                    Text(
                        text = error!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                // Muestra un mensaje si no hay promociones disponibles.
                else if (listaPromocion.isEmpty()) {
                    Text(
                        text = "Aun no hay promociones",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                // Muestra la lista de promociones si hay datos disponibles.
                else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                    ) {
                        items(listaPromocion) { promocion ->
                            PromocionItem(promocion = promocion)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Representa un único elemento visual para una promoción dentro de la lista.
 *
 * Este Composable muestra los detalles de una promoción, como su nombre y descripción,
 * dentro de una tarjeta con elevación para un mejor diseño visual.
 *
 * @param promocion El objeto [Promocion] que contiene los datos a mostrar.
 * @param modifier El [Modifier] que se aplicará a la `Card`.
 */
@Composable
fun PromocionItem(
    promocion: Promocion,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation=CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = promocion.titulo,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Válido hasta: ${promocion.tipo_descuento}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

