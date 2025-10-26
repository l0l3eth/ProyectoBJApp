package mx.tec.proyectoBJ.model

import com.google.gson.Gson

// Define la data class que representará los datos de inicio de sesión.
// Esto es más limpio que reutilizar la clase Usuario si solo necesitas correo y contraseña.
data class UsuarioInicio(
    val correo: String,
    val contrasena: String
) {
    companion object {
        /**
         * Convierte un objeto Usuario (que podría venir de otra parte de tu app)
         * a un String en formato JSON.
         * Útil para enviar los datos de inicio de sesión a tu API.
         */
        fun aJson(usuario: UsuarioInicio): String {
            val gson = Gson()
            return gson.toJson(usuario)
        }

        /**
         * Convierte un String en formato JSON a un objeto UsuarioInicio.
         * Útil si recibes los datos de inicio de sesión como un string.
         * @param jsonString La cadena de texto en formato JSON.
         * @return Un objeto UsuarioInicio o null si la conversión falla.
         */
        fun desdeJson(jsonString: String): UsuarioInicio? {
            return try {
                val gson = Gson()
                gson.fromJson(jsonString, UsuarioInicio::class.java)
            } catch (e: Exception) {
                // Imprime el error para debugging
                println("Error al convertir el JSON a objeto UsuarioInicio: ${e.message}")
                null // Retorna null para indicar que la conversión falló
            }
        }
    }
}
