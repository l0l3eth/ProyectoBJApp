package mx.tec.proyectoBJ.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("idUsuario")
    val idUsuario: Int,

    val nombre: String,
    val apellidos: String,

    @SerializedName("correo")
    val correo: String,

    val telefono: String,
    val contrasena: String? = null,
    val direccion: String,
    val curp: String,

    val tipoUsuario: TipoUsuario,

    val token: String? = null
)
