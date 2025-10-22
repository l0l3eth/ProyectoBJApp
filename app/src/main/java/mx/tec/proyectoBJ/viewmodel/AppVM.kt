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
import java.io.IOException

/**
 * ViewModel principal de la aplicación que actúa como el centro de la lógica de negocio.
 *
 * Esta clase se encarga de preparar y gestionar los datos para la UI, reaccionando a las
 * interacciones del usuario y comunicándose con la capa de datos (a través de [ServicioRemoto]).
 * Expone el estado de la aplicación a los Composables mediante el uso de [LiveData] y [StateFlow],
 * asegurando una arquitectura reactiva y desacoplada.
 *
 * ### Responsabilidades Clave:
 * - **Gestión de Sesión:** Maneja el inicio de sesión, registro, actualización y eliminación de usuarios.
 * - **Visualización de Datos:** Obtiene y gestiona listas de tarjetas de negocio y promociones.
 * - **Generación de Contenido:** Crea códigos QR para los usuarios.
 * - **Control de Flujo de UI:** Gestiona la navegación inicial después de la pantalla de bienvenida.
 * - **Manejo de Estado:** Proporciona estados de carga, éxito y error para las operaciones asíncronas,
 *   permitiendo que la UI reaccione de manera apropiada.
 *
 * @property usuarioLogeado Expone los datos del usuario que ha iniciado sesión.
 * @property listaNegocios Mantiene y expone la lista de tarjetas de negocio.
 * @property promociones Mantiene y expone la lista de promociones.
 * @property qrBitmap Contiene el [ImageBitmap] del código QR generado.
 * @property errorMensaje Proporciona mensajes de error para ser mostrados en la UI.
 *
 * Creado por: Estrella Lolbeth Téllez Rivas A01750496
               Allan Mauricio Brenes Castro A01750747
               Carlos Antonio Tejero Andrade A01801062
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

    // Estado y datos para la lista de promociones
    private val _promociones=MutableStateFlow<List<Promocion>>(emptyList())
    val promociones=_promociones.asStateFlow()

    // Estado de carga genérico para operaciones como la carga de promociones
    private val _estaCargando=MutableStateFlow(false)
    val estaCargando=_estaCargando.asStateFlow()

    // Estado de error genérico
    private val _error=MutableStateFlow<String?>(null)
    val error=_error.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            // Podrías emitir el evento de navegación aquí si fuera necesario
            // _navegarAInicio.emit(PantallaSplash.NavegarAInicio)
        }
        // Cargar los datos iniciales al crear el ViewModel
        obtenerTarjetasNegocios()
        cargarPromociones()
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param nombre Nombre del usuario.
     * @param apellido Apellidos del usuario.
     * @param correo Correo electrónico del usuario.
     * @param contrasena Contraseña para la nueva cuenta.
     * @param direccion Dirección del usuario.
     * @param numeroTelefono Número de teléfono del usuario.
     * @param curp CURP del usuario.
     */
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
     * Autentica a un usuario con su correo y contraseña.
     * Actualiza [_usuarioLogeado] si las credenciales son correctas, o [_errorMensaje] si fallan.
     *
     * @param correo Correo electrónico del usuario.
     * @param contrasena Contraseña del usuario.
     */
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

    /**
     * Elimina un usuario del sistema.
     *
     * @param idUsuario El ID del usuario a eliminar.
     */
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

    /**
     * Actualiza los datos de un usuario existente.
     * Realiza validaciones de formato y campos vacíos antes de enviar la solicitud.
     *
     * @param idUsuario El ID del usuario a actualizar.
     * @param usuario El objeto [Usuario] con la información actualizada.
     */
    fun actualizarUsuario(idUsuario: Int, usuario: Usuario) {
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
     * Genera un código QR para el usuario que ha iniciado sesión.
     * El resultado se almacena en [_qrBitmap].
     * Maneja los estados de carga y error durante el proceso.
     */
    fun generarQR() {
        val idUsuario = _usuarioLogeado.value?.id ?: run {
            _errorMensaje.value = "No se ha iniciado sesión para generar un QR."
            return
        }

        viewModelScope.launch {
            _cargandoQR.value = true
            _qrBitmap.value = null
            _errorMensaje.value = null

            try {
                val responseBody = servicioRemoto.generarQR(idUsuario)
                if (responseBody != null) {
                    val bytes = responseBody.bytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    _qrBitmap.value = bitmap?.asImageBitmap()
                } else {
                    _errorMensaje.value = "No se pudo obtener el código QR del servidor."
                }
            } catch (e: IOException) {
                _errorMensaje.value = "Error de conexión al generar QR: ${e.message}"
                e.printStackTrace()
            } catch (e: Exception) {
                _errorMensaje.value = "Ocurrió un error inesperado al generar QR: ${e.message}"
                e.printStackTrace()
            } finally {
                _cargandoQR.value = false
            }
        }
    }

    /**
     * Obtiene la lista de tarjetas de negocio del servidor y la almacena en [_listaNegocios].
     */
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

    /**
     * Carga la lista de promociones desde el servidor.
     * Actualiza los estados [_estaCargando], [_promociones] y [_error] según el resultado de la operación.
     */
    private fun cargarPromociones() {
        viewModelScope.launch {
            _estaCargando.value = true
            _error.value = null

            try {
                val listaDesdeServidor = ServicioRemoto.obtenerPromocionesNegocio()
                _promociones.value = listaDesdeServidor
            } catch (e: Exception) {
                Log.e("AppVM", "Error al cargar promociones: ${e.message}")
                _error.value = "No se pudieron cargar las promociones. Intenta más tarde."
            } finally {
                _estaCargando.value = false
            }
        }
    }
}
