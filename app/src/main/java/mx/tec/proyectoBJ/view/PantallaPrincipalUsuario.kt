package mx.tec.proyectoBJ.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.proyectoBJ.viewmodel.AppVM

data class Promotion(
    val title: String,
    val description: String
)

val samplePromotions = listOf(
    Promotion("KINEZIS", "25% pagando en efectivo."),
    Promotion("NAILS & LASHES JIMÉNEZ", "10% en aplicación de gelish"),
    Promotion("SALON & BARBER \"THE CROWN\"", "20% en cortes de cabello"),
    Promotion("OPTICAS VISTA", "30% de descuento en armazones"),
)

// --- Composable Principal de la Pantalla ---
@Composable
fun PantallaPrincipalUsuario(/*Logica de navegación*/
                             /*appVM: AppVM*/) {
    Scaffold(
        bottomBar = { BarraNavegacion() },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        // Usamos LazyColumn para que el contenido sea desplazable (scrollable)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(paddingValues),
            // Aseguramos que el contenido empiece justo después de la barra superior
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                ParteSuperior(
                    userName = "Usuario",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            //Solo es para verlos, necesito reemplazarlo por algo sacado de la base de datos.
            items(samplePromotions) { promo ->
                TarjetasPromocion(promo = promo)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ---------------------------------------------------------------------
// PREVIEW
// ---------------------------------------------------------------------

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun UserHomeScreenPreview() {
    PantallaPrincipalUsuario( /*appVM = AppVM()*/ )
}