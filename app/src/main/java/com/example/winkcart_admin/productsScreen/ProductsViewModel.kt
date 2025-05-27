package com.example.winkcart_admin.productsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
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

class ProductsViewModel(private val productRepo: ProductRepo) : ViewModel() {

    private val _products=MutableStateFlow<ResponseStatus<List<Product>>>(ResponseStatus.Loading)
    val products=_products.asStateFlow()
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
                    Log.i("TAG", "fetchProducts: "+it[0].title)
                }

        }

    }
}
