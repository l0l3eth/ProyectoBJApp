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
            // Se ajusta para usar el Response y poder verificar el resultado
            val response = servicio.registrarUsuario(usuario)
            if (!response.isSuccessful) {
                println("Error al registrar. Código: ${response.code()}")
            }
        } catch (e: HttpException) {
            println("Error HTTP, codigo: ${e.code()}")
            println("Error HTTP, mensaje: ${e.message()}")
        } catch (e: Exception) {
            println("Error en la conexión: $e")
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
            val response = servicio.iniciarSesion(credenciales)
            if (response.isSuccessful) {
                response.body()
            } else {
                println("Error de login. Código: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            println("Error de conexión al intentar iniciar sesión: $e")
            null
        }
    }

    /**
     * Envía una petición a la API para eliminar un usuario por su ID.
     * @param idUsuario El ID del usuario que se desea eliminar.
     * @return `true` si el usuario fue eliminado exitosamente (código 2xx),
     *         `false` en caso contrario (error del servidor o de conexión).
     */
    suspend fun borrarUsuario(idUsuario: Int): Boolean {
        return try {
            val response = servicio.borrarUsuario(idUsuario)
            if (response.isSuccessful) {
                println("Usuario con ID $idUsuario borrado exitosamente.")
                true
            } else {
                println("Error al borrar el usuario. Código: ${response.code()}, Mensaje: ${response.message()}")
                false
            }
        } catch (e: HttpException) {
            println("Error HTTP al borrar usuario: ${e.message()}")
            false
        } catch (e: Exception) {
            println("Error de conexión al intentar borrar usuario: $e")
            false
        }
    }

    /**
     * Obtiene la lista completa de negocios desde la API.
     * @return Una `List<Negocio>` con los negocios obtenidos. Si ocurre un error
     *         (de red o HTTP), devuelve una lista vacía y registra el error en consola.
     */
    suspend fun obtenerNegocios(): List<Negocio> {
        try {
            return servicio.obtenerNegocios()
        } catch (e: HttpException) {
            println("Error HTTP, codigo: ${e.code()}")
            println("Error HTTP, mensaje: ${e.message()}")
        } catch (e: Exception) {
            println("Error en la descarga de negocios: $e")
        }
        return listOf()
    }

    /**
     * Obtiene la lista completa de productos disponibles desde la API.
     * En caso de error, la excepción será propagada a quien llame a esta función.
     * @return Una `List<Producto>` con todos los productos.
     */
    suspend fun obtenerProductos(): List<Producto> {
        // Se añade manejo de errores para mayor robustez
        return try {
            servicio.obtenerProductos()
        } catch (e: Exception) {
            println("Error al obtener productos: $e")
            emptyList()
        }
    }

    /**
     * Obtiene la lista completa de usuarios registrados desde la API.
     * En caso de error, la excepción será propagada a quien llame a esta función.
     * @return Una `List<Usuario>` con todos los usuarios.
     */
    suspend fun obtenerUsuarios(): List<Usuario> {
        // Se añade manejo de errores para mayor robustez
        return try {
            servicio.obtenerUsuarios()
        } catch (e: Exception) {
            println("Error al obtener usuarios: $e")
            emptyList()
        }
    }

    suspend fun actualizarUsuario(idUsuario: Int, usuario: Usuario): Boolean {
        return try {
            val response = servicio.actualizarUsuario(idUsuario, usuario)
            if (response.isSuccessful) {
                println("Usuario actualizado correctamente (ID: $idUsuario).")
                true
            } else {
                println("Error al actualizar usuario. Código: ${response.code()}, mensaje: ${response.message()}")
                false
            }
        } catch (e: HttpException) {
            println("Error HTTP al actualizar usuario: ${e.message()}")
            false
        } catch (e: Exception) {
            println("Error de conexión al intentar actualizar usuario: $e")
            false
        }
    }

}
