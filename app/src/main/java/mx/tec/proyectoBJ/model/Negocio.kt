package mx.tec.proyectoBJ.model

/**
 * Representa la información de un negocio.
 *
 * Esta clase de datos se utiliza para almacenar los detalles de un negocio registrado en la aplicación.
 *
 * @property nombreNegocio El nombre oficial del negocio.
 * @property numeroTelefono El número de teléfono de contacto del negocio.
 * @property ubicacion La dirección o ubicación física del negocio.
 * @property correo La dirección de correo electrónico de contacto del negocio.
 * @property contrasena La contraseña para la cuenta del negocio. Por defecto, es una cadena vacía.
 * Creado por: Estrella Lolbeth Téllez Rivas A01750496
 */

data class Negocio(
    val nombreNegocio: String,
    val numeroTelefono: String,
    val ubicacion: String,
    val correo: String,
    val contrasena: String = "",
)
