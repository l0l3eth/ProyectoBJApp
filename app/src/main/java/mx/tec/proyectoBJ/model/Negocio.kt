package mx.tec.proyectoBJ.model

data class Negocio(
    val nombreNegocio: String,
    val tarjetaNegocio: TarjetaNegocio,
    val numeroTelefono: String,
    val ubicacion: String,
    val horarios: Horarios,
    val correo: String,
    val contrasena: String
)
