package com.example.winkcart_admin.data.remote.retrofit

import com.example.winkcart_admin.model.ProductListResponse
import retrofit2.http.GET

interface AdminServices {
    @GET("products.json")
    suspend fun getProducts():ProductListResponse


}