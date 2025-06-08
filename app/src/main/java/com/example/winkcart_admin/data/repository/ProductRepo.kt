package com.example.winkcart_admin.data.repository

import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.model.ImageData
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
}