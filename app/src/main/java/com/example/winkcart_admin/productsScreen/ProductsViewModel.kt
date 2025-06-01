package com.example.winkcart_admin.productsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.repository.ProductRepo
import com.example.winkcart_admin.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProductsViewModel(private val productRepo: ProductRepo) : ViewModel() {

    private val _products=MutableStateFlow<ResponseStatus<MutableList<Product>>>(ResponseStatus.Loading)
    private val _searchQuery= MutableStateFlow("")
    private val _selectedFilter= MutableStateFlow(SearchFilter.BY_NAME)
    val filteredProducts: StateFlow<ResponseStatus<List<Product>>> = combine(_searchQuery,_products,_selectedFilter)
    { query,unfilteredProducts,selectedFilter->
        when(unfilteredProducts){
            is ResponseStatus.Error -> return@combine ResponseStatus.Error(unfilteredProducts.error)
            ResponseStatus.Loading -> return@combine ResponseStatus.Loading
            is ResponseStatus.Success<MutableList<Product>>-> {
                when(selectedFilter){
                    SearchFilter.BY_NAME -> {
                        ResponseStatus.Success(
                            unfilteredProducts.result.filter {
                                it.title.contains(query, ignoreCase = true) }
                        )
                    }
                    SearchFilter.BY_ID -> {
                        ResponseStatus.Success(
                            unfilteredProducts.result.filter {
                                it.id.toString().startsWith(query) }
                        )
                    }
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResponseStatus.Loading)

    private val _singleProduct=MutableStateFlow<ResponseStatus<Product>>(ResponseStatus.Loading)
    val singleProduct=_singleProduct.asStateFlow()


    private val _viewMessage = MutableSharedFlow<String>()
    val viewMessage = _viewMessage.asSharedFlow()

    init {
        fetchProducts()
    }
    fun fetchProducts(){
        _products.value=ResponseStatus.Loading
        viewModelScope.launch {
            productRepo.getAllProducts()
                .catch {
                    _viewMessage.emit("UnexpectedError:  ${it.message}")
                    _products.value=ResponseStatus.Error(it)
                    Log.i("TAG", "fetchProducts: ${it.message}")
                }
                .collect{
                    _products.value=ResponseStatus.Success(it)
                    Log.i("TAG", "fetchProducts: "+it[0])
                }

        }

    }
    fun getProductById(id: Long) {
        viewModelScope.launch {
            productRepo.getProductById(id)
                .catch {
                    _viewMessage.emit("Error getting product by id: ${it.message}")
                    Log.e("ProductsViewModel", "getProductById: ${it.message}")
                }
                .collect { product ->
                    _singleProduct.value=ResponseStatus.Success(product)
                    _viewMessage.emit("getProductById: ${product.title}")
                    Log.i("ProductsViewModel", "getProductById: ${product.title} got Sucessfully")
                }
        }
    }

    fun updateProduct(id: Long, productUpdated: Product) {
        Log.i("TAG", "updateProduct: begining of update product fun")
        viewModelScope.launch {
            productRepo.updateProduct(productUpdated)
                .catch {
                    _viewMessage.emit("Error updating product: ${it.message}")
                    Log.e("ProductsViewModel", "updateProduct: ${it.message}")
                }
                .collect { product ->
                    _singleProduct.value=ResponseStatus.Success(product)
                    _viewMessage.emit("Product updated: ${product.title}")
                    Log.i("ProductsViewModel", "updateProduct: ${product.id} updated successfully")
                    fetchProducts()
                }
        }
    }
    fun deleteProduct(id: Long) {
        viewModelScope.launch {
            try {
                productRepo.deleteProduct(id)
                _viewMessage.emit("Product deleted successfully")
                Log.i("ProductsViewModel", "deleteProduct: Product $id deleted")
                (_products.value as? ResponseStatus.Success)?.result?.removeIf { it.id == id }
                val updatedProducts = (_products.value as ResponseStatus.Success).result
                _products.value=ResponseStatus.Loading
                _products.value = ResponseStatus.Success(updatedProducts.toMutableList())
            } catch (ex: Exception) {
                _viewMessage.emit("Error deleting product: ${ex.message}")
                Log.e("ProductsViewModel", "deleteProduct: ${ex.message}")
            }
        }
    }

    fun addImageToProduct(productId: Long, imageUrl: String) {
        viewModelScope.launch {
            productRepo.addImageToProduct(productId, imageUrl)
                .catch { throwable ->
                    if (throwable is HttpException) {
                        try {
                            val errorResponse = throwable.response()?.errorBody()?.string()
                            Log.e("ProductsViewModel", "Error adding image: $errorResponse")
                        } catch (e: Exception) {
                            Log.e("ProductsViewModel", "Error parsing error response: ${e.message}")
                        }
                    }
                    _viewMessage.emit("Error adding image: ${throwable.message}")
                }
                .collect { imageData ->
                    _viewMessage.emit("Image added to product: ${imageData.src}")
                    Log.i("ProductsViewModel", "addImageToProduct: Image added with URL: ${imageData.src}")
                }
        }
    }

    fun deleteImageFromProduct(productId: Long, imageId: Long) {
        viewModelScope.launch {
            try {
                productRepo.deleteImageFromProduct(productId, imageId)
                _viewMessage.emit("Image deleted from product")
                Log.i("ProductsViewModel", "deleteImageFromProduct: Image $imageId deleted")
            } catch (ex: Exception) {
                _viewMessage.emit("Error deleting image: ${ex.message}")
                Log.e("ProductsViewModel", "deleteImageFromProduct: ${ex.message}")
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _searchQuery.value=newQuery
    }

    fun onSelectedFilterChanged(selectedFilter: SearchFilter) {
        _selectedFilter.value=selectedFilter
        _searchQuery.value=""
    }

}
class ProductsViewModelFactory(private val repository: ProductRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductsViewModel(productRepo = repository) as T
    }
}
enum class SearchFilter {
    BY_NAME, BY_ID
}
