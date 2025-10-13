package mx.tec.proyectoBJ.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ServicioAPI {
    //Iniciar Sesión
    @POST("api/usuario") //falta cambiar el endpoint
    suspend fun iniciarSesion(
        @Body correo: String, contrasena: String
    );

    //Registrar Usuario
    @POST("/api/auth/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario);

    //Lista de Negocios
    @GET("/api/negocios")
    suspend fun obtenerNegocios(): List<Negocio>

    //Datos Usuario para el ID digital
    @GET("/api/usuarios")
    suspend fun obtenerUsuarios(): List<Usuario>

}