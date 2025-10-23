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

/**
 * Define la interfaz de Retrofit para interactuar con la API del backend.
 *
 * Esta interfaz utiliza anotaciones de Retrofit para mapear métodos de Kotlin a
 * endpoints HTTP específicos. Cada función corresponde a una operación de la API,
 * como iniciar sesión, registrar usuarios, obtener listas de datos, etc.
 * Las funciones están marcadas como `suspend` para ser utilizadas dentro de coroutines.
 *
 */
interface ServicioAPI {
    /**
     * Autentica a un usuario y solicita un token de acceso.
     * Envía las credenciales del usuario en el cuerpo de la solicitud POST.
     * @param credenciales Objeto [LoginRequest] que contiene el correo y la contraseña del usuario.
     * @return Un objeto [Response] que envuelve al [Usuario] si la autenticación es exitosa.
     */
    @POST("/api/auth/login-token")
    suspend fun iniciarSesion(
        @Body credenciales: LoginRequest
    ): Response<AuthResponse>

    /**
     * Registra un nuevo usuario en el sistema.
     * Envía todos los datos del nuevo usuario en el cuerpo de la solicitud.
     * @param usuario Objeto [Usuario] con la información completa para el registro.
     * @return Un objeto [Response] sin cuerpo (`Unit`) que indica el resultado de la operación (éxito o fracaso).
     */
    @Headers("Content-Type: application/json")
    @POST("/api/auth/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Unit>

    /**
     * Elimina un usuario específico del sistema basado en su ID.
     * @param idUsuario El ID del usuario que se desea eliminar.
     * @return Un [Response] sin cuerpo (`Unit`) indicando el resultado de la solicitud.
     */
    @DELETE("/api/auth/usuario/{id}")
    suspend fun borrarUsuario(
        @Header("Authorization") token: String,
        @Path("id") idUsuario: Int
    ): Response<Unit>

    /**
     * Actualiza la información de un usuario existente.
     * @param id El ID del usuario a actualizar.
     * @param usuario El objeto [Usuario] con los nuevos datos.
     * @return Un [Response] sin cuerpo (`Unit`) que confirma el resultado de la actualización.
     */
    @PUT("/api/usuarios/{id}")
    suspend fun actualizarUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body usuario: Usuario
    ): Response<Unit>

    /**
     * Obtiene una lista completa de todos los negocios registrados.
     * @return Un [Response] que contiene una lista de objetos [Negocio].
     */
    @GET("/api/negocios/lista")
    suspend fun obtenerNegocios(
        @Header("Authorization") token: String
    ): Response<List<Negocio>>

    /**
     * Obtiene una lista simplificada de negocios para mostrar como tarjetas.
     * @return Una lista de objetos [TarjetaNegocio]. Se asume que la respuesta es directa
     * y no está envuelta en un objeto `Response`.
     */
//    @GET("/api/negocios")
//    suspend fun obtenerTarjetasNegocios(
//        @Header("Authorization") token: String
//    ): List<TarjetaNegocio>

    /**
     * Obtiene una lista de todos los usuarios registrados.
     * Útil para la funcionalidad de la ID Digital si se necesita buscar o validar usuarios.
     * @return Un [Response] que contiene una lista de objetos [Usuario].
     */
    @GET("/api/usuarios")
    suspend fun obtenerUsuarios(
        @Header("Authorization") token: String
    ): Response<List<Usuario>>

    /**
     * Obtiene una lista de todos los productos disponibles.
     * @return Una lista de objetos [Producto].
     */
    @GET("/api/productos")
    suspend fun obtenerProductos(
        @Header("Authorization") token: String
    ): List<Producto>

    /**
     * Solicita la generación de una imagen de código QR para un usuario específico.
     * @param idUsuario El ID del usuario para el cual se generará el QR.
     * @return Un [Response] que contiene el cuerpo de la respuesta crudo ([ResponseBody]),
     * que se espera sea la imagen del código QR.
     */
    @GET("/usuario/{ID}/qr")
    suspend fun generarQR(
        @Header("Authorization") token: String,
        @Path("id") idUsuario: Int
    ): Response<ResponseBody>

    /**
     * Obtiene la lista de todas las promociones de los negocios.
     * @return Un [Response] que contiene una lista de objetos [Promocion].
     */
    @GET("/api/descuentos")
    suspend fun obtenerPromocionesNegocio(
        @Header("Authorization") token: String
    ): Response<List<Promocion>>

    /**
     * Solicita al backend que envíe un correo de restablecimiento de contraseña.
     * Se utiliza @FormUrlEncoded porque muchos endpoints de este tipo esperan el correo
     * como un campo de formulario y no como un JSON.
     *
     * @param correo El correo electrónico del usuario que solicita el restablecimiento.
     * @return Un Response<Unit> que indica si la solicitud fue exitosa.
     */
//    @FormUrlEncoded
//    @POST("/api/auth/forgot-password")
//    suspend fun solicitarRestablecimientoContrasena(
//        @com.google.android.gms.fitness.data.Field("email") correo: String
//    ): Response<Unit>
}
