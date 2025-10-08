package mx.tec.ptoyectobj.model

import mx.tec.proyectoBJ.model.Usuario
import mx.tec.ptoyectobj.URL_BASE
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

object ServicioRemoto {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val servicioUsuario by lazy {
        retrofit.create(UsuarioAPI::class.java)
    }

    suspend fun registrarUsuario(usuario: Usuario) {
        try {
            servicioUsuario.registrarUsuario(usuario)
        } catch (e: HttpException) {
            println("Error, codigo: ${e.code()}")
            println("Error, mensaje: ${e.message()}")
            println("Error, respuesta: ${e.response()}")
        } catch (e: Exception) {
            println("Error en la descarga: $e")
        }
    }
}