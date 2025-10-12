package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.proyectoBJ.model.EstadoDeRegistro
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.ptoyectobj.blanco
import mx.tec.ptoyectobj.morado

@Composable
fun IngresoDeDatos(appVM: AppVM, modifier: Modifier = Modifier) {
    // Pantallas requeridas para el registro.
    val totalPantallas = 4
    var pantallaActual by remember { mutableStateOf(0) }

    var estadoDeRegistro by remember { mutableStateOf(EstadoDeRegistro()) }

    // This function now updates the central state
    fun onStateChange(newState: EstadoDeRegistro) {
        estadoDeRegistro = newState
    }

    fun cambiarPantalla(pantalla: Int = 0) {
        pantallaActual += pantalla
    }

    Box(
        modifier = modifier.background(morado)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            LogoYTextoGrande()

            // Pass the current state and the update function to each screen
            DatosPersonales(
                mostrar = pantallaActual == 0,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange, // Pass the function reference
                modifier = modifier.padding(horizontal = 32.dp)
            )
            DatosDeContacto(
                mostrar = pantallaActual == 1,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange
            )
            Direccion(
                mostrar = pantallaActual == 2,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange
            )
            Contrasena(
                mostrar = pantallaActual == 3,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange
            )
            if (pantallaActual == 4) {
                // Here you can add validation before sending
                val isValid = estadoDeRegistro.nombre != null &&
                        estadoDeRegistro.apellido != null &&
                        estadoDeRegistro.correo != null &&
                        estadoDeRegistro.contrasena != null &&
                        estadoDeRegistro.direccion != null &&
                        estadoDeRegistro.curp != null &&
                        estadoDeRegistro.numeroTelefono != null

                if (isValid) {
                    TextoTitularRegistro("¡Todo listo! Enviando...")
                    appVM.enviarUsuario(
                        nombre = estadoDeRegistro.nombre!!,
                        apellido = estadoDeRegistro.apellido!!,
                        correo = estadoDeRegistro.correo!!,
                        contrasena = estadoDeRegistro.contrasena!!,
                        direccion = estadoDeRegistro.direccion!!,
                        curp = estadoDeRegistro.curp!!,
                        numeroTelefono = estadoDeRegistro.numeroTelefono!!
                    )
                } else {
                    TextoTitularRegistro("Faltan datos por llenar.")
                    // Optionally, you could show a button to go back
                }
            }
            Botones(pantallaActual = pantallaActual,
                totalPantallas = totalPantallas,
                onPantallaCambia = { cambiarPantalla(it) })
        }
    }
}

/**
 * Un composable que muestra los botones de navegación "Regresar" y "Siguiente".
 *
 * Este componente es parte de un flujo de registro de varias pantallas. Muestra condicionalmente
 * el botón "Regresar" si el usuario no está en la primera pantalla, y el botón "Siguiente"
 * si no está en la última. Al hacer clic, estos botones invocan una función de callback para
 * cambiar a la pantalla anterior o siguiente.
 *
 * @param pantallaActual El índice de la pantalla que se está mostrando actualmente en el flujo.
 * @param modifier El modificador a aplicar al layout de la fila de botones.
 * @param totalPantallas El número total de pantallas en el flujo de registro.
 * @param onPantallaCambia Una función de callback que se invoca cuando se hace clic en un botón
 *                         de navegación. Recibe un entero: `-1` para "Regresar" y `1` para "Siguiente".
 */
@Composable
fun Botones(pantallaActual : Int,
            modifier: Modifier = Modifier,
            totalPantallas: Int,
            onPantallaCambia: (Int) -> Unit = {}) {
    Row {
        if (pantallaActual > 0) {
            TextButton(
                onClick = { onPantallaCambia(-1) }
            ) {
                Text(
                    "Regresar",
                    textAlign = TextAlign.Center,
                    style = TextStyle(color = blanco)
                )
            }
        }
        Spacer(modifier = modifier.weight(0.8f))
        if (pantallaActual < totalPantallas) {
            Button(
                onClick = { onPantallaCambia(1) }
            ) {
                Text(
                    "Siguiente",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Un composable que muestra la pantalla para la creación de la contraseña del usuario.
 *
 * Esta pantalla es parte de un proceso de registro de varios pasos. Solicita al usuario que
 * cree una contraseña, que luego se almacena en el estado central del formulario de registro.
 * La visibilidad de esta pantalla está controlada por el parámetro `mostrar`.
 *
 * @param mostrar Una bandera booleana que controla la visibilidad de este composable. Si es `true`,
 *                la pantalla de contraseña se muestra; de lo contrario, se oculta.
 * @param state El estado actual del formulario de registro, que contiene todos los datos
 *              introducidos por el usuario en los pasos anteriores.
 * @param onStateChange Una función de callback que se invoca cuando el valor del campo de la
 *                      contraseña cambia. Recibe un objeto `EstadoDeRegistro` actualizado.
 */
@Composable
fun Contrasena(mostrar: Boolean = false,
               state: EstadoDeRegistro,
               onStateChange: (EstadoDeRegistro) -> Unit = {}) {
    if (mostrar) {
        TextoTitularRegistro("Crea una contraseña.")
        // Resaltar los requerimientos de seguridad de contraseña al usuario
        CampoDeTexto(etiqueta = "Contraseña",
            value = state.contrasena ?: "",
            onValueChange = { onStateChange(state.copy(contrasena = it)) })
        // CampoDeTexto("Confirmar contraseña")
    }
}

/**
 * Un composable que muestra un formulario para recolectar los datos de contacto del usuario.
 *
 * Esta pantalla forma parte de un flujo de registro de varios pasos. Solicita al usuario
 * su correo electrónico y número de teléfono. Los datos se gestionan a través de un
 * objeto de estado compartido y se actualizan mediante una función de callback.
 *
 * @param mostrar Una bandera booleana que controla la visibilidad de este composable. Si es `true`,
 *                el formulario se muestra; de lo contrario, se oculta.
 * @param state El estado actual del formulario de registro, que contiene todos los datos
 *              introducidos por el usuario hasta el momento.
 * @param onStateChange Una función de callback que se invoca cada vez que cambia el valor de un
 *                      campo de entrada. Recibe el objeto `EstadoDeRegistro` actualizado.
 */
@Composable
fun DatosDeContacto(mostrar: Boolean = true,
                    state: EstadoDeRegistro,
                    onStateChange: (EstadoDeRegistro) -> Unit = {}) {
    if (mostrar) {
        TextoTitularRegistro("Ingresa tus datos de contacto.")
        CampoDeTexto(etiqueta = "Correo electrónico",
            value = state.correo ?: "",
            onValueChange = { onStateChange(state.copy(correo = it)) })
        CampoDeTexto(etiqueta = "Número de teléfono",
            value = state.numeroTelefono ?: "",
            onValueChange = { onStateChange(state.copy(numeroTelefono = it)) })
    }
}

@Composable
fun Direccion(mostrar: Boolean = false,
              state: EstadoDeRegistro,
              onStateChange: (EstadoDeRegistro) -> Unit = {}) {
    if (mostrar) {
        TextoTitularRegistro("¿En dónde vives?")
        TextoTitularRegistro("Sólo se aceptan direcciones de Atizapán.")
        // Por hacer: verificar que la dirección pertenezca a Atizapán
        CampoDeTexto(etiqueta = "Calle, número y colonia o fraccionamiento",
            value = state.direccion ?: "",
            onValueChange = { onStateChange(state.copy(direccion = it)) })
    }
}

/**
 * Un composable que muestra un formulario para recolectar datos personales del usuario.
 *
 * Esta pantalla es parte de un proceso de registro de varios pasos. Recolecta el/los nombre(s),
 * apellidos y CURP del usuario. Los datos recolectados se gestionan a través de un objeto
 * de estado central.
 *
 * @param modifier El modificador a aplicar al layout.
 * @param mostrar Una bandera booleana que controla la visibilidad de este composable. Si es `true`,
 *                el formulario se muestra; de lo contrario, se oculta.
 * @param state El estado actual del formulario de registro, que contiene todos los datos
 *              introducidos por el usuario.
 * @param onStateChange Una función de callback que se invoca cuando el valor de cualquiera de los
 *                      campos de entrada cambia. Recibe un objeto `EstadoDeRegistro` actualizado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatosPersonales(modifier: Modifier = Modifier,
                    mostrar: Boolean = true,
                    state: EstadoDeRegistro,
                    onStateChange: (EstadoDeRegistro) -> Unit = {}) {
//    var menuFecha by remember { mutableStateOf(false) }
//    var nombres by remember { mutableStateOf("") }
//    var apellidos by remember { mutableStateOf("") }
//    var fechaNacimiento by remember { mutableStateOf("") }
//    var sexo by remember { mutableStateOf("") }

    if (mostrar) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            TextoTitularRegistro("Cuéntanos un poco sobre tí")
            CampoDeTexto(etiqueta = "Nombre(s)",
                value = state.nombre ?: "",
                onValueChange = { onStateChange(state.copy(nombre = it)) })
            CampoDeTexto(etiqueta = "Apellidos",
                value = state.apellido ?: "",
                onValueChange = { onStateChange(state.copy(apellido = it)) })
            CampoDeTexto(etiqueta = "CURP",
                value = state.curp ?: "",
                onValueChange = { onStateChange(state.copy(curp = it)) })
//            Button(onClick = { menuFecha = true }) {
//                Text(fechaNacimiento.ifEmpty { "Fecha de nacimiento" })
//            }
//            DatePickerModal(onDateSelected = { onStateChange(state.copy(fechaNacimiento = it)) },
//                onDismiss = { menuFecha = false },
//                menuFecha = menuFecha)
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun IngresoDeDatosPreview() {
//    IngresoDeDatos()
//}
