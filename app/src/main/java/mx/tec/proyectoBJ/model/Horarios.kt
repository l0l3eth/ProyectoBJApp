package mx.tec.proyectoBJ.model

data class Horarios (
    val dia: String,
    val horario: String,
    val esHoy: Boolean = false
)
