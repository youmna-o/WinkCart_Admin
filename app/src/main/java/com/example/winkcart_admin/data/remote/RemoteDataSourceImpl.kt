package com.example.winkcart_admin.data.remote

import com.example.winkcart_admin.data.remote.retrofit.AdminServices
import com.example.winkcart_admin.model.Product

class RemoteDataSourceImpl(private val adminServices: AdminServices) :RemoteDataSource{
    override suspend fun getAllProducts(): List<Product> {
        return adminServices.getProducts().products
    }
}