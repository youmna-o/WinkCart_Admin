package com.example.winkcart_admin.data.repository

import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.remote.RemoteDataSource
import com.example.winkcart_admin.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ProductRepoImpl (private val remoteDataSource: RemoteDataSource):ProductRepo{
    override suspend fun getAllProducts(): Flow<List<Product>> = flow {
        try {
            val products= remoteDataSource.getAllProducts()
            emit(products)
        }catch (ex:Exception){
            throw ex
        }
    }.flowOn(Dispatchers.IO)
}