package mx.tec.proyectoBJ.model

data class Negocio(
    var nombreNegocio: String,
    var numeroTelefono: String,
    var ubicacion: String,
    var correo: String,
    var contrasena: String = "",
    var icono: String = "",
    var portada: String = ""
)
