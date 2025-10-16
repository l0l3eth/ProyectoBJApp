package mx.tec.proyectoBJ.model

data class Producto(
    val id: Int,
    val favorito: String,
    val nombre: String,
    val precio: Int,
    val stock: Int,
    val esta_activo: ProductoActivo,
)
