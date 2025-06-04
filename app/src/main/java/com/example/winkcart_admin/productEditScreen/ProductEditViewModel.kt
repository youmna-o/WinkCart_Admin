package com.example.winkcart_admin.productEditScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.repository.ProductRepo
import com.example.winkcart_admin.model.Product
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProductEditViewModel(private val productRepo: ProductRepo, product: Product) : ViewModel() {


    private val _productState= MutableStateFlow<ResponseStatus<Product>>(ResponseStatus.Success(product))
    val productState=_productState.asStateFlow()

    private val _viewMessage = MutableSharedFlow<String>()
    val viewMessage = _viewMessage.asSharedFlow()

    fun createProduct(createdProduct: Product) {
        _productState.value=ResponseStatus.Loading
        viewModelScope.launch {

            Log.i("TAG", "createProductbeofore: ${createdProduct}")
            productRepo.createProduct(createdProduct)
                .catch {
                    _viewMessage.emit("Error creating product: ${it.message}")
                    if (it is HttpException) {
                        try {
                            val errorResponse = it.response()?.errorBody()?.string()
                            Log.e("ProductEditViewModel", "Error Creating Product: $errorResponse")
                        } catch (e: Exception) {
                            Log.e("ProductEditViewModel", "Error parsing error response: ${e.message}")
                        }
                    }
                }
                .collect { product ->
                    _productState.value= ResponseStatus.Success(product)
                    _viewMessage.emit("Product created: ${product.title}")
                    Log.i("ProductsViewModel", "createProduct: ${product} created successfully")
                }
        }
    }
    fun editProduct(updatedProduct: Product) {
        val currentState = _productState.value
        if (currentState !is ResponseStatus.Success) {
            viewModelScope.launch {
                _viewMessage.emit("Cannot update product: product not loaded.")
            }
            return
        }
        val originalProduct = currentState.result
        Log.i("TAG", "editProduct:original product:${originalProduct} ")
        Log.i("TAG", "editProduct:updated product before sending:${updatedProduct} ")
        val originalImages = originalProduct.images ?: emptyList()
        val originalImageSrcSet = originalImages.map { it.src }.toSet()
        val finalImageSrcSet = updatedProduct.images?.map { it.src }?.toSet() ?: setOf()
        val removedImages = originalImageSrcSet - finalImageSrcSet
        val srcToIdMap = originalImages.associateBy({ it.src }, { it.id })
        val oldVariantIds = originalProduct.variants.map { it.id }
        viewModelScope.launch {
            _productState.value = ResponseStatus.Loading
            try {
                oldVariantIds.forEach { variantId ->
                    try {
                        productRepo.deleteVariant(updatedProduct.id, variantId?:0)
                    } catch (e: Exception) {
                        if (e is HttpException) {
                            try {
                                val errorResponse = e.response()?.errorBody()?.string()
                                Log.e("TAG", "Error deleting variant: $errorResponse")
                            } catch (e: Exception) {
                                Log.e("TAG", "Error parsing error response: ${e.message}")
                            }
                        }
                        Log.e("ProductEditViewModel", "Failed to delete variant $variantId: ${e.message}")
                    }
                }
                updatedProduct.variants.forEach { it.id=null }
                productRepo.updateProduct(updatedProduct)
                    .catch { throwable ->
                        if (throwable is HttpException) {
                            try {
                                val errorResponse = throwable.response()?.errorBody()?.string()
                                Log.e("TAG", "Error updating product: $errorResponse")
                            } catch (e: Exception) {
                                Log.e("TAG", "Error parsing error response: ${e.message}")
                            }
                        }
                        _viewMessage.emit("Error updating product: ${throwable.message}")
                    }
                    .collect{
                    }

                removedImages.forEach { deletedImageSrc ->
                    val imageId = srcToIdMap[deletedImageSrc]
                    if (imageId != null) {
                        productRepo.deleteImageFromProduct(updatedProduct.id, imageId)
                    }
                }

                productRepo.getProductById(updatedProduct.id)
                    .collect { finalProduct ->
                        _productState.value = ResponseStatus.Success(finalProduct)
                        _viewMessage.emit("Product updated: ${finalProduct.title}")
                        Log.i("ProductEditViewModel", "fetch by id: ${finalProduct}")
                    }

            } catch (e: Exception) {
                _viewMessage.emit("Error updating product: ${e.message}")
                Log.e("ProductEditViewModel", "editProduct failed: ${e.message}")
            }
        }
    }



}
class ProductEditViewModelFactory(private val repository: ProductRepo,private val product: Product) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductEditViewModel(productRepo = repository,product =product ) as T
    }
}
