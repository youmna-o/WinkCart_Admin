package com.example.winkcart_admin.data.remote

import com.example.winkcart_admin.model.ImageData
import com.example.winkcart_admin.model.ImageRequest
import com.example.winkcart_admin.model.ImageResponse
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
}