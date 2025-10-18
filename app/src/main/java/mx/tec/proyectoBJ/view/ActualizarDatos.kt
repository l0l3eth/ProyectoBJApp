package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.ptoyectobj.morado
import mx.tec.ptoyectobj.naranja
import mx.tec.ptoyectobj.rosa


// --- Modelo de Datos para las Opciones ---
data class UserOption(
    val label: String,
    val hasEditIcon: Boolean,
    val action: () -> Unit = {}
)

val userOptions = listOf(
    UserOption("USUARIO", true),
    UserOption("CORREO", true),
    UserOption("FOLIO", false),
    UserOption("CONTRASEÑA", false) // El botón de edición de contraseña se gestionaría en la acción
)

// ---------------------------------------------------------------------
// COMPOSABLE PRINCIPAL
// ---------------------------------------------------------------------

@Composable
fun ActualizarDatos(
    // Aquí puedes pasar acciones reales desde el ViewModel o el NavController
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(morado) // Fondo principal morado
    ) {
        // Formas de fondo
        //Forma rosa inferior de arriba
        Box(
            modifier = Modifier
                .offset(x = (-40).dp, y = (-120).dp) // El offset negativo mueve el óvalo hacia arriba, fuera de la pantalla
                .size(400.dp)
                .clip(CircleShape)
                .background(rosa)

        )
        //Forma naranja superior de arriba
        Box(
            modifier = Modifier
                .offset(x = 5.dp, y = (-190).dp)
                .size(width = 800.dp, height = 400.dp) // Define un tamaño de óvalo (casi un círculo en este caso)
                .clip(CircleShape) // La forma circular crea un óvalo al ajustar el tamaño
                .background(naranja)

        )

        //Forma rosa inferior de abajo
        Box(
            modifier = Modifier
                .offset(x = (-150).dp, y = 700.dp)
                .size(400.dp)
                .clip(CircleShape)
                .background(rosa)

        )
        //Forma naranja superior de abajo
        Box(
            modifier = Modifier
                .offset(x = 20.dp, y = 700.dp)
                .size(400.dp)
                .clip(CircleShape)
                .background(naranja)

        )

        // Líneas blancas decorativas
        // Línea superior
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = 8.dp, y = (-170).dp)
                .size(400.dp)
                .clip(CircleShape)
                .border(1.dp, Color.White, CircleShape)
        )
        // Línea inferior
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 100.dp, y = 200.dp)
                .size(400.dp)
                .clip(CircleShape)
                .border(1.dp, White, CircleShape)
        )

        // --- Contenido Central (Opciones y Botones) ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 100.dp, bottom = 48.dp) // Ajuste para evitar el header
        ) {

            // Opciones del perfil (Usuario, Correo, Folio, Contraseña)
            userOptions.forEach { option ->
                item {
                    UserSettingItem(option = option)
                }
            }

            // Espacio de separación
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Opciones de acción (Regresar, Cerrar sesión)
            item {
                Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                    Text(
                        text = "Regresar",
                        color = White.copy(alpha = 0.9f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable(onClick = onBack)
                            .padding(vertical = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Cerrar sesión",
                        color = naranja,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clickable(onClick = onLogout)
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------
// COMPONENTE DE ITEM DE CONFIGURACIÓN
// ---------------------------------------------------------------------

@Composable
fun UserSettingItem(option: UserOption) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = option.action) // Hace todo el item clicable
            .padding(horizontal = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Etiqueta de la opción
            Text(
                text = option.label,
                color = White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Icono de Edición (solo para Usuario y Correo en la captura)
            if (option.hasEditIcon) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar ${option.label}",
                    tint = White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        // Divisor (Línea)
        Divider(color = White.copy(alpha = 0.3f), thickness = 1.dp)
    }
}

// ---------------------------------------------------------------------
// PREVIEW
// ---------------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun UserSettingsScreenPreview() {
    ActualizarDatos()
}