package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.ptoyectobj.fondoGris
import mx.tec.ptoyectobj.morado
import mx.tec.ptoyectobj.rosa

// --- Modelos de datos para el ejemplo ---
data class PromotionDetail(
    val id: String,
    val category: String,
    val businessName: String,
    val description: String,
    val discountsAvailable: String
)

val sampleDetailedPromotions = listOf(
    PromotionDetail("1", "Comida", "WAFFLES Y FRESAS \"BESC\"", "Postres y café", "2 descuentos disponibles"),
    PromotionDetail("2", "Salud", "JÖTUNHEIM", "Academia de artes marciales", "2 descuentos disponibles"),
    PromotionDetail("3", "Comida", "BURGER DEALER", "Restaurante", "1 descuento disponible"),
    PromotionDetail("4", "Belleza", "WILD BARBER SCHOOL", "Escuela de barbería", "2 descuentos disponibles"),
    PromotionDetail("5", "Entretenimiento", "CINE MUNDO", "Boletos 2x1", "3 descuentos disponibles"),
)

val categories = listOf("Todo", "Comida", "Salud", "Belleza", "Entretenimiento")

@Composable
fun PromocionesScreen(
    //appVM: AppVM

) {
    // 1. Estados de la pantalla
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var searchText by remember { mutableStateOf("") }

    // 2. Lógica de Filtrado Local (simulación)
    val selectedCategory = categories[selectedTabIndex]

    val filteredByTab = sampleDetailedPromotions.filter {
        selectedCategory == "Todo" || it.category == selectedCategory
    }

    val finalFilteredList = filteredByTab.filter {
        it.businessName.contains(searchText, ignoreCase = true) ||
                it.description.contains(searchText, ignoreCase = true)
    }

    Scaffold(
        bottomBar = { BarraNavegacion( onNavigateToInicio = {},
                                        onNavigateToMapa = {},
                                        onNavigateToPromociones = {},
                                        onNavigateToID = {}) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoGris)
                .padding(paddingValues),
            contentPadding = PaddingValues(top = 0.dp, bottom = 16.dp)
        ) {
            item {
                // --- Parte Superior (Reutilizada) ---
                Column {
                    ParteSuperior(
                        userName = "Usuario",
                        modifier = Modifier.padding(bottom = 0.dp)
                    )

                    // --- Pestañas de Categorías ---
                    CategoryTabs(
                        categories = categories,
                        selectedTabIndex = selectedTabIndex,
                        onTabSelected = { selectedTabIndex = it }
                    )
                }
            }

            // --- Lista de Promociones Filtradas ---
            items(finalFilteredList) { promo ->
                PromotionDetailCard(promo = promo)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ---------------------------------------------------------------------
// 1. PESTAÑAS DE CATEGORÍA
// ---------------------------------------------------------------------

@Composable
fun CategoryTabs(
    categories: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    // Contenedor para la TabRow que permite el desplazamiento horizontal (si hay muchas tabs)
    ScrollableTabRow(
        selectedTabIndex = selectedTabIndex,
        edgePadding = 16.dp, // Espacio antes de la primera y después de la última pestaña
        containerColor = White,
        indicator = { tabPositions ->
            // Indicador de la pestaña seleccionada (la línea morada)
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .padding(horizontal = 12.dp), // Ajuste horizontal de la línea
                color = morado,
                height = 3.dp
            )
        }
    ) {
        categories.forEachIndexed { index, title ->
            val isSelected = index == selectedTabIndex
            Tab(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                modifier = Modifier
                    .height(48.dp),
                text = {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) morado else Color.Gray
                    )
                }
            )
        }
    }
    // Línea divisoria debajo de las pestañas
    Divider(color = Color.LightGray.copy(alpha = 0.5f), thickness = 1.dp)
}

// ---------------------------------------------------------------------
// 2. TARJETA DE NEGOCIO
// ---------------------------------------------------------------------

@Composable
fun PromotionDetailCard(promo: PromotionDetail) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(110.dp), // Altura aproximada
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { /* TODO: Navegar a detalles de la promoción */ },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder de la Imagen (cuadrado gris)
            Box(
                modifier = Modifier
                    .size(80.dp) // Tamaño de la imagen
                    .padding(start = 12.dp)
                    .background(Color.LightGray.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Textos de la promoción
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = promo.businessName.uppercase(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = promo.description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = promo.discountsAvailable,
                    fontSize = 12.sp,
                    color = rosa,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ---------------------------------------------------------------------
// PREVIEW
// ---------------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun PromocionesScreenPreview() {
    PromocionesScreen()
}