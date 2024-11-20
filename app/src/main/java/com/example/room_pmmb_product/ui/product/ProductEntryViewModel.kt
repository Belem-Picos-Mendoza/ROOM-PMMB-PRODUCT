package com.example.room_pmmb_product.ui.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.room_pmmb_product.Datos.Product
import com.example.room_pmmb_product.Datos.ProductRepository
import java.text.NumberFormat
import com.example.room_pmmb_product.ui.product.ProductUiState
import com.example.room_pmmb_product.ui.product.ProductDetails
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to validate and insert products in the Room database.
 */
class ProductEntryViewModel(private val productRepository: ProductRepository) : ViewModel() {

    var productUiState by mutableStateOf(ProductUiState())
        private set

    /**
     * Updates the [productUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(productDetails: ProductDetails) {
        val isValid = validateInput(productDetails)
        productUiState = ProductUiState(
            productDetails = productDetails,
            isEntryValid = isValid
        )
    }

    /**
     * Inserts a [Product] in the Room database.
     */
    suspend fun saveProduct() {
        if (validateInput()) {
            productRepository.insertProduct(productUiState.productDetails.toProduct())
        }
    }

    private fun validateInput(uiState: ProductDetails = productUiState.productDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() &&
                    description.isNotBlank() &&
                    category.isNotBlank() &&
                    price.isNotBlank() &&
                    price.toDoubleOrNull() != null && price.toDouble() > 0 &&  // Validación de precio
                    quantity > 0  // Validación de cantidad
        }
    }

    suspend fun updateProduct(navigateToEdit: (Int) -> Unit) {
        if (validateInput(productUiState.productDetails)) {
            val updatedProduct = productUiState.productDetails.toProduct()
            productRepository.updateProduct(updatedProduct)
            navigateToEdit(updatedProduct.id)
        } else {
            // Manejo de error
        }
    }
}

/**
 * Represents UI State for a Product.
 */
data class ProductUiState(
    val productDetails: ProductDetails = ProductDetails(),
    val isEntryValid: Boolean = false
)

data class ProductDetails(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val price: String = "",
    val quantity: Float = 0f // Cambiado a Float
)

/**
 * Extension function to convert [ProductDetails] to [Product]. If the value of [ProductDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly, if the value of
 * [ProductDetails.quantity] is not a valid [Float], then the quantity will be set to 0.0.
 */
fun ProductDetails.toProduct(): Product = Product(
    id = id,
    name = name,
    description = description,
    category = category,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity // Ya es Float, no necesita conversión
)

/**
 * Extension function to format the price of a [Product].
 */
fun Product.formattedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price)
}

/**
 * Extension function to convert [Product] to [ProductUiState].
 */
fun Product.toProductUiState(isEntryValid: Boolean = false): ProductUiState = ProductUiState(
    productDetails = this.toProductDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Product] to [ProductDetails].
 */
fun Product.toProductDetails(): ProductDetails = ProductDetails(
    id = id,
    name = name,
    description = description,
    category = category,
    price = price.toString(),
    quantity = quantity // Ya es Float
)
