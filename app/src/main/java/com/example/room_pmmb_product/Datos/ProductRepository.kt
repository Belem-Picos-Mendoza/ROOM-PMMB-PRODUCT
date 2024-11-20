package com.example.room_pmmb_product.Datos

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Item] from a given data source.
 */
interface ProductRepository {
    /**
     * Retrieve all the items from the the given data source.
     */
    fun getAllProductStream(): Flow<List<Product>>

    /**
     * Retrieve an item from the given data source that matches with the [id].
     */
    fun getProductStream(id: Int): Flow<Product?>

    /**
     * Insert item in the data source
     */
    suspend fun insertProduct(product: Product)

    /**
     * Delete item from the data source
     */
    suspend fun deleteProduct(product: Product)

    /**
     * Update item in the data source
     */
    suspend fun updateProduct(product: Product)
}
