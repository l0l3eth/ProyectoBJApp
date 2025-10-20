package mx.tec.proyectoBJ.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.tec.proyectoBJ.model.Horarios
import mx.tec.ptoyectobj.fondoGris
import mx.tec.ptoyectobj.morado
import mx.tec.ptoyectobj.rosa

val sampleSchedule = listOf(
    Horarios("Lunes", "9:00 - 9:00, 18:00 - 22:00"),
    Horarios("Martes", "9:00 - 9:00, 18:00 - 22:00"),
    Horarios("Miércoles", "9:00 - 9:00, 18:00 - 22:00"),
    Horarios("Jueves", "9:00 - 12:00"),
    Horarios("Viernes", "11:00 - 20:00", esHoy = true), // Resaltado
    Horarios("Sábado", "9:00 - 9:00, 18:00 - 22:00"),
    Horarios("Domingo", "9:00 - 12:00"),
)

@Composable
fun NegocioProfileScreen(
    // Aquí iría el ViewModel si fuera necesario
) {
    Scaffold(
        bottomBar = { BarraNavegacion( onNavigateToInicio = {},
                                        onNavigateToMapa = {},
                                        onNavigateToPromociones = {},
                                        onNavigateToID = {}) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(fondoGris)
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
                TextButton(
                    onClick = { /* Por hacer: Navegar a pantalla de edición */ }
                ) {
                    Text(text = "Editar perfil",
                        color = rosa,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(top = 24.dp, bottom = 48.dp) // Espacio para el enlace
                    )
                }
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
            .background(morado)
            .padding(bottom = 0.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Fila superior (Menú y Título)
            BarraSuperior(userName)

            // Espacio negro para el banner/imagen del negocio
            Portada()
        }

        // Logos flotantes (Alineados al centro)
        Icono()
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
            .background(fondoGris) // Asegura que el fondo sea gris
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
fun ScheduleSection(schedule: List<Horarios>) {
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
                    text = item.dia,
                    fontSize = 14.sp,
                    fontWeight = if (item.esHoy) FontWeight.Bold else FontWeight.Normal,
                    color = if (item.esHoy) Color.Black else Color.Gray,
                    modifier = Modifier.weight(1f)
                )

                // Horario
                Text(
                    text = item.horario,
                    fontSize = 14.sp,
                    fontWeight = if (item.esHoy) FontWeight.Bold else FontWeight.Normal,
                    color = if (item.esHoy) rosa else Color.Black,
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