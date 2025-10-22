package mx.tec.proyectoBJ.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.tec.proyectoBJ.model.Promocion // Importa el modelo de datos
import mx.tec.proyectoBJ.model.ServicioRemoto // Importa al ÚNICO con el que habla


class PromocionesViewModel: ViewModel() {
    private val _promociones=MutableStateFlow<List<Promocion>>(emptyList())
    val promociones=_promociones.asStateFlow()

    private val _estaCargando=MutableStateFlow(false)
    val estaCargando=_estaCargando.asStateFlow()

    private val _error=MutableStateFlow<String?>(null)
    val error=_error.asStateFlow()

    init{
        cargarPromociones()
    }

    private fun cargarPromociones() {
        viewModelScope.launch {
            _estaCargando.value = true // Mostramos el loader
            _error.value = null      // Limpiamos errores anteriores

            try {
                // AQUÍ OCURRE LA MAGIA:
                // El ViewModel llama a ServicioRemoto para obtener los datos.
                // No sabe cómo lo hace, solo confía en que le devolverá una lista.
                val listaDesdeServidor = ServicioRemoto.obtenerPromocionesNegocio()
                _promociones.value = listaDesdeServidor

            } catch (e: Exception) {
                // Si ServicioRemoto falla (ej. sin internet), atrapamos el error.
                Log.e("PromocionesVM", "Error al cargar promociones: ${e.message}")
                _error.value = "No se pudieron cargar las promociones. Intenta más tarde."
            } finally {
                // Este bloque se ejecuta siempre, con o sin error.
                _estaCargando.value = false // Ocultamos el loader
            }
        }
    }


}
