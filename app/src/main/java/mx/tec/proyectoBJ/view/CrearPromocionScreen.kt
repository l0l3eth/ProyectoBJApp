package mx.tec.proyectoBJ.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mx.tec.proyectoBJ.model.Promocion
import mx.tec.proyectoBJ.viewmodel.AppVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearPromocionScreen(
    appVM: AppVM,
    onNavigateBack: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    // El campo 'descripcion' no está en tu data class, lo he quitado.
    // Si lo necesitas, deberás añadirlo a la data class Promocion.
    var tipoDescuento by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Nueva Promoción") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Campo de texto para el Título
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título de la promoción") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo de texto para el Tipo de Descuento/Vigencia
            OutlinedTextField(
                value = tipoDescuento,
                onValueChange = { tipoDescuento = it },
                label = { Text("Tipo de descuento o vigencia") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón hacia abajo

            // --- BOTÓN "GUARDAR" MODIFICADO ---
            Button(
                onClick = {
                    // 2. Validamos que los campos no estén vacíos
                    if (titulo.isNotBlank() && tipoDescuento.isNotBlank()) {

                        // 3. Creamos el objeto `Promocion` con los datos del formulario
                        val nuevaPromocion = Promocion(
                            id = 0, // El ID lo genera el backend, por eso enviamos 0.
                            titulo = titulo,
                            tipo_descuento = tipoDescuento
                        )

                        // 4. Llamamos a la función del ViewModel que crearás
                        // appVM.guardarPromocion(nuevaPromocion) // <-- Descomenta cuando la crees en AppVM

                        // 5. Navegamos hacia atrás para volver a la lista
                        onNavigateBack()
                    } else {
                        // Opcional: Mostrar un mensaje de que los campos son requeridos.
                        // (Por ahora, simplemente no hacemos nada si están vacíos).
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("GUARDAR PROMOCIÓN")
            }
        }
    }
}