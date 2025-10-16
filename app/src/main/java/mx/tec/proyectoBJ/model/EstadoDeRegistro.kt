package mx.tec.proyectoBJ.model

data class EstadoDeRegistro(
    val nombre: String? = null,
    val apellido: String? = null,
    val fechaNacimiento: String? = null,
    val genero: Genero? = null,
    val curp: String? = null,
    val numeroTelefono: String? = null,
    val correo: String? = null,
    val direccion: String? = null,
    val contrasena: String? = null
)

