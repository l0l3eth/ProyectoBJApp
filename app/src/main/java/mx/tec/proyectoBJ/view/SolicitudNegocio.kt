package mx.tec.proyectoBJ.view

import android.annotation.SuppressLint
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import mx.tec.proyectoBJ.model.EstadoDeRegistro
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.proyectoBJ.blanco
import mx.tec.proyectoBJ.degradado
import mx.tec.proyectoBJ.morado

/**
 * Composable principal que gestiona un formulario de registro de negocio multipasos.
 *
 * Este componente orquesta varias sub-pantallas (`DatosDeNegocio`, `DatosDeContactoNegocio`, etc.)
 * para recolectar la información necesaria para registrar un nuevo negocio. Mantiene el estado
 * del formulario y la pantalla actual, y finalmente envía los datos a través del [AppVM]
 * una vez que todos los pasos se han completado.
 *
 * @param appVM La instancia del ViewModel [AppVM] utilizada para enviar los datos del nuevo usuario/negocio.
 * @param modifier El modificador a aplicar al contenedor principal.
 * Creado por: Estrella Lolbeth Téllez Rivas A01750496
 */
@Composable
fun RellenoDeSolicitud(appVM: AppVM, modifier: Modifier = Modifier){
    val totalPantallas = 4
    // Estado para controlar la pantalla visible (ej. 0 para DatosDeNegocio, 1 para Contacto, etc.)
    var pantallaActual by remember { mutableIntStateOf(0) }
    // Estado para almacenar todos los datos del formulario a medida que se completan.
    var estadoDeRegistro by remember { mutableStateOf(EstadoDeRegistro()) }

    /**
     * Función interna para actualizar el estado del formulario de registro.
     * @param newState El nuevo estado con la información actualizada.
     */
    fun onStateChange(newState: EstadoDeRegistro) {
        estadoDeRegistro = newState
    }

    /**
     * Función interna para navegar entre las diferentes pantallas del formulario.
     * @param pantalla El valor a sumar a la pantalla actual (-1 para retroceder, 1 para avanzar).
     */
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

            // Muestra condicionalmente cada paso del formulario basado en `pantallaActual`.
            DatosDeNegocio(
                mostrar = pantallaActual == 0,
                state = estadoDeRegistro,
                onStateChange = ::onStateChange,
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
            // Pantalla final: validación y envío de datos.
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
                    // Llama al ViewModel para enviar los datos al backend.
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
                }
            }
            // Muestra los botones de navegación en todas las pantallas.
            BotonesNegocio(pantallaActual = pantallaActual,
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
 * Un composable que muestra la pantalla para la creación de la contraseña del negocio.
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
        CampoDeTexto(etiqueta = "Contraseña",
            value = state.contrasena ?: "",
            onValueChange = { onStateChange(state.copy(contrasena = it)) })
    }
}

/**
 * Un composable que muestra un formulario para recolectar los datos de contacto del negocio.
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

/**
 * Un composable que muestra un formulario para recolectar la dirección del negocio.
 *
 * Esta pantalla es parte de un flujo de registro de varios pasos. Solicita al usuario
 * los detalles de su dirección. La visibilidad de la pantalla está controlada por
 * el parámetro `mostrar`.
 *
 * @param mostrar Una bandera booleana que controla si el composable debe mostrarse.
 * @param state El estado actual del formulario de registro.
 * @param onStateChange Callback que se invoca cuando el campo de dirección cambia,
 *                      actualizando el estado general.
 */
@Composable
fun DireccionNegocio(mostrar: Boolean = false,
                     state: EstadoDeRegistro,
                     onStateChange: (EstadoDeRegistro) -> Unit = {}) {
    if (mostrar) {
        TextoTitularRegistro("¿En dónde se ubica tu negocio?")
        TextoTitularRegistro("Sólo se aceptan direcciones de Atizapán.")
        CampoDeTexto(etiqueta = "Calle, número y colonia o fraccionamiento",
            value = state.direccion ?: "",
            onValueChange = { onStateChange(state.copy(direccion = it)) })
    }
}

/**
 * Un composable que muestra un formulario para recolectar los datos iniciales del negocio.
 *
 * Esta es la primera pantalla en el flujo de registro. Recolecta el nombre del negocio
 * y permite al usuario seleccionar el tipo de establecimiento a través de un menú desplegable.
 *
 * @param modifier El modificador a aplicar al layout de la columna.
 * @param mostrar Una bandera booleana que controla la visibilidad de este composable.
 * @param state El estado actual del formulario de registro.
 * @param onStateChange Una función de callback que se invoca cuando el valor del
 *                      campo de nombre cambia.
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
            // Se puede conectar onSelectionChange para guardar el tipo de establecimiento si es necesario
            TipoEstablecimientoDropdown(onSelectionChange = { /* onStateChange(state.copy(tipo = it)) */ })
        }
    }
}

/**
 * Un menú desplegable estilizado para seleccionar el tipo de establecimiento.
 *
 * Este Composable proporciona una lista predefinida de categorías de negocio en un
 * menú desplegable (`ExposedDropdownMenuBox`). Está diseñado para coincidir con la
 * apariencia de los `CampoDeTexto` de la aplicación.
 *
 * @param modifier El modificador a aplicar al contenedor del menú.
 * @param options La lista de strings a mostrar como opciones en el menú.
 * @param initialSelection El texto que se muestra por defecto antes de hacer una selección.
 * @param onSelectionChange Un callback que se invoca con la opción seleccionada por el usuario.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TipoEstablecimientoDropdown(
    modifier: Modifier = Modifier,
    options: List<String> = listOf("Entretenimiento", "Comida", "Salud", "Belleza", "Educación", "Moda y Accesorios", "Servicios"),
    initialSelection: String = "Tipo de establecimiento",
    onSelectionChange: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(initialSelection) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .height(56.dp),
            readOnly = true,
            value = selectedText,
            onValueChange = {},
            placeholder = {
                if (selectedText == initialSelection) {
                    Text(text = initialSelection, color = Color.Gray)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = White,
                unfocusedContainerColor = White,
                disabledContainerColor = White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Transparent,
                focusedLeadingIconColor = morado,
                unfocusedLeadingIconColor = morado,
                focusedTrailingIconColor = morado,
                unfocusedTrailingIconColor = morado,
                focusedTextColor = Color.Black.copy(alpha = 0.8f),
                unfocusedTextColor = Color.Black.copy(alpha = 0.8f)
            ),
            shape = RoundedCornerShape(28.dp)
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedText = selectionOption
                        isExpanded = false
                        onSelectionChange(selectionOption)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

/**
 * Un Composable de previsualización para el formulario de registro de negocio.
 *
 * Esta función permite a Android Studio renderizar una vista previa del `RellenoDeSolicitud`
 * sin necesidad de ejecutar la aplicación completa.
 */
@SuppressLint("ViewModelConstructorInComposable")
@Composable
@Preview
fun FormularioPreview(){
    RellenoDeSolicitud( appVM = AppVM() )
}
