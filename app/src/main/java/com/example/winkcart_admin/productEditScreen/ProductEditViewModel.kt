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

            Log.i("TAG", "createProductt: ${createdProduct}")
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
    fun editProduct(updatedProduct: Product, editedImageList: List<String>) {
        _productState.value = ResponseStatus.Loading

        val originalImagesSet = updatedProduct.images?.map { it.src }?.toSet() ?: emptySet()
        val editedImagesSet = editedImageList.toSet()

        val addedImages = editedImagesSet - originalImagesSet
        val removedImages = originalImagesSet - editedImagesSet

        val srcToIdMap = updatedProduct.images?.associate { it.src to it.id } ?: emptyMap()
        Log.i("TAG", "editProduct: $addedImages")
        Log.i("ProductEditViewModel", "product Before update: ${updatedProduct}")
        viewModelScope.launch {
            try {
                productRepo.updateProduct(updatedProduct).
                    catch {
                        _viewMessage.emit("Error updating product: ${it.message}")
                        Log.e("ProductEditViewModel", "Edit Product: ${it.message}")
                    }
                    .collect{
                    }
                addedImages.forEach {
                    productRepo.addImageToProduct(updatedProduct.id, it).catch {
                        _viewMessage.emit("Error updating Image: ${it.message}")
                        Log.e("ProductEditViewModel", "Edit Product: ${it.message}")
                    }.collect{

                    }
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
                        Log.i("ProductEditViewModel", "Final update complete: ${finalProduct}")
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
