package mx.tec.proyectoBJ.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.tec.proyectoBJ.model.ServicioRemoto
import mx.tec.proyectoBJ.model.Usuario

/**
 * AppVM (App ViewModel) es el ViewModel principal de la aplicación.
 * Se encarga de la lógica  relacionada con el inicio y cierre de sesión.
 */

sealed class PantallaSplash {
    object NavegarAInicio : PantallaSplash()
}

// A new sealed class to represent the state of the registration
sealed class EstadoRegistroUI {
    object Idle : EstadoRegistroUI() // Initial state
    object Loading : EstadoRegistroUI() // When the request is in progress
    object Success : EstadoRegistroUI() // On success
    data class Error(val message: String?) : EstadoRegistroUI() // On failure
}

class AppVM : ViewModel(){
    private val servicioRemoto = ServicioRemoto
    private val _NavegarAInicio = MutableSharedFlow<PantallaSplash>()
    val NavegarAInicio: SharedFlow<PantallaSplash> = _NavegarAInicio.asSharedFlow()

    private val _estatusRegistro = MutableStateFlow<EstadoRegistroUI>(EstadoRegistroUI.Idle)
    val estatusRegistro = _estatusRegistro.asStateFlow()

    init {
        // Ejecuta la lógica de retardo y navegación al iniciar el ViewModel
        viewModelScope.launch {
            // Retraso de menos de un segundo
            delay(3000L)
            // Envía el evento de navegación
            _NavegarAInicio.emit(PantallaSplash.NavegarAInicio)
        }
    }

    fun enviarUsuario(nombre: String,
                      apellidos: String,
                      correo: String,
                      contrasena: String,
                      direccion: String,
                      numeroTelefono: String,
                      curp: String) {
        viewModelScope.launch {

            _estatusRegistro.value = EstadoRegistroUI.Loading

            val resultado = servicioRemoto.registrarUsuario(
                Usuario(
                    nombre = nombre,
                    apellidos = apellidos,
                    correo = correo,
                    contrasena = contrasena,
                    direccion = direccion,
                    telefono = numeroTelefono,
                    curp = curp
                )
            )

            if (resultado.isSuccess) {
                _estatusRegistro.value = EstadoRegistroUI.Success
            } else {
                _estatusRegistro.value = EstadoRegistroUI.Error(resultado.exceptionOrNull()?.message)
            }
        }
    }

}
