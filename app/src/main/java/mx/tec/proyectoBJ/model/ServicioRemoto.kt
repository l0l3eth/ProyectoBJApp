package mx.tec.proyectoBJ.model

import mx.tec.ptoyectobj.URL_BASE
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

/**
 * Objeto singleton para gestionar las comunicaciones con el servidor remoto (API).
 *
 * Se encarga de configurar Retrofit para realizar las llamadas HTTP,
 * incluyendo el manejo de errores y la conversión de datos JSON a objetos Kotlin.
 * Todas las operaciones de red son funciones de suspensión (suspend) para ser
 * llamadas desde corutinas.
 */
object ServicioRemoto {

    /**
     * Interceptor de logging para OkHttp.
     * Registra el cuerpo completo de las peticiones y respuestas HTTP.
     * Es útil para depurar las comunicaciones de red.
     */
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Cliente OkHttp personalizado.
     * Incluye el interceptor de logging para poder visualizar las trazas de red
     * en el Logcat de Android Studio.
     */
    private val cliente = okhttp3.OkHttpClient.Builder()
        .addInterceptor(logging)
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
            .baseUrl(URL_BASE)
            .client(cliente)
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
    suspend fun registrarUsuario(usuario: Usuario) {
        try {
            servicio.registrarUsuario(usuario)
        } catch (e: HttpException) {
            println("Error, codigo: ${e.code()}")
            println("Error, mensaje: ${e.message()}")
            println("Error, respuesta: ${e.response()}")
        } catch (e: Exception) {
            println("Error en la descarga: $e")
        }
    }

    /**
     * Envía las credenciales del usuario a la API para iniciar sesión.
     * @param correo El correo electrónico del usuario.
     * @param contrasena La contraseña del usuario.
     * @return Un objeto `Usuario` si el inicio de sesión es exitoso (código HTTP 2xx),
     *         o `null` si las credenciales son incorrectas, hay un error en el servidor
     *         o problemas de conexión.
     */
    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        val credenciales = mapOf("correo" to correo, "contrasena" to contrasena)

        return try {
            // Llama a la función del proxy de Retrofit
            val response = servicio.iniciarSesion(credenciales)

            // Verifica el código HTTP
            if (response.isSuccessful) {
                // Si es 2xx, devuelve el cuerpo (el objeto Usuario)
                response.body()
            } else {
                // Código HTTP 4xx o 5xx (ej: credenciales incorrectas)
                println("Error de login. Código: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            // Errores de red (ej: sin internet)
            println("Error de conexión al intentar iniciar sesión: $e")
            null
        }
    }

    /**
     * Obtiene la lista completa de negocios desde la API.
     * @return Una `List<Negocio>` con los negocios obtenidos. Si ocurre un error
     *         (de red o HTTP), devuelve una lista vacía y registra el error en consola.
     */
    suspend fun obtenerNegocio(): List<Negocio> {
        try {
            val lista = servicio.obtenerNegocios()
            return lista
        } catch (e: HttpException) {
            println("Error, codigo: ${e.code()}")
            println("Error, mensaje: ${e.message()}")
            println("Error, respuesta: ${e.response()}")
        } catch (e: Exception) {
            println("Error en la descarga: $e")
        }
        return listOf()
    }

    /**
     * Obtiene la lista completa de productos disponibles desde la API.
     * En caso de error, la excepción será propagada a quien llame a esta función.
     * @return Una `List<Producto>` con todos los productos.
     */
    suspend fun obtenerProductos(): List<Producto> {
        return servicio.obtenerProductos()
    }

    /**
     * Obtiene la lista completa de usuarios registrados desde la API.
     * En caso de error, la excepción será propagada a quien llame a esta función.
     * @return Una `List<Usuario>` con todos los usuarios.
     */
    suspend fun obtenerUsuarioID(): List<Usuario> {
        return servicio.obtenerUsuarios()
    }
}
