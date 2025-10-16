package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Reemplaza esto con tu import real si usas resources
// import androidx.compose.ui.res.painterResource
// import mx.tec.proyectoBJ.R

// --- Definición de Colores ---
val DarkPurple = Color(0xFF38156E)
val PrimaryPink = Color(0xFFF06292) // Rosa para detalles
val GrayBackground = Color(0xFFF5F5F5)
val White = Color(0xFFFFFFFF)
val BlackPlaceholder = Color(0xFF000000) // Para el fondo del logo

// --- Modelo de Datos de Horarios ---
data class DailySchedule(
    val day: String,
    val schedule: String,
    val isToday: Boolean = false
)

val sampleSchedule = listOf(
    DailySchedule("Lunes", "9:00 - 9:00, 18:00 - 22:00"),
    DailySchedule("Martes", "9:00 - 9:00, 18:00 - 22:00"),
    DailySchedule("Miércoles", "9:00 - 9:00, 18:00 - 22:00"),
    DailySchedule("Jueves", "9:00 - 12:00"),
    DailySchedule("Viernes", "11:00 - 20:00", isToday = true), // Resaltado
    DailySchedule("Sábado", "9:00 - 9:00, 18:00 - 22:00"),
    DailySchedule("Domingo", "9:00 - 12:00"),
)

@Composable
fun NegocioProfileScreen(
    // Aquí iría el ViewModel si fuera necesario
) {
    Scaffold(
        bottomBar = { BarraNavegacion() },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(GrayBackground)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally // Centra el contenido horizontalmente
        ) {
            item {
                HeaderNegocio(userName = "Negocio")
            }

            item {
                BusinessInfoSection(
                    name = "JÖTUNHEIM",
                    description = "Academia de Artes Marciales"
                )
            }

            item {
                ScheduleSection(schedule = sampleSchedule)
            }

            item {
                // Botón/Enlace "Editar perfil"
                Text(
                    text = "Editar perfil",
                    color = PrimaryPink,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 48.dp) // Espacio para el enlace
                        .clickable { /* TODO: Navegar a pantalla de edición */ }
                )
            }
        }
    }
}

// ---------------------------------------------------------------------
// 1. HEADER ESPECÍFICO DEL NEGOCIO
// ---------------------------------------------------------------------

@Composable
fun HeaderNegocio(userName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            // No tiene las esquinas redondeadas en la parte inferior de la captura
            .background(DarkPurple)
            .padding(bottom = 0.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Fila superior (Menú y Título)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: Abrir Drawer de Navegación */ }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menú",
                        tint = White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Bienvenido $userName",
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Espacio negro para el banner/imagen del negocio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp) // Altura del espacio negro
                    .background(BlackPlaceholder)
            )
        }

        // Logos flotantes (Alineados al centro)
        Column(
            modifier = Modifier.align(Alignment.Center)
                .offset(y = 100.dp) // Baja los logos para que queden sobre la transición de color
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder del logo principal del negocio
            Box(
                modifier = Modifier
                    .size(80.dp) // Tamaño del logo
                    .clip(CircleShape)
                    .background(Color.White) // Círculo blanco de fondo
                    .border(2.dp, Color.White, CircleShape), // Borde blanco (si fuera necesario)
                contentAlignment = Alignment.Center
            ) {
                // Placeholder para el logo 'JOTUNHEIM'
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(Color.Red.copy(alpha = 0.5f)) // Simulación del logo interno
                )
                /* Si tuvieras el asset:
                Image(
                    painter = painterResource(id = R.drawable.logo_negocio),
                    contentDescription = "Logo del negocio",
                    modifier = Modifier.fillMaxSize()
                )
                */
            }
        }
    }

    // Relleno para que el contenido de abajo inicie después de los logos flotantes
    Spacer(modifier = Modifier.height(100.dp + 16.dp))
}

// ---------------------------------------------------------------------
// 2. INFORMACIÓN Y HORARIOS
// ---------------------------------------------------------------------

@Composable
fun BusinessInfoSection(name: String, description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(GrayBackground) // Asegura que el fondo sea gris
    ) {
        Text(
            text = name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        Text(
            text = description,
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ScheduleSection(schedule: List<DailySchedule>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(White, RoundedCornerShape(12.dp)) // Fondo blanco para la tabla
            .padding(16.dp)
    ) {
        schedule.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Día de la semana
                Text(
                    text = item.day,
                    fontSize = 14.sp,
                    fontWeight = if (item.isToday) FontWeight.Bold else FontWeight.Normal,
                    color = if (item.isToday) Color.Black else Color.Gray,
                    modifier = Modifier.weight(1f)
                )

                // Horario
                Text(
                    text = item.schedule,
                    fontSize = 14.sp,
                    fontWeight = if (item.isToday) FontWeight.Bold else FontWeight.Normal,
                    color = if (item.isToday) PrimaryPink else Color.Black,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

// ---------------------------------------------------------------------
// PREVIEW
// ---------------------------------------------------------------------

@Preview(showBackground = true)
@Composable
fun NegocioProfileScreenPreview() {
    NegocioProfileScreen()
}