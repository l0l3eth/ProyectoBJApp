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
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * ViewModel principal de la aplicación (`AppVM`).
 *
 * Se encarga de la lógica de negocio y de gestionar el estado de la UI para
 * funcionalidades clave como:
 * - Control de la pantalla de bienvenida (splash screen).
 * - Registro e inicio de sesión de usuarios.
 * - Eliminación de usuarios.
 *
 * Comunica el resultado de las operaciones a la UI a través de LiveData y StateFlow,
 * permitiendo una arquitectura reactiva y desacoplada.
 */

sealed class PantallaSplash {
    object NavegarAInicio : PantallaSplash()
}

class AppVM : ViewModel(){
    private val servicioRemoto = ServicioRemoto

    /**
     * Objeto `SharedFlow` para gestionar eventos de navegación de un solo uso,
     * como la transición desde la pantalla de bienvenida.
     */
    private val _NavegarAInicio = MutableSharedFlow<PantallaSplash>()
    val NavegarAInicio: SharedFlow<PantallaSplash> = _NavegarAInicio.asSharedFlow()

    /**
     * Almacena los datos del usuario autenticado.
     * La UI observa este `LiveData` para reaccionar a los cambios en el estado de la sesión.
     * Es nulo si no hay ningún usuario logueado.
     */
    private val _usuarioLogeado = MutableLiveData<Usuario?>(null)
    val usuarioLogeado: LiveData<Usuario?> = _usuarioLogeado

    /**
     * Contiene mensajes de error para ser mostrados en la UI.
     * Se actualiza cuando una operación (como el login) falla.
     */
    private val _errorMensaje = MutableLiveData<String?>(null)
    val errorMensaje: LiveData<String?> = _errorMensaje

    /**
     * `StateFlow` que indica si una operación de borrado está en progreso.
     * Útil para mostrar indicadores de carga en la UI.
     */
    private val _estaBorrando = MutableStateFlow(false)
    val estaBorrando: StateFlow<Boolean> = _estaBorrando.asStateFlow()

    /**
     * `SharedFlow` para emitir un evento de una sola vez cuando un usuario
     * ha sido eliminado con éxito. La UI puede escuchar este evento para
     * refrescar listas o navegar a otra pantalla.
     */
    private val _borradoExitoso = MutableSharedFlow<Boolean>()
    val borradoExitoso: SharedFlow<Boolean> = _borradoExitoso.asSharedFlow()

    /**
     * StateFlow que almacena la imagen del código QR como un ImageBitmap.
     * Es nulo inicialmente o si ocurre un error. La UI observa este estado
     * para mostrar el QR cuando esté disponible.
     */
    private val _qrBitmap = MutableStateFlow<ImageBitmap?>(null)
    val qrBitmap: StateFlow<ImageBitmap?> = _qrBitmap.asStateFlow()

    /**
     * StateFlow que indica si el código QR se está cargando.
     * Útil para mostrar un indicador de progreso en la UI.
     */
    private val _cargandoQR = MutableStateFlow(false)
    val cargandoQR: StateFlow<Boolean> = _cargandoQR.asStateFlow()

    init {
        // Ejecuta la lógica de retardo y navegación al iniciar el ViewModel
        viewModelScope.launch {
            // Retraso para la pantalla de bienvenida
            delay(3000L)
            // Envía el evento de navegación para pasar a la siguiente pantalla
            _NavegarAInicio.emit(PantallaSplash.NavegarAInicio)
        }
    }

    /**
     * Llama al servicio remoto para registrar un nuevo usuario en el sistema.
     * Los parámetros corresponden a los datos del formulario de registro.
     */
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

    /**
     * Intenta autenticar a un usuario con su correo y contraseña.
     * Actualiza `usuarioLogeado` si la autenticación es exitosa, o
     * `errorMensaje` en caso de fallo.
     *
     * @param correo El correo electrónico del usuario.
     * @param contrasena La contraseña del usuario.
     */
    fun iniciarSesion(correo: String, contrasena: String) {
        viewModelScope.launch {
            val resultadoUsuario = ServicioRemoto.iniciarSesion(correo, contrasena)
            if (resultadoUsuario != null) {
                _usuarioLogeado.value = resultadoUsuario
                _errorMensaje.value = null
            } else {
                _usuarioLogeado.value = null
                _errorMensaje.value = "Correo o contraseña incorrectos. Inténtalo de nuevo."
            }
        }
    }

    /**
     * Llama al servicio remoto para eliminar un usuario por su ID.
     * Gestiona el estado de carga (`estaBorrando`) y notifica el resultado
     * a través de `borradoExitoso` o `errorMensaje`.
     *
     * @param idUsuario El ID numérico del usuario a eliminar.
     */
    fun eliminarUsuario(idUsuario: Int) {
        viewModelScope.launch {
            // Indicar que la operación ha comenzado
            _estaBorrando.value = true
            _errorMensaje.value = null // Limpiar errores previos

            val exito = servicioRemoto.borrarUsuario(idUsuario)

            if (exito) {
                // Notificar a la UI que el borrado fue exitoso
                _borradoExitoso.emit(true)
            } else {
                // Notificar a la UI que hubo un error
                _errorMensaje.value = "No se pudo eliminar el usuario. Inténtalo de nuevo."
            }
            // Indicar que la operación ha terminado
            _estaBorrando.value = false
        }
    }
    // En tu archivo AppVM.kt

    fun actualizarUsuario(idUsuario: Int, usuario: Usuario) {
        // validaciones ALGUIEN HAGA MEJOR ESTO PORFA ESTA HORRIBLE COMO LA HICE XD
        if (!usuario.nombre.matches(Regex("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$"))) {
            _errorMensaje.value = "Error, el nombre solo puede contener letras."
            return // Detiene la ejecución si el nombre es inválido
        }
        if (usuario.nombre == "") {
            _errorMensaje.value = "Error, el nombre no puede estar vacío."
            return
        }

        if (!usuario.apellidos.matches(Regex("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$"))) {
            _errorMensaje.value = "Error, el apellido solo puede contener letras."
            return // Detiene la ejecución si el nombre es inválido
        }
        if (usuario.apellidos == "") {
            _errorMensaje.value = "Error, el apellido no puede estar vacío."
            return
        }
        // Esta Regex es un estándar común para validar el formato de un email.
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        if (!usuario.correo.matches(emailRegex)) {
            _errorMensaje.value = "Error, el formato del correo no es válido."
            return // Detiene la ejecución si el correo es inválido
        }
        // ---------------------------------------------------

        // Si todas las validaciones pasan, se ejecuta la actualización.
        viewModelScope.launch {
            // Limpiamos cualquier mensaje de error previo antes de intentar la operación
            _errorMensaje.postValue(null)

            val exito = servicioRemoto.actualizarUsuario(idUsuario, usuario)
            if (exito) {
                println("Usuario actualizado con éxito.")
                // Actualiza el LiveData con el nuevo objeto de usuario para que la UI reaccione
                _usuarioLogeado.postValue(usuario)
            } else {
                _errorMensaje.value = "No se pudo actualizar el usuario. Inténtalo de nuevo."
            }
        }
    }

    /**
     * Solicita la generación de un código QR para el usuario actualmente logueado.
     * Gestiona los estados de carga y actualiza el `qrBitmap` con la imagen recibida,
     * o `errorMensaje` si la operación falla.
     */
    fun generarQR() {
        // Obtenemos el ID del usuario que ya inició sesión.
        val idUsuario = _usuarioLogeado.value?.id ?: return

        viewModelScope.launch {
            // 1. Iniciar estado de carga y limpiar datos anteriores
            _cargandoQR.value = true
            _qrBitmap.value = null
            _errorMensaje.value = null

            try {
                // 2. Llamar al servicio remoto
                val response = servicioRemoto.generarQR(idUsuario)

                if (response.isSuccessful && response.body() != null) {
                    // 3. Procesar la respuesta exitosa
                    val responseBody = response.body()!!
                    // Convertir los bytes de la respuesta en un array
                    val bytes = responseBody.bytes()
                    // Decodificar el array de bytes a un Bitmap y luego a un ImageBitmap
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    _qrBitmap.value = bitmap?.asImageBitmap() // Actualizar el StateFlow

                } else {
                    // 4. Manejar respuesta de error del servidor
                    _errorMensaje.value = "Error al generar el QR. Código: ${response.code()}"
                }
            } catch (e: Exception) {
                // 5. Manejar errores de red u otras excepciones
                _errorMensaje.value = "Error de conexión: ${e.message}"
                e.printStackTrace()
            } finally {
                // 6. Finalizar estado de carga
                _cargandoQR.value = false
            }
        }
    }


}
