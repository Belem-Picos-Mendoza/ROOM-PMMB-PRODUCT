package com.example.room_pmmb_product.Datos
import androidx.room.PrimaryKey
import androidx.room.Entity

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val category : String,
    val price : Double,
    val quantity : Float

//    •	ID: 101
//    •	Name: "Laptop"
//•	Description: "A high-performance laptop"
//•	Category: "Electronics"
//•	Price: 1500.99
//•	Quantity: 50


) {
    // Método para formatear el precio
    fun formatedPrice(): String {
        return "$${"%.2f".format(price)}"  // Formato monetario con 2 decimales
    }


}
