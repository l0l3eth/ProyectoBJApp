package mx.tec.proyectoBJ.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.proyectoBJ.R
import mx.tec.ptoyectobj.blanco
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Un composable que muestra un `OutlinedTextField` estilizado con un borde redondeado.
 * Este componente está diseñado para la entrada de texto dentro de la aplicación.
 *
 * @param value El texto actual que se mostrará en el campo de texto.
 * @param modifier El modificador que se aplicará al `OutlinedTextField`. Por defecto es `Modifier`.
 * @param etiqueta El texto que se mostrará como etiqueta para el campo de texto.
 * @param onValueChange Una función callback que se activa cuando el usuario modifica el texto en el campo. Proporciona el nuevo valor de texto como un `String`.
 */
@Composable
fun CampoDeTexto(value: String,
                 modifier: Modifier = Modifier,
                 etiqueta: String,
                 onValueChange: (String) -> Unit) {
    // 1. You need a state to hold the text field's value.

    OutlinedTextField(
        // 2. The `value` parameter expects the current string to display.
        value = value,
        // 3. The `onValueChange` lambda gives you the new string when the user types.
        //    You must update your state here.
        onValueChange = onValueChange,
        // The label parameter was already correct!
        label = { Text(etiqueta) },
        modifier = modifier
            .border(
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(1.dp, Color.Black)
            )
            .background(color = blanco,
                shape = RoundedCornerShape(32.dp)), // It's good practice to apply the modifier.
        shape = RoundedCornerShape(32.dp)
    )
}

// Guardado por si se utiliza para escoger fecha.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    menuFecha: Boolean = false
) {
    val datePickerState = rememberDatePickerState()
    if (menuFecha) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        // Format the date to YYYY-MM-DD
                        val sdf = SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                        val formattedDate = sdf.format(Date(millis))
                        onDateSelected(formattedDate)
                    }
                    onDismiss()
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

/**
 * Un composable que muestra un botón circular con un icono y un texto opcional debajo de él.
 * El botón tiene una forma de círculo y organiza su contenido (icono y texto) en una columna vertical centrada.
 *
 * @param icono El `ImageVector` que se mostrará como el icono principal dentro del botón.
 * @param modifier El modificador que se aplicará al `Button`. Por defecto es `Modifier`.
 * @param texto El texto opcional que se mostrará debajo del icono. Por defecto es una cadena vacía.
 * @param tamano El tamaño (ancho y alto) del botón en dp. Por defecto es 100.
 */
@Composable
fun BotonCircular(icono: ImageVector,
                  modifier: Modifier = Modifier,
                  texto: String = "",
                  tamano: Int = 100) {
    Button(onClick = {},
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        modifier = modifier
            .padding(0.dp)
            .size(tamano.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxSize()
        ) {
            Icon(
                icono,
                contentDescription = null,
                modifier = modifier
                    .padding(0.dp)
                    .size(50.dp)
            )
            Text(
                texto,
                modifier = modifier
                    .padding(0.dp)
                    .size(55.dp)
            )
        }
    }
}

@Composable
fun LogoYTextoPequeño() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp), // Padding superior para el logo
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Contenedor circular blanco para el logo 'b'
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            //logo de beneficio
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de Beneficio Joven",
                modifier = Modifier.size(55.dp)
            )

        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("BENEFICIO")
                }
                    append("JOVEN")
            },
            color = blanco,
            fontSize = 20.sp,
        )
    }
}

@Composable
fun LogoYTextoGrande(){
    // Logo y texto "BENEFICIO JOVEN" (Centrado en la parte superior)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Contenedor circular blanco para el logo 'b'
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(Color.White, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            //logo de beneficio
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de Beneficio Joven",
                modifier = Modifier.size(75.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("BENEFICIO")
                }
                append("JOVEN")
            },
            color = blanco,
            fontSize = 24.sp
        )
    }
}

@Composable
fun TextoTitularRegistro(texto: String, modifier: Modifier = Modifier) {
    Text(texto,
        modifier = modifier.padding(16.dp),
        color = blanco,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold)
}

