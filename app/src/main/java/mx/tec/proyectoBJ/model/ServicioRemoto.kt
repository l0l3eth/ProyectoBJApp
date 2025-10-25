package mx.tec.proyectoBJ.model

import mx.tec.proyectoBJ.URL_BASE
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import android.util.Log
import com.google.maps.android.ktx.BuildConfig
import retrofit2.HttpException

/**
 * Objeto singleton para gestionar las comunicaciones con el servidor remoto (API).
 *
 * Se encarga de configurar Retrofit para realizar las llamadas HTTP,
 * incluyendo el manejo de errores y la conversión de datos JSON a objetos Kotlin.
 * Todas las operaciones de red son funciones de suspensión (suspend) para ser
 * llamadas desde corutinas.
 * Autores: Estrella Lolbeth Téllez Rivas A01750496
 *          Allan Mauricio Brenes Castro  A01750747
 */
object ServicioRemoto {

    /**
     * Interceptor de logging para OkHttp.
     * Registra el cuerpo completo de las peticiones y respuestas HTTP.
     * Es útil para depurar las comunicaciones de red.
     */
    private val logging = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    /**
     * Cliente OkHttp personalizado.
     * Incluye el interceptor de logging para poder visualizar las trazas de red
     * en el Logcat de Android Studio.
     */

    private val certificatePinner = okhttp3.CertificatePinner.Builder()
        .build()

    private val cliente = okhttp3.OkHttpClient.Builder()
        .addInterceptor(logging)
        .certificatePinner(certificatePinner)
        .build()

    /**
     * Instancia de Retrofit configurada para comunicarse con la API.
     * Utiliza `lazy` para que la inicialización se realice solo una vez, cuando se
     * accede por primera vez.
     * - `baseUrl`: La URL base de la API, definida en otra parte del proyecto.
     * - `client`: El cliente OkHttp personalizado con logging.
     * - `addConverterFactory`: Usa Gson para convertir automáticamente las respuestas JSON
     *   en objetos de datos Kotlin.
     */
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE) //Cambiar a HTTPS
            // .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * Implementación de la interfaz `ServicioAPI` generada por Retrofit.
     * Contiene los métodos que definen los endpoints de la API.
     * También se inicializa de forma `lazy`.
     */
    private val servicio by lazy {
        retrofit.create(ServicioAPI::class.java)
    }

    /**
     * Envía una petición a la API para registrar un nuevo usuario.
     * Maneja excepciones de red y HTTP, imprimiendo los detalles del error en la consola.
     * @param usuario El objeto `Usuario` con los datos a registrar.
     */
    suspend fun registrarUsuario(usuario: Usuario): Boolean {
        return try {
            val response = servicio.registrarUsuario(usuario)
            println("Registro de usuario exitoso")
            if (response.isSuccessful) {
                Log.d("Registro de usuario", "Registro exitoso")
                true
            } else {
                Log.e(
                    "Registro",
                    "Error al registrar usuario, código: ${response.errorBody()?.string()}"
                )
                false
            }
        } catch (e: Exception) {
            Log.e("Registro", "Error en la conexión: ${e.message}")
            false
        }
    }

    suspend fun iniciarSesion(correo:String, contrasena:String): Any? {
        val request= LoginRequest(correo=correo, contrasena=contrasena)
        try{
            val response=servicio.iniciarSesion(request)
            if(response.isSuccessful){
                val authResponse = response.body()
                if (authResponse != null) {
                    // Asignamos el token al objeto usuario que vamos a devolver.
                    // Es buena práctica que el objeto Usuario contenga su propio token.
                    val usuarioConToken = authResponse.usuario.copy(token = authResponse.token)
                    return usuarioConToken
                }
                if(authResponse!=null){
                    println("Inicio de sesión exitoso, token: ${authResponse.token}")
                    authResponse
                } else{
                    println("Respuesta exitosa, cuerpo vacío")
                    null
                }
            }else{
                val errBody=response.errorBody()?.string()
                println("Error de inicio de sesión: ${response.code()}. Mensaje del server: $errBody")
                null
            }
        } catch(e: Exception) {
            println("Error en la descarga: $e")
            null
        }
        return null
    }


    /**
     * Envía una petición a la API para eliminar un usuario por su ID.
     * @param idUsuario El ID del usuario que se desea eliminar.
     * @return `true` si el usuario fue eliminado exitosamente (código 2xx),
     *         `false` en caso contrario (error del servidor o de conexión).
     */
    suspend fun borrarUsuario(token: String, idUsuario: Int): Result<Unit> {
        return try {
            val response = servicio.borrarUsuario(token,idUsuario)
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("ServicioRemoto", "Error en la conexión: $e")
            Result.failure(e)
        }
    }

    suspend fun actualizarUsuario(token: String,idUsuario: Int, usuario: Usuario) {
        try {
            val response = servicio.actualizarUsuario(token,idUsuario, usuario)
            if (response.isSuccessful) {
                Log.d("ServicioRemoto","Usuario $idUsuario actualizado correctamente")
            } else {
                Log.e("ServicioRemoto", "Error al actualizar usuario. Código: " +
                        "${response.code()}, mensaje: ${response.message()}")
                throw HttpException(response)
            }
        } catch (e: Exception) {
            Log.e("ServicioRemoto", "Error en la conexión: ${e.message}")
            throw e
        }
    }

    suspend fun obtenerTarjetasNegocios(token: String):
            List<TarjetaNegocio> {
        try {
            val response=servicio.obtenerNegocios(token)
            if (response.isSuccessful){
                return (response.body() ?: emptyList())
                        as List<TarjetaNegocio>
            } else{
                Log.e("Error al obtener negocios",
                    "Código: ${response.code()}")
                throw HttpException(response)
            }
        } catch (e: Exception){
            Log.e("ServicioRemoto", "Error en la " +
                    "conexión al obtener negocios: ${e.message}")
            throw e
        }
    }


    suspend fun obtenerUsuarios(token: String): List<Usuario> {
        try{
            val response=servicio.obtenerUsuarios(token)
            if(response.isSuccessful){
                return response.body() ?: emptyList()
            }else{
                Log.e("Error al obtener usuarios",
                    "Código: ${response.code()}")
                throw HttpException(response)
            }
        }catch(e: Exception){
            Log.e("Error en la conexión", "Mensaje: $e")
            throw e
        }
    }


    suspend fun obtenerPromocionesNegocio(token: String): List<Promocion> {
        try{
            val response = servicio.obtenerPromocionesNegocio(token)
            if (response.isSuccessful){
                return response.body() ?: emptyList()
            }else{
                Log.e("Error al obtener promociones", "Código: ${response.code()}")
                throw HttpException(response)
            }
        }catch(e:Exception){
            Log.e("Fallo de conexión",
                "Mensaje: ${e.message}")
            throw e
        }
    }

    suspend fun generarQR(token: String, idUsuario: Int): okhttp3.ResponseBody {
        try {
            val respuesta = servicio.generarQR(token, idUsuario)

            if (respuesta.isSuccessful) {
                val cuerpoRespuesta = respuesta.body()
                if (cuerpoRespuesta != null) {
                    Log.d("ServicioRemoto", "QR generado exitosamente para el usuario $idUsuario.")
                    return cuerpoRespuesta
                } else {
                    // Lanzamos una excepción específica si el cuerpo es nulo inesperadamente.
                    throw NullPointerException("La respuesta fue exitosa pero el cuerpo del QR está vacío.")
                }
            } else {

                val errorBody = respuesta.errorBody()?.string()
                Log.e("ServicioRemoto", "Error al generar QR. Código: ${respuesta.code()}, Mensaje: $errorBody")
                throw HttpException(respuesta)
            }
        } catch (e: Exception) {

            Log.e("ServicioRemoto", "Excepción al generar QR: ${e.message}")
            throw e
        }
    }
}
