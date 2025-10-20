package mx.tec.ptoyectobj

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Modelo

const val URL_BASE = "http://54.144.192.111:8080" //Cambiar http por httpsis

// Vista

// Colores
val morado = Color(0xFF38156E)
val rosa = Color(red = 243, green = 26, blue = 138, alpha = 255)
val naranja = Color(red = 250, green = 77, blue = 103, alpha = 255)
val blanco = Color(0xFFFFFFFF)

val degradado = Brush.horizontalGradient(listOf(rosa, naranja))