package com.example.winkcart_admin.CouponsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.repository.ProductRepo
import com.example.winkcart_admin.model.CouponsModel
import com.example.winkcart_admin.model.PriceRule
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CouponsViewModel(private val productRepo: ProductRepo) : ViewModel() {

    private val _couponsState = MutableStateFlow<ResponseStatus<MutableList<CouponsModel>>>(ResponseStatus.Loading)
    val couponsState = _couponsState.asStateFlow()

    private val _viewMessage = MutableSharedFlow<String>()
    val viewMessage = _viewMessage.asSharedFlow()


    fun fetchCoupons() {
        viewModelScope.launch {
            try {
                _couponsState.value=ResponseStatus.Loading
                val coupons = productRepo.getAllCoupons()
                _couponsState.value = ResponseStatus.Success(coupons)
            } catch (e: Exception) {
                if (e is HttpException) {
                    try {
                        val errorResponse = e.response()?.errorBody()?.string()
                        Log.e("CouponsViewModel", "Error Fetching Coupons: $errorResponse")
                    } catch (e: Exception) {
                        Log.e("CouponsViewModel", "Error parsing error response: ${e.message}")
                    }
                }
                _couponsState.value = ResponseStatus.Error(e)
                Log.e("TAG", "fetchCoupons:${e.message} " )
            }
        }
    }

    fun deleteCoupon(coupon: CouponsModel) {
        viewModelScope.launch {
            val couponsList=(_couponsState.value as ResponseStatus.Success).result
            _couponsState.value= ResponseStatus.Loading
            try {
                productRepo.deleteCoupon(coupon.id)
                couponsList.remove(coupon)
                _couponsState.value= ResponseStatus.Success(couponsList)
                _viewMessage.emit("Deleted Successfully")
            } catch (e: Exception) {
                _viewMessage.emit("Error Deleting Coupon")
                _couponsState.value= ResponseStatus.Success(couponsList)
            }
        }
    }
}
class CouponsViewModelFactory(private val repository: ProductRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CouponsViewModel(productRepo = repository) as T
    }
}