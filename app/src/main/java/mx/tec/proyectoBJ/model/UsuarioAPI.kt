package mx.tec.ptoyectobj.model

import mx.tec.proyectoBJ.model.Usuario
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioAPI {
    @POST("/api/auth/registrar")
    suspend fun registrarUsuario(@Body usuario: Usuario);
}