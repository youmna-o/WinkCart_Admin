package com.example.winkcart_admin.data.remote

import com.example.winkcart_admin.model.Product
interface RemoteDataSource {
    suspend fun getAllProducts():List<Product>
}