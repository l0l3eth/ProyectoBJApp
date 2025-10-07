package mx.tec.proyectoBJ.model

data class Usuario(
    val nombre : String,
    val correo : String,
    val numeroTelefono : String,
    val curp : String,
    val foto : String,
    val genero : Genero,
    val contrasena : String,
    val direccion : String,
    val tiposNegocioFavorito: List<Negocio>
)
