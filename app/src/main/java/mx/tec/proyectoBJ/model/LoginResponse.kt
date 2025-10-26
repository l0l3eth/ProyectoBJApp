package mx.tec.proyectoBJ.model

import com.google.gson.annotations.SerializedName

// Representa la estructura de la respuesta JSON del endpoint de login
data class LoginResponse(
    @SerializedName("token")
    val token: String?,

    @SerializedName("tipoUsuario")
    val tipoUsuario: TipoUsuario?
)
    