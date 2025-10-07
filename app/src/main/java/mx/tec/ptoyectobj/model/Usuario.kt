package mx.tec.ptoyectobj.model

enum class Genero() {
    MASCULINO,
    FEMENINO,
    NO_BINARIO
}

class Usuario(
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val telefono: String,
    val curp: String,
    val genero: Enum<Genero>,
    val contrasena: String,
    val direccion: String,
    val tiposNegocioFavorito: List<Enum<TiposNegocio>>
) {

}