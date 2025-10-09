<<<<<<<< HEAD:app/src/main/java/mx/tec/proyectoBJ/view/model/Folio.kt
package mx.tec.proyectoBJ.view.model
========
package mx.tec.proyectoBJ.model
>>>>>>>> Registro:app/src/main/java/mx/tec/proyectoBJ/model/Folio.kt

enum class Aceptacion {
    PENDIENTE, VALIDADO, RECHAZADO
}

data class Fecha(
    val dia: Int = 0,
    val mes: Int = 0,
    val ano: Int = 0
)

class Folio(
    private val idFolio: String = "",
    private val estado: Aceptacion = Aceptacion.PENDIENTE,
    private val fechaCreacion: Fecha
) {
    fun confirmarUso(): Boolean {
        return true
    }

    fun verificarEstado(): Aceptacion {
        return Aceptacion.valueOf("PENDIENTE")
    }
}