package mx.tec.proyectoBJ.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    val id : Int? = null,
    @SerializedName("nombre")
    val nombre : String,
    @SerializedName("apellidos")
    val apellidos : String,
    @SerializedName("correo")
    val correo : String,
    @SerializedName("telefono")
    val telefono : String,
    @SerializedName("contrasena")
    val contrasena : String, //CharArray,
    @SerializedName("direccion")
    val direccion : String,
    @SerializedName("curp")
    val curp: String,
    val tipoUsuario : String = "JOVEN",
    val token : String? = null
)
