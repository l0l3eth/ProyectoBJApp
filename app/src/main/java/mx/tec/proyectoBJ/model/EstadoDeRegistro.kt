package mx.tec.proyectoBJ.model

/**
 * Representa el estado de los datos introducidos por un usuario durante el proceso de registro.
 *
 * Esta clase de datos se utiliza para almacenar de forma temporal la información del formulario de registro
 * a medida que el usuario avanza por los diferentes pasos.
 *
 * @property nombre El nombre del usuario.
 * @property apellido El apellido del usuario.
 * @property fechaNacimiento La fecha de nacimiento del usuario en formato de texto (String).
 * @property genero El género del usuario, utilizando el enum [Genero].
 * @property curp La Clave Única de Registro de Población (CURP) del usuario.
 * @property numeroTelefono El número de teléfono del usuario.
 * @property correo La dirección de correo electrónico del usuario.
 * @property direccion La dirección postal del usuario.
 * @property contrasena La contraseña elegida por el usuario.
 * Creado por: Allan Mauricio Brenes Castro  A01750747
 */
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
