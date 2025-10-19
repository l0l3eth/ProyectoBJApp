package mx.tec.proyectoBJ.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import mx.tec.proyectoBJ.viewmodel.AppVM
import mx.tec.ptoyectobj.morado
import mx.tec.ptoyectobj.naranja
import mx.tec.ptoyectobj.rosa

// --- Modelo de Datos para las Opciones ---
data class UserOption(
    val label: String,
    val hasEditIcon: Boolean,
    val action: () -> Unit = {}
)

@Composable
fun ActualizarDatos( appVM: AppVM,
    onMenuClick: () -> Unit = {},
    // onBack ahora es para navegación simple, no para un logout completo
    onBack: () -> Unit = {},
    // onLogoutSuccess nos permite navegar fuera después de que el VM confirme el borrado
    onLogoutSuccess: () -> Unit = {}
) {
    // 1. Estado para controlar la visibilidad del diálogo de confirmación
    var mostrarDialogo by remember { mutableStateOf(false) }

    // 2. Observamos el estado de carga desde el ViewModel
    val estaBorrando by appVM.estaBorrando.collectAsState()

    // 3. Efecto para reaccionar cuando el usuario ha sido borrado con éxito
    LaunchedEffect(Unit) {
        appVM.borradoExitoso.collect { exito ->
            if (exito) {
                // Si el borrado fue exitoso, navegamos a la pantalla de inicio
                onLogoutSuccess()
            }
        }
    }

    // Obtenemos los datos del usuario logeado desde el ViewModel
    val usuario by appVM.usuarioLogeado.observeAsState()

    // Creamos la lista de opciones dinámicamente con los datos del usuario
    val userOptions = listOfNotNull(
        usuario?.nombre?.let { UserOption("USUARIO: $it", true) },
        usuario?.correo?.let { UserOption("CORREO: $it", true) },
        usuario?.id?.let { UserOption("FOLIO: $it", false) },
        UserOption("CONTRASEÑA", false) // Asumimos que esto lleva a otra pantalla
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(morado) // Fondo principal morado
    ) {
        // Formas de fondo (sin cambios)
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

        // Líneas blancas decorativas (sin cambios)
        // Línea superior
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(x = 8.dp, y = (-170).dp)
                .size(400.dp)
                .clip(CircleShape)
                .border(1.dp, White, CircleShape)
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
            items(userOptions.size) { index ->
                UserSettingItem(option = userOptions[index])
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

                    // 4. Se envuelve el texto en un Row para mostrar el indicador de carga
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable(
                                enabled = !estaBorrando, // Deshabilita el click mientras carga
                                onClick = { mostrarDialogo = true } // 5. Al hacer click, muestra el diálogo
                            )
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Cerrar sesión",
                            color = naranja,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        // Muestra el indicador de carga si se está borrando el usuario
                        if (estaBorrando) {
                            Spacer(modifier = Modifier.width(8.dp))
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = naranja,
                                strokeWidth = 2.dp
                            )
                        }
                    }
                }
            }
        }
    }

    // 6. Si el estado es true, se muestra el diálogo
    if (mostrarDialogo) {
        ConfirmarSalida(
            appVM = appVM,
            onDismissRequest = {
                // Permite cerrar el diálogo solo si no está en proceso de borrado
                if (!estaBorrando) {
                    mostrarDialogo = false
                }
            }
        )
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

            // Icono de Edición (solo para Usuario y Correo)
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
        HorizontalDivider(thickness = 1.dp, color = White.copy(alpha = 0.3f))
    }
}

// ---------------------------------------------------------------------
// PREVIEW
// ---------------------------------------------------------------------

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun UserSettingsScreenPreview() {
    ActualizarDatos( appVM = AppVM())
}

