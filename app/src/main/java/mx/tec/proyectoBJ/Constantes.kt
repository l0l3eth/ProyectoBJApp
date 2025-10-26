package mx.tec.proyectoBJ

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Archivo que contiene constantes globales utilizadas a lo largo de la aplicación.
 *
 * Este objeto centraliza valores que no cambian, como la URL base de la API
 * y la paleta de colores de la marca, facilitando el mantenimiento y asegurando
 * la consistencia en toda la aplicación.
 */

// --- MODELO ---

/**
 * La URL base para todas las llamadas a la API del backend.
 * Es crucial para la capa de red (Retrofit) para saber a qué servidor apuntar.
 */
const val URL_BASE = "https://benejoven.hopto.org"

// --- VISTA ---

// --- Paleta de Colores ---

/**
 * Color primario principal de la aplicación. Utilizado para fondos, cabeceras y
 * elementos de marca importantes.
 */
val morado = Color(0xFF38156E)

/**
 * Color de acento utilizado en gradientes y elementos de llamada a la acción.
 */
val rosa = Color(red = 243, green = 26, blue = 138, alpha = 255)

/**
 * Color de acento secundario, comúnmente usado en conjunto con el rosa para crear gradientes.
 */
val naranja = Color(red = 250, green = 77, blue = 103, alpha = 255)

/**
 * Color blanco estándar. Usado para textos sobre fondos oscuros y superficies de tarjetas.
 */
val blanco = Color(0xFFFFFFFF)

/**
 * Un gris muy claro utilizado para los fondos de las pantallas principales,
 * proporcionando un contraste suave con los elementos de la UI.
 */
val fondoGris = Color(0xFFF5F5F5)

/**
 * Un gris claro para bordes, divisores o estados deshabilitados de componentes.
 */
val grisClaro = Color(0xFFE0E0E0)

/**
 * Un gradiente horizontal que transiciona del color [rosa] al [naranja].
 * Utilizado para botones prominentes, decoraciones y otros elementos visuales
 * que requieren un alto impacto.
 */
val degradado = Brush.horizontalGradient(listOf(rosa, naranja))
