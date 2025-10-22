package mx.tec.proyectoBJ.model

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServicioAPI {
    //Iniciar Sesión
    @POST("/api/auth/login-token") //Ya cambio el endpoint //
    suspend fun iniciarSesion(
        @Body credenciales: LoginRequest
    ): Response<Usuario>

    //Registrar Usuario
    @Headers("Content-Type: application/json")
    @POST("/api/auth/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario):
            Response<Unit> // Es buena práctica esperar una respuesta


    //--Funciones con token--


    //Borrar Usuario por ID
    @DELETE("/api/auth/usuario/{id}")
    suspend fun borrarUsuario(
        @Header("Authorization") token: String,
        @Path("id") idUsuario: Int
    ): Response<Unit>

    //Actualizar Usuario por ID
    @PUT("/api/usuarios/{id}")
    suspend fun actualizarUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body usuario: Usuario
    ): Response<Unit>

    //Lista de Negocios
    @GET("/api/negocios/lista")
    suspend fun obtenerNegocios(
        @Header("Authorization") token: String
    ): Response<List<Negocio>>

    //Lista de Tarjetas de Negocios
    @GET("/api/negocios/lista")
    suspend fun obtenerTarjetasNegocios(
        @Header("Authorization") token: String
    ): List<TarjetaNegocio>

    //Datos Usuario para el ID digital
    @GET("/api/usuarios")
    suspend fun obtenerUsuarios(
        @Header("Authorization") token: String
    ): Response<List<Usuario>>

    //Lista de Productos
    @GET("/api/productos")
    suspend fun obtenerProductos(
        @Header("Authorization") token: String
    ): List<Producto>

    //Generación de QR
    @POST("/usuario/{id}/qr") // o @GET, dependiendo de cómo esté implementado tu backend
    suspend fun generarQR(
        @Header("Authorization") token: String,
        @Path("id") idUsuario: Int
    ): Response<ResponseBody>

    @GET("/api/promociones")
    suspend fun obtenerPromocionesNegocio(
        @Header("Authorization") token: String
    ): Response<List<Promocion>>

}
