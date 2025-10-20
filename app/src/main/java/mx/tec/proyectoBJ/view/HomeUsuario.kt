package mx.tec.proyectoBJ.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import coil3.compose.rememberAsyncImagePainter
import mx.tec.proyectoBJ.model.TarjetaNegocio
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.ptoyectobj.fondoGris
import mx.tec.ptoyectobj.morado
import mx.tec.ptoyectobj.rosa

// Las categorías se mantienen, ya que son parte de la UI
val categorias = listOf("Todo", "Comida", "Salud", "Belleza", "Entretenimiento")

@Composable
fun HomeUsuario(
    appVM: AppVM
) {
    // 1. Estados de la UI
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    //var searchText by remember { mutableStateOf("") } // Aún no lo usamos, pero es útil tenerlo

    // PASO 2: CONECTAR CON EL VIEWMODEL
    // Obtenemos el estado de carga y la lista de negocios directamente del AppVM.
    // 'by' se encarga de observar los cambios y redibujar el Composable automáticamente.
    val estaCargando by appVM.cargando
    val negociosDesdeAPI by appVM.listaNegocios

    // 2. Lógica de Filtrado (ahora con datos reales)
    val selectedCategory = categorias[selectedTabIndex]

    // Filtra la lista obtenida de la API según la pestaña seleccionada
    val filteredByTab = if (selectedCategory == "Todo") {
        negociosDesdeAPI //mostraremos todos
    } else {
        negociosDesdeAPI.filter { negocio ->
            // Compara la categoría del negocio con la seleccionada (ignorando mayúsculas/minúsculas)
            negocio.categoria.equals(selectedCategory, ignoreCase = true)
        }
    }

    // (Opcional) Filtro por texto de búsqueda. Por ahora, solo usamos el filtro de pestañas.
    val finalFilteredList = filteredByTab // Si agregas un buscador, aquí se anidaría el filtro.

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        // PASO 3: GESTIONAR EL ESTADO DE CARGA
        // Si 'estaCargando' es true, mostramos un indicador de progreso centrado.
        if (estaCargando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(fondoGris),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = morado) // Loader
            }
        } else {
            // Si no está cargando, mostramos el contenido principal.
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(fondoGris)
                    .padding(paddingValues),
                contentPadding = PaddingValues(top = 0.dp, bottom = 16.dp)
            ) {
                item {
                    Column {
                        ParteSuperior(
                            userName = "Usuario",
                            modifier = Modifier.padding(bottom = 0.dp),
                            onClick = {}
                        )
                        TabCategorias(
                            categories = categorias,
                            selectedTabIndex = selectedTabIndex,
                            onTabSelected = { selectedTabIndex = it }
                        )
                    }
                }

                // --- Lista de Negocios desde la API ---
                items(finalFilteredList) { negocio ->
                    // PASO 3 (Continuación): Usamos la nueva tarjeta adaptada.
                    TarjetaNegocioCard(negocio = negocio)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// ... (el resto de tu archivo HomeUsuario.kt se mantiene igual)


@Composable
fun TabCategorias(
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
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .padding(horizontal = 12.dp), // Ajuste horizontal de la línea
                height = 3.dp,
                color = morado
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
    HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))
}

// ---------------------------------------------------------------------
// 2. TARJETA DE NEGOCIO (ADAPTADA PARA TarjetaNegocio)
// ---------------------------------------------------------------------

@Composable
fun TarjetaNegocioCard(negocio: TarjetaNegocio) { // Renombrada y adaptada
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(110.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable { /* TODO: Navegar a detalles con el id del negocio */ },
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen desde la URL usando Coil
            Image(
                // Ojo: Asegúrate de que `negocio.imagen` sea una URL válida
                painter = rememberAsyncImagePainter(model = negocio.imagen),
                contentDescription = "Imagen de ${negocio.nombre}",
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(80.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))

            )

            Spacer(modifier = Modifier.width(12.dp))

            // Textos usando los campos del modelo TarjetaNegocio
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = negocio.nombre.uppercase(), // Usamos 'nombre'
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    // Podemos usar la categoría como subtítulo
                    text = negocio.categoria, // Usamos 'categoria'
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ver descuentos",
                    fontSize = 12.sp,
                    color = rosa,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun HomeUsuarioPreview() {
    HomeUsuario(appVM = AppVM())
}

