package mx.tec.proyectoBJ.model

// En tu archivo de modelos (ej. AuthResponse.kt o Usuario.kt)
data class AuthResponse(
    val token: String,
    val usuario: Usuario // <-- ¡Añade este campo!
)
