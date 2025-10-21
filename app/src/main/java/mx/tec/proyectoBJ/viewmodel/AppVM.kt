package mx.tec.proyectoBJ.viewmodel

import android.graphics.BitmapFactory
import androidx.compose.runtime.State // Importación clave
import androidx.compose.runtime.mutableStateOf // Importación clave
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
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
import mx.tec.proyectoBJ.model.TarjetaNegocio
import mx.tec.proyectoBJ.model.Usuario

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
 * Autores: Estrella Lolbeth Téllez Rivas A01750496
 *          Allan Mauricio Brenes Castro  A01750747
 */

sealed class PantallaSplash {
    object NavegarAInicio : PantallaSplash()
}

class AppVM : ViewModel(){
    private val servicioRemoto = ServicioRemoto

    // ... (El resto de tus propiedades como _NavegarAInicio, _usuarioLogeado, etc., se mantienen igual)
    private val _NavegarAInicio = MutableSharedFlow<PantallaSplash>()
    val NavegarAInicio: SharedFlow<PantallaSplash> = _NavegarAInicio.asSharedFlow()

    private val _usuarioLogeado = MutableLiveData<Usuario?>(null)
    val usuarioLogeado: LiveData<Usuario?> = _usuarioLogeado

    private val _errorMensaje = MutableLiveData<String?>(null)
    val errorMensaje: LiveData<String?> = _errorMensaje

    private val _estaBorrando = MutableStateFlow(false)
    val estaBorrando: StateFlow<Boolean> = _estaBorrando.asStateFlow()

    private val _borradoExitoso = MutableSharedFlow<Boolean>()
    val borradoExitoso: SharedFlow<Boolean> = _borradoExitoso.asSharedFlow()

    private val _qrBitmap = MutableStateFlow<ImageBitmap?>(null)
    val qrBitmap: StateFlow<ImageBitmap?> = _qrBitmap.asStateFlow()

    private val _cargandoQR = MutableStateFlow(false)
    val cargandoQR: StateFlow<Boolean> = _cargandoQR.asStateFlow()

    /**
     * Estado privado y mutable: Solo el ViewModel puede modificar esta lista.
     * La UI observa esta lista para mostrar las tarjetas de negocio.
     */
    private val _listaNegocios = mutableStateOf<List<TarjetaNegocio>>(emptyList())
    // CORRECCIÓN: Se expone como State<T> que es de solo lectura para la UI. Esto está correcto.
    val listaNegocios: State<List<TarjetaNegocio>> = _listaNegocios

    /**
     * Estado privado y mutable para el estado de carga de los negocios.
     */
    private val _cargando = mutableStateOf(false)
    // CORRECCIÓN: Se expone como State<Boolean> de solo lectura. Esto también está correcto.
    val cargando: State<Boolean> = _cargando


    init {
        // Ejecuta la lógica de retardo y navegación al iniciar el ViewModel
        viewModelScope.launch {
            // Retraso para la pantalla de bienvenida
            delay(3000L)
            // Envía el evento de navegación para pasar a la siguiente pantalla
            _NavegarAInicio.emit(PantallaSplash.NavegarAInicio)
        }
        // Llamamos a la función para cargar los negocios cuando el ViewModel se crea.
        obtenerTarjetasNegocios()
    }

    // ... (Las funciones enviarUsuario, iniciarSesion, eliminarUsuario, actualizarUsuario y generarQR se mantienen igual)
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
        viewModelScope.launch {
            val resultadoUsuario = ServicioRemoto.iniciarSesion(correo, contrasena)
            if (resultadoUsuario != null) {
                _usuarioLogeado.value = resultadoUsuario as Usuario?
                _errorMensaje.value = null
            } else {
                _usuarioLogeado.value = null
                _errorMensaje.value = "Correo o contraseña incorrectos. Inténtalo de nuevo."
            }
        }
    }

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

    fun obtenerTarjetasNegocios() {
        viewModelScope.launch {
            // CORRECCIÓN: Para cambiar el valor, se usa la propiedad .value del MutableState.
            _cargando.value = true
            try {
                // Llamamos a la función correcta de tu servicio remoto.
                val resultado = ServicioRemoto.obtenerTarjetasNegocios()
                // CORRECCIÓN: Asignamos el resultado a la propiedad .value del MutableState.
                _listaNegocios.value = resultado

            } catch (e: Exception) {
                // Maneja el error. Considera usar el _errorMensaje para notificar a la UI.
                _errorMensaje.value = "Error al obtener negocios: ${e.message}"
                println("Error al obtener negocios: ${e.message}")
            } finally {
                // CORRECCIÓN: Finaliza el estado de carga usando la propiedad .value.
                _cargando.value = false
            }
        }
    }

    fun generarQR(idUsuario: Int) {
        viewModelScope.launch {
            _cargandoQR.value = true
            _errorMensaje.value = null
            _qrBitmap.value = null

            try {
                // ¡Llamada directa a tu nueva función en ServicioRemoto!
                val responseBody = ServicioRemoto.generarQR(idUsuario)

                if (responseBody != null) {
                    // Convierte la respuesta binaria a un Bitmap
                    val bytes = responseBody.bytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    _qrBitmap.value = bitmap.asImageBitmap() // Convierte a ImageBitmap para Compose
                } else {
                    _errorMensaje.value = "La respuesta del servidor para el QR está vacía."
                }

            } catch (e: Exception) {
                _errorMensaje.value = "Error de conexión: ${e.message}"
            } finally {
                _cargandoQR.value = false
            }
        }
    }
}
