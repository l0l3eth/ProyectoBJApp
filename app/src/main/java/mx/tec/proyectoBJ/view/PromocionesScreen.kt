package mx.tec.proyectoBJ.view

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mx.tec.proyectoBJ.model.Promocion
import mx.tec.proyectoBJ.viewmodel.AppVM


@Composable
fun PromocionesScreen(
    appVM: AppVM,
){
    val listaPromocion by appVM.promociones.collectAsState()
    val estaCargando by appVM.estaCargando.collectAsState()
    val error by appVM.error.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (estaCargando) {
            CircularProgressIndicator(modifier = Modifier.size(50.dp))
        }
        else if(error != null){
            Text(
                text= error!!,
                color=Color.Red,
                style=MaterialTheme.typography.bodyLarge
            )
        }
        else if(listaPromocion.isEmpty()){
            Text(
                text="Aun no hay promociones",
                style=MaterialTheme.typography.bodyLarge
            )
        }
        else{
            LazyColumn (
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
            ) {
                items(listaPromocion) {promocion->
                    PromocionItem(promocion=promocion)

                }
            }
        }
    }
}

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
                text = promocion.nombre,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Válido hasta: ${promocion.descripcion}",
                style = MaterialTheme.typography.bodyMedium
            )

        }

    }
}