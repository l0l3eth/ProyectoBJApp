package mx.tec.proyectoBJ.model

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServicioAPI {
    //Iniciar Sesión
    @POST("/api/auth/login")
    suspend fun iniciarSesion(
        @Body credenciales: Map<String, String>
    ): Response<Usuario>

    //Registrar Usuario
    @POST("/api/auth/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Unit> // Es buena práctica esperar una respuesta

    //Borrar Usuario por ID
    @DELETE("/api/auth/usuario/{id}")
    suspend fun borrarUsuario(@Path("id") idUsuario: Int): Response<Unit>

    //Actualizar Usuario por ID
    @PUT("/api/usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Int,
        @Body usuario: Usuario
    ): Response<Unit>

    //Lista de Negocios
    @GET("/api/negocios")
    suspend fun obtenerNegocios(): List<Negocio>

    //Lista de Tarjetas de Negocios
    @GET("/api/negocios")
    suspend fun obtenerTarjetasNegocios(): List<TarjetaNegocio>

    //Datos Usuario para el ID digital
    @GET("/api/usuarios")
    suspend fun obtenerUsuarios(): List<Usuario>

    //Lista de Productos
    @GET("/api/productos")
    suspend fun obtenerProductos(): List<Producto>

    //Generación de QR
    @POST("/usuario/{id}/qr") // o @GET, dependiendo de cómo esté implementado tu backend
    suspend fun generarQR(
        @Path("id") idUsuario: Int
    ): Response<ResponseBody>
}
