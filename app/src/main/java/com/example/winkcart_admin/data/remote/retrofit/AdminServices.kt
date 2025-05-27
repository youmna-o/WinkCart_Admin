package com.example.winkcart_admin.data.remote.retrofit

import com.example.winkcart_admin.model.ImageRequest
import com.example.winkcart_admin.model.ImageResponse
import com.example.winkcart_admin.model.ProductListResponse
import com.example.winkcart_admin.model.SingleProductResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AdminServices {
    @GET("products.json")
    suspend fun getProducts():ProductListResponse
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





}