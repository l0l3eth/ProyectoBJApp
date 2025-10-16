package mx.tec.proyectoBJ.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
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

class AppVM : ViewModel(){
    private val servicioRemoto = ServicioRemoto
    private val _NavegarAInicio = MutableSharedFlow<PantallaSplash>()
    val NavegarAInicio: SharedFlow<PantallaSplash> = _NavegarAInicio.asSharedFlow()

    // Estado para la UI: almacena el usuario si el login es exitoso
    private val _usuarioLogeado = MutableLiveData<Usuario?>(null)
    val usuarioLogeado: LiveData<Usuario?> = _usuarioLogeado

    // Estado para mostrar errores en la UI (ej: "Credenciales incorrectas")
    private val _errorMensaje = MutableLiveData<String?>(null)
    val errorMensaje: LiveData<String?> = _errorMensaje

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
                      apellido: String,
                      correo: String,
                      contrasena: String,
                      direccion: String,
                      numeroTelefono: String,
                      curp: String) {
        viewModelScope.launch {
            servicioRemoto.registrarUsuario(
                Usuario(
                    nombre = nombre,
                    apellidos = apellido,
                    correo = correo,
                    contrasena = contrasena,
                    direccion = direccion,
                    telefono = numeroTelefono,
                    curp = curp
                )
            )
        }
    }

    fun iniciarSesion(correo: String, contrasena: String) {
        // Ejecutamos la llamada de red en un scope de coroutines
        viewModelScope.launch {

            // 1. Llamar al Servicio Remoto
            val resultadoUsuario = ServicioRemoto.iniciarSesion(correo, contrasena)

            // 2. Manejar el resultado
            if (resultadoUsuario != null) {
                // Éxito: Guardar el usuario y notificar a la UI
                _usuarioLogeado.value = resultadoUsuario
                _errorMensaje.value = null
            } else {
                // Fallo: Mostrar un mensaje de error genérico (o uno más específico si el servicio lo permite)
                _usuarioLogeado.value = null
                _errorMensaje.value = "Correo o contraseña incorrectos. Inténtalo de nuevo."
            }
        }
    }

}
