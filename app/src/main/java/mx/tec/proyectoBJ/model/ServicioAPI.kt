package mx.tec.proyectoBJ.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ServicioAPI {
    //Iniciar Sesión
    @POST("api/usuario") //falta cambiar el endpoint
    suspend fun iniciarSesion(
        @Body loginRequest: LoginRequest
    ): Response <AuthResponse>

    //Registrar Usuario
    @Headers ("Content-Type: application/json")
    @POST("/api/auth/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Void>


    //Lista de Negocios
    @GET("/api/negocios")
    suspend fun obtenerNegocios(): Response<List<Negocio>>

    //Datos Usuario para el ID digital
    @GET("/api/usuarios")
    suspend fun obtenerUsuarios(): Response<List<Usuario>>

}