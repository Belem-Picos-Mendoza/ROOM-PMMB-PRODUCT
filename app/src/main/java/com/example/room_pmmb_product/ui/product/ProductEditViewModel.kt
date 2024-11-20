package com.example.room_pmmb_product.ui.product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.room_pmmb_product.Datos.Product
import com.example.room_pmmb_product.Datos.ProductRepository
import com.example.room_pmmb_product.ui.product.ProductEditDestination
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProductEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val productId: Int = savedStateHandle[ProductEditDestination.productIdArg] ?: 0
) : ViewModel() {

    // Estado del UI con el producto
    var productUiState by mutableStateOf(ProductUiState())
        private set
    init {
        // Cargamos el producto si el ID es válido
        if (productId == 0) {
            // Maneja el caso cuando el ID es incorrecto o no se pasa
            // Podrías redirigir o mostrar un mensaje de error
            // navigateBack() o cualquier otro manejo de error
        } else {
            loadProduct()
        }
    }


    fun loadProduct() {
        viewModelScope.launch {
            productUiState = productRepository.getProductStream(productId)
                .filterNotNull()
                .first()
                .toProductUiState(true) // Convierte el objeto al estado adecuado para la UI
        }
    }

//    suspend fun saveProduct() {
//        if (validateInput(productUiState.productDetails)) {
//            // Guardamos el producto editado
//            productRepository.updateProduct(productUiState.productDetails.toProduct())
//        } else {
//            // Si la validación falla, se puede lanzar una excepción
//            throw IllegalArgumentException("Los datos del producto no son válidos.")
//        }
//    }
suspend fun saveProduct(newId: Int? = null) {
    val currentProduct = productUiState.productDetails.toProduct()
    val updatedProduct = if (newId != null) {
        currentProduct.copy(id = newId) // Actualiza el ID si se pasa un nuevo valor
    } else {
        currentProduct // Mantén el ID original si no se pasa uno
    }
    productRepository.updateProduct(updatedProduct)  // Actualiza el producto en la base de datos
}


    fun updateUiState(productDetails: ProductDetails) {
        // Actualizamos el estado UI con los nuevos valores
        productUiState = ProductUiState(
            productDetails = productDetails,
            isEntryValid = validateInput(productDetails)
        )
    }

    private fun validateInput(uiState: ProductDetails = productUiState.productDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && description.isNotBlank() && category.isNotBlank() &&
                    price.isNotBlank() && quantity > 0
        }
    }


}
