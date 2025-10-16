package mx.tec.proyectoBJ.model

import mx.tec.ptoyectobj.URL_BASE
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

object ServicioRemoto {
    // Para obtener el mensaje completo HTTP del servidor
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val cliente = okhttp3.OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val servicio by lazy {
        retrofit.create(ServicioAPI::class.java)
    }

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

    suspend fun obtenerProductos(): List<Producto> {
        return servicio.obtenerProductos()
    }

    suspend fun obtenerUsuariID(): List<Usuario> {
        return servicio.obtenerUsuarios()
    }

}