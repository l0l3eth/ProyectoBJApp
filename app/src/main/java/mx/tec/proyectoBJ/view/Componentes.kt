package mx.tec.ptoyectobj.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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

@Composable
fun CampoDeTexto(texto: String, modifier: Modifier = Modifier) {
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
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

@Composable
fun BotonCircular(icono: ImageVector, modifier: Modifier = Modifier, texto: String = "", tamano: Int = 100) {
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
fun TextoTitular(texto: String, modifier: Modifier = Modifier) {
    Text(texto,
        modifier = modifier.padding(16.dp),
        color = blanco,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold)
}
