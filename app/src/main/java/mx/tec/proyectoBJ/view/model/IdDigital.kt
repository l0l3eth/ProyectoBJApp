package mx.tec.proyectoBJ.view.model

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