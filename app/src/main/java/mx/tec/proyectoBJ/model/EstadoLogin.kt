package mx.tec.proyectoBJ.model

sealed class EstadoLogin {
    object Idle : EstadoLogin()

    /** El proceso de inicio de sesión está en curso. La UI debería mostrar una carga. */
    object Loading : EstadoLogin()

    /** El inicio de sesión fue exitoso. La UI debería navegar a la siguiente pantalla. */
    data class Success(val tipoUsuario: TipoUsuario) : EstadoLogin()

    // El estado de error contiene un mensaje
    data class Error(val message: String) : EstadoLogin()
}