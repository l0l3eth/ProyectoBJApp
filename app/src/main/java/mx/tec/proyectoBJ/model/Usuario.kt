package mx.tec.proyectoBJ.model

import com.google.gson.annotations.SerializedName

data class Usuario(
    val nombre : String,
    val apellidos : String,
    val correo : String,
    val telefono : String,
    val contrasena : String, //CharArray,
    val direccion : String,
    val curp: String,
    val tipo_usuario : String = "JOVEN"
)
