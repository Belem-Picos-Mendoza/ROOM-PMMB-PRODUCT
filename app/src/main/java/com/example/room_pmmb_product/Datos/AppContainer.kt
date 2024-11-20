package com.example.room_pmmb_product.Datos
import android.content.Context
interface AppContainer{
    val productRepository : ProductRepository
}

class AppDataContainer (private val context : Context) : AppContainer{
    override val productRepository: ProductRepository by lazy {
        OfflineProductRepository(Product_DB.getDatabase(context).ProductDao())
    }
}