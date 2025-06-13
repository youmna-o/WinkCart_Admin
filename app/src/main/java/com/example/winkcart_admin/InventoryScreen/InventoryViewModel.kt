package com.example.winkcart_admin.InventoryScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.repository.ProductRepo
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.productEditScreen.ProductEditViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
class InventoryViewModel (private val productRepo: ProductRepo, product: Product) : ViewModel() {

    private val _productState= MutableStateFlow<ResponseStatus<Product>>(ResponseStatus.Success(product))
    val productState=_productState.asStateFlow()

    private val _viewMessage = MutableSharedFlow<String>()
    val viewMessage = _viewMessage.asSharedFlow()

    fun updateVariantQuantity(variantId: Long, newQuantity: Int) {
        viewModelScope.launch {
            try {
                productRepo.setInventoryLevel(newQuantity,variantId)
                _viewMessage.emit("Variant updated successfully")

            } catch (ex: Exception) {
                _viewMessage.emit("error updating Variant")
                if (ex is HttpException) {
                    try {
                        val errorResponse = ex.response()?.errorBody()?.string()
                        Log.e("InventoryViewModel", "Error updating inventory: $errorResponse")
                    } catch (e: Exception) {
                        Log.e("InventoryViewModel", "Error parsing error response: ${e.message}")
                    }
                }
                Log.i("TAG", "updateVariantQuantity: ${ex.message}")
            }
        }


    }

}

class InventoryViewModelFactory(private val repository: ProductRepo,private val product: Product) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InventoryViewModel(productRepo = repository,product =product ) as T
    }
}