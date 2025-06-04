package com.example.winkcart_admin.data.repository

import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.remote.RemoteDataSource
import com.example.winkcart_admin.model.ImageData
import com.example.winkcart_admin.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductRepoImpl (private val remoteDataSource: RemoteDataSource):ProductRepo{
    override suspend fun getAllProducts(): Flow<MutableList<Product>> = flow {
        try {
            val products= remoteDataSource.getAllProducts()
            emit(products)
        }catch (ex:Exception){
            throw ex
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getProductById(id: Long): Flow<Product> = flow{
        try {
            val product= remoteDataSource.getProductByID(id)
            emit(product)
        }catch (ex:Exception){
            throw ex
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun createProduct(createdProduct: Product): Flow<Product> =flow{
        try {
            val product= remoteDataSource.createProduct(createdProduct)
            emit(product)
        }catch (ex:Exception){
            throw ex
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun updateProduct(productUpdated: Product): Flow<Product> = flow{
        try {
            if(productUpdated.title.isNotBlank()){
                val product= remoteDataSource.updateProduct(productUpdated.id,productUpdated)
                emit(product)
            }
            else{
                throw IllegalArgumentException("Product Title Cant be blank")
            }
        }catch (ex:Exception){
            throw ex
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteProduct(id: Long) {
        try {
            remoteDataSource.deleteProduct(id)
        }catch (ex:Exception){
            throw ex
        }
    }

    override suspend fun addImageToProduct(productId: Long, imageURl: String): Flow<ImageData> =flow{
        try {
            val imageData= remoteDataSource.addImageToProduct(productId,imageURl)
            emit(imageData)
        }catch (ex:Exception){
            throw ex
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteImageFromProduct(productId: Long, imageId: Long) {
        try {
            remoteDataSource.deleteImageFromProduct(productId, imageId)
        }catch (ex:Exception){
            throw ex
        }
    }

    override suspend fun deleteVariant(productId: Long, variantId: Long) {
        remoteDataSource.deleteProductVariant(productId,variantId)
    }

}