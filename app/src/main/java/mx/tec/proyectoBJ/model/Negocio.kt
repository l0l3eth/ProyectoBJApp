package mx.tec.proyectoBJ.model

data class Negocio(
    val nombreNegocio: String,
    val numeroTelefono: String,
    val ubicacion: String,
    val correo: String,
    val contrasena: String = "",
    val descripcion: String,
    val horarios: List<Horarios>
)
