package com.example.room_pmmb_product.Datos
import kotlinx.coroutines.flow.Flow

class OfflineProductRepository(private val productDao: ProductDao) : ProductRepository {
    override fun getAllProductStream(): Flow<List<Product>> = productDao.getAllProducts()

    override fun getProductStream(id: Int): Flow<Product?> = productDao.getProduct(id)

    override suspend fun insertProduct(product: Product) = productDao.insert(product)

    override suspend fun deleteProduct(product: Product) = productDao.delete(product)

    override suspend fun updateProduct(product: Product) = productDao.update(product)
}