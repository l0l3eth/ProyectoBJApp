package mx.tec.proyectoBJ.model
import com.google.gson.annotations.SerializedName

data class Promocion(
    @SerializedName("id_promocion")
    val id:Int,
    @SerializedName("nombre_promocion")
    val nombre: String,
    @SerializedName("descripcion_promocion")
    val descripcion: String,
    @SerializedName("fecha-fin-oferta")
    val imagen: String,
 //   @SerializedName("URL_imagen_promocion")
 //   val imagenPromocion: String

)