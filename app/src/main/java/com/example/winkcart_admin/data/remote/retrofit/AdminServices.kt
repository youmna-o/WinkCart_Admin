package com.example.winkcart_admin.data.remote.retrofit

import com.example.winkcart_admin.model.DiscountCodeRequest
import com.example.winkcart_admin.model.DiscountCodeResponse
import com.example.winkcart_admin.model.ImageRequest
import com.example.winkcart_admin.model.ImageResponse
import com.example.winkcart_admin.model.InventoryLevelSetRequest
import com.example.winkcart_admin.model.PriceRule
import com.example.winkcart_admin.model.PriceRuleRequest
import com.example.winkcart_admin.model.PriceRulesResponse
import com.example.winkcart_admin.model.ProductListResponse
import com.example.winkcart_admin.model.SingleProductResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AdminServices {
    @GET("products.json")
    suspend fun getProducts( @Query("limit") limit: Int = 250):ProductListResponse
    @GET("products/{id}.json")
    suspend fun getProductById(@Path("id") id:Long):SingleProductResponse
    @POST("products.json")
    suspend fun createProduct(@Body product: SingleProductResponse): SingleProductResponse

    @PUT("products/{id}.json")
    suspend fun updateProduct(@Path("id") id: Long, @Body product: SingleProductResponse): SingleProductResponse

    @DELETE("products/{id}.json")
    suspend fun deleteProduct(@Path("id") id: Long)

    @POST("products/{product_id}/images.json")
    suspend fun addImageToProduct(@Path("product_id") productId: Long, @Body imageRequest: ImageRequest): ImageResponse

    @DELETE("products/{product_id}/images/{image_id}.json")
    suspend fun deleteImageFromProduct(@Path("product_id") productId: Long, @Path("image_id") imageId: Long)

    @DELETE("products/{product_id}/variants/{variant_id}.json")
    suspend fun deleteProductVariant(@Path("product_id") productId: Long, @Path("variant_id")variantId: Long)

    @POST("inventory_levels/set.json")
    suspend fun setInventoryLevel(@Body request: InventoryLevelSetRequest)

    @GET("price_rules.json")
    suspend fun getAllPriceRules(): PriceRulesResponse

    @GET("price_rules/{price_rule_id}.json")
    suspend fun getPriceRuleById(@Path("price_rule_id") priceRuleId: Long): PriceRuleRequest

    @GET("price_rules/{price_rule_id}/discount_codes.json")
    suspend fun getDiscountCodesForPriceRule(@Path("price_rule_id") priceRuleId: Long) :DiscountCodeResponse

    @POST("price_rules.json")
    suspend fun createPriceRule(@Body request: PriceRuleRequest):PriceRuleRequest

    @POST("price_rules/{price_rule_id}/discount_codes.json")
    suspend fun createDiscountCode( @Path("price_rule_id") priceRuleId: Long,@Body request: DiscountCodeRequest):DiscountCodeResponse

    @DELETE("price_rules/{price_rule_id}/discount_codes/{discount_code_id}.json")
    suspend fun deleteDiscountCode(@Path("price_rule_id") priceRuleId: Long, @Path("discount_code_id") discountCodeId: Long)

    @DELETE("price_rules/{price_rule_id}.json")
    suspend fun deletePriceRule(@Path("price_rule_id") priceRuleId: Long)

    @PUT("price_rules/{price_rule_id}.json")
    suspend fun updatePriceRule(@Path("price_rule_id") priceRuleId: Long, @Body request: PriceRuleRequest):PriceRuleRequest



}