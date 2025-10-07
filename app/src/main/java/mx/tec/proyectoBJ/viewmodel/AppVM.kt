package mx.tec.proyectoBJ.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import mx.tec.proyectoBJ.model.TOKEN_WEB

/**
 * AppVM (App ViewModel) es el ViewModel principal de la aplicación.
 * Gestiona el estado de autenticación del usuario (login/logout) utilizando Firebase Authentication.
 * Se encarga de la lógica  relacionada con el inicio y cierre de sesión.
 */

sealed class PantallaSplash {
    object NavegarAInicio : PantallaSplash()
}

class AppVM : ViewModel(){
    private val _NavegarAInicio = MutableSharedFlow<PantallaSplash>()
    val NavegarAInicio: SharedFlow<PantallaSplash> = _NavegarAInicio.asSharedFlow()

    init {
        // Ejecuta la lógica de retardo y navegación al iniciar el ViewModel
        viewModelScope.launch {
            // Retraso de menos de un segundo
            delay(100L)
            // Envía el evento de navegación
            _NavegarAInicio.emit(PantallaSplash.NavegarAInicio)
        }
    }

    // Instancia de FirebaseAuth para gestionar la autenticación con Firebase.
    private val auth = Firebase.auth

    // _estaLoggeado es un MutableStateFlow que mantiene el estado de autenticación actual.
    // Es privado para que solo pueda ser modificado desde este ViewModel.
    // MutableStateFlow es ideal para estados que cambian y necesitan ser observados por la UI.
    private val _estaLoggeado = MutableStateFlow(false)

    // estaLoggeado expone el estado de _estaLoggeado como un StateFlow de solo lectura.
    // La UI (por ejemplo, una vista de Compose) puede observar este flujo para reaccionar
    // a los cambios en el estado de autenticación (por ejemplo, navegar a otra pantalla).
    val estaLoggeado: StateFlow<Boolean> = _estaLoggeado

    /**
     * Inicia sesión en Firebase utilizando una credencial de autenticación (en este caso, de Google).
     * Se ejecuta dentro de una corrutina (viewModelScope) para no bloquear el hilo principal.
     *
     * @param credencial La credencial de autenticación obtenida de Google Sign-In.
     */
    fun hacerLoginGoogle(credencial: AuthCredential) =
        viewModelScope.launch {
            try {
                // Llama a Firebase para iniciar sesión con la credencial proporcionada.
                auth.signInWithCredential(credencial)
                    .addOnCompleteListener { task ->
                        // Este listener se ejecuta cuando la operación de inicio de sesión finaliza.
                        if (task.isSuccessful) {
                            // Si el inicio de sesión fue exitoso, actualiza el estado.
                            println("SignIn EXITOSO, ${auth.currentUser?.displayName}")
                            _estaLoggeado.value = true
                        } else {
                            // Si hubo un error, se mantiene el estado como no autenticado.
                            _estaLoggeado.value = false
                        }
                    }
                    .addOnFailureListener {
                        // Este listener se ejecuta si la operación falla (por ejemplo, por un problema de red).
                        println("ERROR al hacer login con Google")
                        _estaLoggeado.value = false
                    }
            } catch (e: Exception) {
                // Captura cualquier excepción que pueda ocurrir durante el proceso.
                println("EXCEPCIÓN al hacer login: ${e.localizedMessage}")
                _estaLoggeado.value = false
            }
        }

    /**
     * Cierra la sesión del usuario tanto en Firebase como en Google Sign-In.
     * Es importante cerrar sesión en ambos servicios para una desconexión completa.
     *
     * @param context El Contexto de la aplicación, necesario para obtener el GoogleSignInClient.
     */
    fun hacerLogout(context: Context) {
        // 1. Cierra la sesión en Firebase.
        // Esto invalida el token de autenticación de Firebase del usuario.
        FirebaseAuth.getInstance().signOut()

        // 2. Configura y cierra la sesión en Google Sign-In.
        // Esto revoca el acceso de la app a la cuenta de Google del usuario.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(TOKEN_WEB) // Se requiere el ID de cliente web para el backend de Firebase.
            .requestEmail()
            .build()

        // Obtiene el cliente de Google Sign-In.
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        googleSignInClient.signOut().addOnCompleteListener { task ->
            // Se asegura de que el estado de autenticación se actualice a 'false'
            // independientemente de si el cierre de sesión de Google fue exitoso o no.
            // La parte importante (Firebase) ya se ha cerrado.
            _estaLoggeado.value = false
            if (task.isSuccessful) {
                println("SignOut, Salió de Google")
            } else {
                println("SignOut, No puede salir de Google: ${task.exception?.message}")
            }
        }
    }
}
