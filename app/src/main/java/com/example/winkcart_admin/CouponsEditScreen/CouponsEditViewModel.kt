package com.example.winkcart_admin.CouponsEditScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.repository.ProductRepo
import com.example.winkcart_admin.model.CouponFormState
import com.example.winkcart_admin.model.DiscountCode
import com.example.winkcart_admin.model.DiscountCodeRequest
import com.example.winkcart_admin.model.PrerequisiteSubtotalRange
import com.example.winkcart_admin.model.PriceRule
import com.example.winkcart_admin.model.PriceRuleRequest
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.model.toFormState
import com.example.winkcart_admin.model.toPriceRule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CouponsEditViewModel(private val productRepo: ProductRepo) : ViewModel() {


    private val _formState = MutableStateFlow(CouponFormState())
    val formState: StateFlow<CouponFormState> = _formState.asStateFlow()

    private val _couponResponse = MutableStateFlow<ResponseStatus<PriceRule>>(ResponseStatus.Loading)
    val couponResponse: StateFlow<ResponseStatus<PriceRule>> = _couponResponse.asStateFlow()
    private lateinit var productList:List<Product>

    init {
        viewModelScope.launch {
            productRepo.getAllProducts()
            .collect{
                productList=it
            }
        }

    }

    fun loadCoupon(couponId: Long) {
        if (couponId == 0L) {
            _couponResponse.value = ResponseStatus.Success(createDefaultPriceRule())
        } else {
            viewModelScope.launch {
                try {
                    val rule :PriceRule= productRepo.getCouponDataById(couponId)
                    _couponResponse.value = ResponseStatus.Success(rule)
                    _formState.value = rule.toFormState()
                } catch (e: Exception) {
                    _couponResponse.value = ResponseStatus.Error(e)
                }
            }
        }
    }

    private fun createDefaultPriceRule(): PriceRule = PriceRule(
        id = 0,
        title = "",
        targetType = "line_item",
        targetSelection = "all",
        allocationMethod = "across",
        valueType = "percentage",
        value = "",
        customerSelection = "all",
        startsAt = "",
        endsAt = null,
        usageLimit = null,
        prerequisiteSubtotalRange = null,
        discountCodeMap = mutableMapOf()
    )

    fun onFieldChange(newCouponState: CouponFormState) {
        _formState.value=newCouponState
    }


    fun uploadCouponUpdate() {
        var returnedRule:PriceRule
        viewModelScope.launch {
            try {
                _couponResponse.value=ResponseStatus.Loading
                val rule=_formState.value.toPriceRule()
                val priceRuleRequest=PriceRuleRequest(rule)
                val newCodes=_formState.value.newDiscountCodes.map { DiscountCodeRequest(discountCode = DiscountCode(code = it)) }
                Log.i("TAG", "uploadCouponUpdate:before creating/updating ${PriceRuleRequest(rule)}")
                if (_formState.value.id==0L){
                    returnedRule=productRepo.createCoupon(
                        request = priceRuleRequest,
                        discountCodeRequests = newCodes
                    )
                }
                else{
                    returnedRule=productRepo.updatePriceRule(
                        request = priceRuleRequest,
                        discountCodeRequests = newCodes
                    )
                }
                _couponResponse.value=ResponseStatus.Success(returnedRule)
                _formState.value=returnedRule.toFormState()
                Log.i("TAG", "uploadCouponUpdate: Successful Coupon create/update")

            }catch (ex:Exception){
                _couponResponse.value=ResponseStatus.Error(ex)
                if (ex is HttpException) {
                    try {
                        val errorResponse = ex.response()?.errorBody()?.string()
                        Log.e("InventoryViewModel", "Error updating Coupon: $errorResponse")
                    } catch (e: Exception) {
                        Log.e("InventoryViewModel", "Error parsing error response: ${e.message}")
                    }
                }
                Log.i("TAG", "uploadCouponUpdate: ${ex}")
            }

        }

    }

    fun isProductIdValid(productId: Long): Boolean {
        Log.i("TAG", "isProductIdValid: ${productList.firstOrNull{ it.id==productId}!=null}")
        return productList.firstOrNull{ it.id==productId}!=null
    }

    fun deleteDiscountCode(codeId: Long) {
        viewModelScope.launch {
            productRepo.deleteDiscountCode(formState.value.id,codeId)
        }

    }
}


class CouponsEditViewModelFactory(private val repository: ProductRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CouponsEditViewModel(productRepo = repository) as T
    }
}



