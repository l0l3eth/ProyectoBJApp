package mx.tec.proyectoBJ.model

class IdDigital(
    val folio: String = "",
    var foto: String = "",
    var nombre: String = "",
    var imagenQR: String = ""
) {
    fun generarQR(): String {
        return ""
    }
}