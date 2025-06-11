package com.example.winkcart_admin.data.repository

import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.model.CouponsModel
import com.example.winkcart_admin.model.DiscountCodeRequest
import com.example.winkcart_admin.model.ImageData
import com.example.winkcart_admin.model.PriceRule
import com.example.winkcart_admin.model.PriceRuleRequest
import com.example.winkcart_admin.model.PriceRulesResponse
import com.example.winkcart_admin.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepo {

    suspend fun getAllProducts(): Flow<MutableList<Product>>
    suspend fun getProductById(id:Long):Flow<Product>
    suspend fun createProduct(createdProduct: Product):Flow<Product>


    suspend fun updateProduct(productUpdated: Product): Flow<Product>
    suspend fun deleteProduct(id: Long)

    suspend fun addImageToProduct(productId: Long, imageURl: String): Flow<ImageData>

    suspend fun deleteImageFromProduct(productId: Long, imageId: Long)
    suspend fun deleteVariant(productId: Long, variantId: Long)

    suspend fun setInventoryLevel(value:Int,inventoryItemId:Long,locationID:Long=83257360632)
    suspend fun  getAllCoupons(): MutableList<CouponsModel>

    suspend fun getCouponDataById(id:Long):PriceRule
    suspend fun createCoupon(request: PriceRuleRequest, discountCodeRequests: List<DiscountCodeRequest>): PriceRule
    suspend fun deleteCoupon(priceRuleId: Long)
    suspend fun updatePriceRule(request: PriceRuleRequest,discountCodeRequests: List<DiscountCodeRequest>): PriceRule
    suspend fun deleteDiscountCode(productId: Long, codeId: Long)
}