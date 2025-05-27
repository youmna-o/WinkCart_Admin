package com.example.winkcart_admin.data.remote.retrofit

import com.example.winkcart_admin.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "https://mad45-sv-and2.myshopify.com/admin/api/2025-04/"
    private const val ACCESS_TOKEN =BuildConfig.SHOPIFY_ADMIN_TOKEN

    private val okHttpClient= OkHttpClient.Builder()
        .addInterceptor(ShopifyAuthInterceptor(ACCESS_TOKEN))
        .build()

    private val retrofitInstance = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val productService: AdminServices = retrofitInstance.create(AdminServices::class.java)
}
