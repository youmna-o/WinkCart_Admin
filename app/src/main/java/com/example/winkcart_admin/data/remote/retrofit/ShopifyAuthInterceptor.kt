package com.example.winkcart_admin.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class ShopifyAuthInterceptor (private val accessToken:String): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request=chain.request()
            .newBuilder()
            .addHeader("X-Shopify-Access-Token",accessToken)
            .build()
        return chain.proceed(request)
    }

}