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

    suspend fun registrarUsuario(usuario: Usuario): Result<Unit> {
        return try {
            servicio.registrarUsuario(usuario)
            Result.success(Unit)
        } catch (e: HttpException) {
            println("Error, codigo: ${e.code()}")
            println("Error, mensaje: ${e.message()}")
            println("Error, respuesta: ${e.response()}")
            Result.failure(e)
        } catch (e: Exception) {
            println("Error en la descarga: $e")
            Result.failure(e)
        }
    }

    suspend fun iniciarSesion(correo:String, contrasena:String){
        try{
            servicio.iniciarSesion(correo, contrasena)
        } catch(e: HttpException){
            println("Error, codigo: ${e.code()}")
            println("Error, mensaje: ${e.message()}")
            println("Error, respuesta: ${e.response()}")
        } catch(e: Exception) {
            println("Error en la descarga: $e")
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
        } catch (e: Exception){
            println("Error en la descarga: $e")
        }
        return listOf()
    }

    suspend fun obtenerUsuariID(): List<Usuario> {
        return servicio.obtenerUsuarios()
    }
}
