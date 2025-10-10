package mx.tec.proyectoBJ.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    val nombre : String,
    val apellidos : String,
    val correo : String,
    val telefono : String,
    val genero : Genero,
    val contrasena : String,
    val direccion : String,
    val curp: String,
    val fechaNacimiento : String,
    @SerializedName("tipo_usuario")
    val tipoUsuario : String = "JOVEN"
)
