package mx.tec.proyectoBJ.model

import mx.tec.ptoyectobj.URL_BASE
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import android.util.Log

object ServicioRemoto {
    // Para obtener el mensaje completo HTTP del servidor
    private val logging = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY }
        else{
            HttpLoggingInterceptor.Level.NONE
        }
    }
    private val certificatePinner = CertificatePinner.Builder()
        .add(".example.com", "sha256/ZC3lTYTDBJQVf1P2V7+uEqXx4P/vU3Dc8GTxTQ==")
        //cambiar "exaample.com por el dominio de la página y el hash real del certificado"
        .build()


    private val cliente = okhttp3.OkHttpClient.Builder()
        .addInterceptor(logging)
        .certificatePinner(certificatePinner)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE) //Cambiar a HTTPS
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val servicio by lazy {
        retrofit.create(ServicioAPI::class.java)
    }

    suspend fun registrarUsuario(usuario: Usuario) {
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
    }

    suspend fun obtenerNegocio(): List<Negocio> {
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
