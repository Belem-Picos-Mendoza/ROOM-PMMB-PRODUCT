package com.example.room_pmmb_product.ui.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.room_pmmb_product.Datos.Product
import com.example.room_pmmb_product.Datos.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete a product from the [ProductRepository]'s data source.
 */
class ProductDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val productId: Int = checkNotNull(savedStateHandle[ProductDetailsDestination.productIdArg])

    /**
     * Holds the product details UI state. The data is retrieved from [ProductRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<ProductDetailsUiState> =
        productRepository.getProductStream(productId)
            .filterNotNull()
            .map {
                ProductDetailsUiState(outOfStock = it.quantity <= 0, productDetails = it.toProductDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ProductDetailsUiState()
            )

    /**
     * Reduces the product quantity by one and update the [ProductRepository]'s data source.
     */
    fun reduceQuantityByOne() {
        viewModelScope.launch {
            val currentProduct = uiState.value.productDetails

            // Reducir la cantidad en 1 si es mayor que 0
            if (currentProduct.quantity > 0) {
                val updatedProduct = currentProduct.copy(quantity = currentProduct.quantity - 1)

                // Actualizar el producto en el repositorio
                productRepository.updateProduct(updatedProduct.toProduct())
            }
        }
    }

    /**
     * Deletes the product from the [ProductRepository]'s data source.
     */
    suspend fun deleteProduct() {
        productRepository.deleteProduct(uiState.value.productDetails.toProduct())
    }

    var productUiState by mutableStateOf(ProductUiState())
        private set

    private fun validateInput(uiState: ProductDetails = productUiState.productDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && description.isNotBlank() &&
                    category.isNotBlank() && price.isNotBlank() &&
                    quantity > 0 // Validar que quantity sea mayor que 0
        }
    }
    suspend fun saveProduct() {
        if (validateInput()) {
            productRepository.insertProduct(productUiState.productDetails.toProduct())
        }
    }


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * UI state for ProductDetailsScreen
 */
data class ProductDetailsUiState(
    val outOfStock: Boolean = true,
    val productDetails: ProductDetails = ProductDetails()
)