package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mx.tec.proyectoBJ.model.EstadoDeRegistro
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.ptoyectobj.blanco
import mx.tec.ptoyectobj.degradado
import mx.tec.ptoyectobj.morado

@Composable
fun RellenoDeSolicitud(appVM: AppVM, modifier: Modifier = Modifier){
    val totalPantallas = 4
    var pantallaActual by remember { mutableStateOf(0) }
    var estadoDeRegistro by remember { mutableStateOf(EstadoDeRegistro()) }

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
        ) {
            LogoYTextoGrande()

            DatosDeNegocio(
                mostrar = pantallaActual == 0,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange, // Pass the function reference
                modifier = modifier.padding(horizontal = 32.dp)
            )
            DatosDeContactoNegocio(
                mostrar = pantallaActual == 1,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange
            )
            DireccionNegocio(
                mostrar = pantallaActual == 2,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange
            )
            ContrasenaNegocio(
                mostrar = pantallaActual == 3,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange
            )
            if (pantallaActual == 4) {
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
fun BotonesNegocio(pantallaActual : Int,
            modifier : Modifier = Modifier,
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
            Box(
                modifier = modifier.background(
                    degradado,
                    shape = RoundedCornerShape(100.dp)
                )
                    .clickable{ onPantallaCambia(1)}
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ){
                Text(
                    "Siguiente",
                    textAlign = TextAlign.Center,
                    color = White
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
fun ContrasenaNegocio(mostrar: Boolean = false,
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
fun DatosDeContactoNegocio(mostrar: Boolean = true,
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
fun DireccionNegocio(mostrar: Boolean = false,
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
fun DatosDeNegocio(modifier: Modifier = Modifier,
                    mostrar: Boolean = true,
                    state: EstadoDeRegistro,
                    onStateChange: (EstadoDeRegistro) -> Unit = {}) {

    if (mostrar) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            TextoTitularRegistro("Cuéntanos un poco sobre tí")
            CampoDeTexto(etiqueta = "Nombre del negocio",
                value = state.nombre ?: "",
                onValueChange = { onStateChange(state.copy(nombre = it)) })
            TipoEstablecimientoDropdown(onSelectionChange = {})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipoEstablecimientoDropdown(
    modifier: Modifier = Modifier,
    // Lista de opciones a mostrar en el menú
    options: List<String> = listOf("Entretenimiento", "Comida", "Salud", "Belleza", "Educación", "Moda y Accesorios", "Servicios"),
    // Valor inicial o seleccionado
    initialSelection: String = "Tipo de establecimiento",
    // Callback para devolver la opción seleccionada
    onSelectionChange: (String) -> Unit
) {
    // 1. Estado para controlar si el menú está expandido
    var isExpanded by remember { mutableStateOf(false) }

    // 2. Estado para el texto seleccionado que se muestra en el campo
    var selectedText by remember { mutableStateOf(initialSelection) }

    // Utiliza ExposedDropdownMenuBox para manejar la lógica de apertura/cierre
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp)) // Esquinas redondeadas
    ) {
        // 3. Campo de Texto (simula el input)
        // Usamos un OutlinedTextField para la apariencia de tu pantalla
        OutlinedTextField(
            // El modificador 'menuAnchor()' es OBLIGATORIO en el TextField
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .height(56.dp),
            readOnly = true, // No permite que el usuario escriba, solo selecciona
            value = selectedText,
            onValueChange = {}, // No hace nada ya que es de solo lectura
            label = null, // No se usa etiqueta flotante en tu diseño
            placeholder = {
                // Muestra un placeholder solo si el texto es el inicial
                if (selectedText == initialSelection) {
                    Text(text = initialSelection, color = Color.Gray)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                disabledContainerColor = White,
                focusedBorderColor = Color.Transparent, // Sin borde visible
                unfocusedBorderColor = Color.Transparent, // Sin borde visible
                cursorColor = Color.Transparent,
                focusedLeadingIconColor = morado,
                unfocusedLeadingIconColor = morado,
                focusedTrailingIconColor = morado,
                unfocusedTrailingIconColor = morado,
                focusedTextColor = Color.Black.copy(alpha = 0.8f),
                unfocusedTextColor = Color.Black.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(28.dp) // Esquinas redondeadas del campo
        )

        // 4. El Menú Desplegable que aparece al hacer clic
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }, // Cierra si se toca fuera
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedText = selectionOption // 1. Actualiza el texto mostrado
                        isExpanded = false             // 2. Cierra el menú
                        onSelectionChange(selectionOption) // 3. Notifica al componente padre
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}