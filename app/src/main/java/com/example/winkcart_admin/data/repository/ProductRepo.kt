package com.example.winkcart_admin.data.repository

import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepo {

    suspend fun getAllProducts(): Flow<List<Product>>
}