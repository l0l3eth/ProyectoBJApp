package mx.tec.proyectoBJ.viewmodel

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import mx.tec.proyectoBJ.model.Promocion
import mx.tec.proyectoBJ.model.ServicioRemoto
import mx.tec.proyectoBJ.model.TarjetaNegocio
import mx.tec.proyectoBJ.model.Usuario
import java.io.IOException // Importante para un manejo de errores más específico

/**
 * ViewModel principal de la aplicación (`AppVM`).
 *
 * Se encarga de la lógica de negocio y de gestionar el estado de la UI para
 * funcionalidades clave como:
 * - Control de la pantalla de bienvenida (splash screen).
 * - Registro e inicio de sesión de usuarios.
 * - Eliminación y actualización de usuarios.
 * - Generación de códigos QR.
 * - Carga de tarjetas de negocio.
 *
 * Comunica el resultado de las operaciones a la UI a través de LiveData y StateFlow,
 * permitiendo una arquitectura reactiva y desacoplada.
 * Autores: Estrella Lolbeth Téllez Rivas A01750496
 *          Allan Mauricio Brenes Castro  A01750747
 */

sealed class PantallaSplash {
    object NavegarAInicio : PantallaSplash()
}

class AppVM : ViewModel() {
    private val servicioRemoto = ServicioRemoto

    // Flujo para la navegación después de la pantalla de bienvenida
    private val _navegarAInicio = MutableSharedFlow<PantallaSplash>()
    val navegarAInicio: SharedFlow<PantallaSplash> = _navegarAInicio.asSharedFlow()

    // Estado del usuario que ha iniciado sesión
    private val _usuarioLogeado = MutableLiveData<Usuario?>(null)
    val usuarioLogeado: LiveData<Usuario?> = _usuarioLogeado

    // Mensajes de error para mostrar en la UI
    private val _errorMensaje = MutableLiveData<String?>(null)
    val errorMensaje: LiveData<String?> = _errorMensaje

    // Estado para operaciones de borrado
    private val _estaBorrando = MutableStateFlow(false)
    val estaBorrando: StateFlow<Boolean> = _estaBorrando.asStateFlow()

    private val _borradoExitoso = MutableSharedFlow<Boolean>()
    val borradoExitoso: SharedFlow<Boolean> = _borradoExitoso.asSharedFlow()

    // Estado y datos para la generación del código QR
    private val _qrBitmap = MutableStateFlow<ImageBitmap?>(null)
    val qrBitmap: StateFlow<ImageBitmap?> = _qrBitmap.asStateFlow()

    private val _cargandoQR = MutableStateFlow(false)
    val cargandoQR: StateFlow<Boolean> = _cargandoQR.asStateFlow()

    // Estado y datos para la lista de tarjetas de negocio
    private val _listaNegocios = mutableStateOf<List<TarjetaNegocio>>(emptyList())
    val listaNegocios: State<List<TarjetaNegocio>> = _listaNegocios

    private val _cargandoNegocios = mutableStateOf(false)
    val cargandoNegocios: State<Boolean> = _cargandoNegocios

    private val _promociones=MutableStateFlow<List<Promocion>>(emptyList())
    val promociones=_promociones.asStateFlow()

    private val _estaCargando=MutableStateFlow(false)
    val estaCargando=_estaCargando.asStateFlow()

    private val _error=MutableStateFlow<String?>(null)
    val error=_error.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            }
        // Cargar los negocios al iniciar el ViewModel
        obtenerTarjetasNegocios()
        cargarPromociones()
    }

    fun enviarUsuario(
        nombre: String,
        apellido: String,
        correo: String,
        contrasena: String,
        direccion: String,
        numeroTelefono: String,
        curp: String
    ) {
        viewModelScope.launch {
            // Aquí también podrías añadir validaciones antes de enviar
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
            _estaBorrando.value = true
            _errorMensaje.value = null

            val exito = servicioRemoto.borrarUsuario(idUsuario)
            if (exito) {
                _borradoExitoso.emit(true)
            } else {
                _errorMensaje.value = "No se pudo eliminar el usuario. Inténtalo de nuevo."
            }
            _estaBorrando.value = false
        }
    }

    fun actualizarUsuario(idUsuario: Int, usuario: Usuario) {
        // CORRECCIÓN: Validaciones mejoradas y centralizadas al inicio de la función.
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")

        if (usuario.nombre.isBlank() || usuario.apellidos.isBlank() || usuario.correo.isBlank()) {
            _errorMensaje.value = "Error, ningún campo puede estar vacío."
            return
        }
        if (!usuario.nombre.matches(Regex("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+\$"))) {
            _errorMensaje.value = "Error, el nombre solo puede contener letras y espacios."
            return
        }
        if (!usuario.apellidos.matches(Regex("^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+\$"))) {
            _errorMensaje.value = "Error, los apellidos solo pueden contener letras y espacios."
            return
        }
        if (!usuario.correo.matches(emailRegex)) {
            _errorMensaje.value = "Error, el formato del correo no es válido."
            return
        }

        viewModelScope.launch {
            _errorMensaje.postValue(null)
            val exito = servicioRemoto.actualizarUsuario(idUsuario, usuario)
            if (exito) {
                println("Usuario actualizado con éxito.")
                _usuarioLogeado.postValue(usuario)
            } else {
                _errorMensaje.value = "No se pudo actualizar el usuario. Inténtalo de nuevo."
            }
        }
    }

    /**
     * CORRECCIÓN: Se unificaron las funciones `generarQR` en una sola.
     * Genera un código QR para el usuario que ha iniciado sesión.
     * Si necesitas generar un QR para otro usuario, puedes crear otra función como `generarQROtroUsuario(id: Int)`.
     */
    fun generarQR() {
        // Se obtiene el ID del usuario logeado. Si no hay, la función termina.
        val idUsuario = _usuarioLogeado.value?.id ?: run {
            _errorMensaje.value = "No se ha iniciado sesión para generar un QR."
            return
        }

        viewModelScope.launch {
            _cargandoQR.value = true
            _qrBitmap.value = null
            _errorMensaje.value = null

            try {
                // Se asume que ServicioRemoto.generarQR devuelve ResponseBody?
                val responseBody = servicioRemoto.generarQR(idUsuario)

                if (responseBody != null) {
                    val bytes = responseBody.bytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    _qrBitmap.value = bitmap?.asImageBitmap()
                } else {
                    _errorMensaje.value = "No se pudo obtener el código QR del servidor."
                }
            } catch (e: IOException) {
                // Error de red o de I/O
                _errorMensaje.value = "Error de conexión al generar QR: ${e.message}"
                e.printStackTrace()
            } catch (e: Exception) {
                // Otro tipo de error
                _errorMensaje.value = "Ocurrió un error inesperado al generar QR: ${e.message}"
                e.printStackTrace()
            } finally {
                _cargandoQR.value = false
            }
        }
    }

    fun obtenerTarjetasNegocios() {
        viewModelScope.launch {
            _cargandoNegocios.value = true
            try {
                _listaNegocios.value = servicioRemoto.obtenerTarjetasNegocios()
            } catch (e: Exception) {
                _errorMensaje.value = "Error al obtener negocios: ${e.message}"
                println("Error al obtener negocios: ${e.message}")
            } finally {
                _cargandoNegocios.value = false
            }
        }
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
