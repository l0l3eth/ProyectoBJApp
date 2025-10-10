package mx.tec.proyectoBJ.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import mx.tec.proyectoBJ.model.Genero
import mx.tec.proyectoBJ.model.Usuario
import mx.tec.ptoyectobj.model.ServicioRemoto

/**
 * AppVM (App ViewModel) es el ViewModel principal de la aplicación.
 * Gestiona el estado de autenticación del usuario (login/logout) utilizando Firebase Authentication.
 * Se encarga de la lógica  relacionada con el inicio y cierre de sesión.
 */

sealed class PantallaSplash {
    object NavegarAInicio : PantallaSplash()
}

class AppVM : ViewModel(){
    private val servicioRemoto = ServicioRemoto
    private val _NavegarAInicio = MutableSharedFlow<PantallaSplash>()
    val NavegarAInicio: SharedFlow<PantallaSplash> = _NavegarAInicio.asSharedFlow()

    init {
        // Ejecuta la lógica de retardo y navegación al iniciar el ViewModel
        viewModelScope.launch {
            // Retraso de menos de un segundo
            delay(100L)
            // Envía el evento de navegación
            _NavegarAInicio.emit(PantallaSplash.NavegarAInicio)
        }
    }

    fun enviarUsuario(nombre: String,
                      apellido: String,
                      correo: String,
                      contrasena: String,
                      direccion: String,
                      fechaNacimiento: String,
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
                    fechaNacimiento = fechaNacimiento,
                    telefono = numeroTelefono,
                    curp = curp
                )
            )
        }
    }

}
