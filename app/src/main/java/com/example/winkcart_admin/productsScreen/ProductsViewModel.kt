package com.example.winkcart_admin.productsScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
                    Log.i("Fetch", "fetchProducts: ${it.message}")
                }
                .collect{
                    _products.value=ResponseStatus.Success(it)
                    Log.i("Fetch", "fetchProducts: "+it[0])
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
