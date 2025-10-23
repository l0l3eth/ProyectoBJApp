package mx.tec.proyectoBJ.model
import com.google.gson.annotations.SerializedName

data class Promocion(
    @SerializedName("id")
    val id:Int,
    @SerializedName("titulo")
    val titulo: String,
    @SerializedName("tipo_descuento")
    val tipo_descuento: String,
)