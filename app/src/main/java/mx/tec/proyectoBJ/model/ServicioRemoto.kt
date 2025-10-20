package mx.tec.proyectoBJ.model

import mx.tec.ptoyectobj.URL_BASE
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import android.util.Log
import com.google.maps.android.ktx.BuildConfig

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
            HttpLoggingInterceptor.Level.BODY }
        else{
            HttpLoggingInterceptor.Level.NONE
        }
    }

    /**
     * Cliente OkHttp personalizado.
     * Incluye el interceptor de logging para poder visualizar las trazas de red
     * en el Logcat de Android Studio.
     */
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
    suspend fun registrarUsuario(usuario: Usuario): Boolean {
       return try {
            val response= servicio.registrarUsuario(usuario)
            println("Registro de usuario exitoso")
            if (response.isSuccessful){
                Log.d("Registro de usuario", "Registro exitoso")
                true
            }else{
                Log.e("Registro",
                    "Error al registrar usuario, código: ${response.errorBody()?.string()}")
                false
                }
            }
       catch (e: Exception) {
                  Log.e("Registro", "Error en la conexión: ${e.message}")
                  false
       }
    }

    suspend fun iniciarSesion(correo:String, contrasena:String):String?{
        val request= LoginRequest(correo=correo, contrasena=contrasena)
        return try{
            val response=servicio.iniciarSesion(request)
            if(response.isSuccessful){
                val authResponse=response.body()
                if(authResponse!=null){
                    println("Inicio de sesión exitoso, token: ${authResponse.token}")
                    return authResponse.token
                } else{
                    println("Respuesta exitosa, cuerpo vacío")
                    return null
                }
            }else{
                val errBody=response.errorBody()?.string()
                println("Error de inicio de sesión: ${response.code()}. Mensaje del server: $errBody")
                return null
            }
        } catch(e: Exception) {
            println("Error en la descarga: $e")
            return null
        }
        return listOf()
    }

    suspend fun obtenerTarjetasNegocios(): List<TarjetaNegocio> {
        try {
            val response=servicio.obtenerNegocios()
            if (response.isSuccessful){
                return response.body() ?: listOf()
            } else{
                println("Error al obtener negocios, codigo: ${response.code()}")
            }
        } catch (e: Exception){
            println("Error en la conexión al obtener negocios: $e")
        }
        return listOf()
    }


    suspend fun obtenerUsuariID(): List<Usuario> {
        try{
            val response=servicio.obtenerUsuarios()
            if(response.isSuccessful){
                return response.body() ?: listOf()
            }else{
                Log.e("Error al obtener usuarios", "Código: ${response.code()}")
            }
        }catch(e: Exception){
            Log.e("Error en la conexión", "Mensaje: $e")
        }
        return listOf()
    }

}
