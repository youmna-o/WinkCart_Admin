package com.example.winkcart_admin.data.remote

import com.example.winkcart_admin.model.DiscountCodeRequest
import com.example.winkcart_admin.model.DiscountCodeResponse
import com.example.winkcart_admin.model.ImageData
import com.example.winkcart_admin.model.ImageRequest
import com.example.winkcart_admin.model.ImageResponse
import com.example.winkcart_admin.model.InventoryLevelSetRequest
import com.example.winkcart_admin.model.PriceRule
import com.example.winkcart_admin.model.PriceRuleRequest
import com.example.winkcart_admin.model.PriceRulesResponse
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.model.SingleProductResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RemoteDataSource {
    suspend fun getAllProducts():MutableList<Product>
    suspend fun getProductByID(id: Long):Product
    suspend fun createProduct(product:Product):Product

    suspend fun updateProduct(id: Long, product: Product): Product
    suspend fun deleteProduct(id: Long)

    suspend fun addImageToProduct(productId: Long, imageURl: String): ImageData

    suspend fun deleteImageFromProduct(productId: Long, imageId: Long)
    suspend fun deleteProductVariant(productId: Long, variantId: Long)
    suspend fun setInventoryLevel(request: InventoryLevelSetRequest)
    suspend fun getAllPriceRules(): PriceRulesResponse
    suspend fun getPriceRuleById(ruleId:Long):PriceRuleRequest
    suspend fun getDiscountCodesForPriceRule(priceRuleId: Long): DiscountCodeResponse
    suspend fun createPriceRule(request: PriceRuleRequest): PriceRuleRequest
    suspend fun createDiscountCode(priceRuleId: Long, request: DiscountCodeRequest): DiscountCodeResponse
    suspend fun deleteDiscountCode(priceRuleId: Long, discountCodeId: Long)
    suspend fun deletePriceRule(priceRuleId: Long)
    suspend fun updatePriceRule(request: PriceRuleRequest): PriceRuleRequest
}