package com.example.winkcart_admin.productsScreen.components

import androidx.compose.runtime.Composable
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.model.Product

@Composable
fun ProductViewer(productsResult: ResponseStatus<List<Product>>) {

    when(productsResult){
        is ResponseStatus.Error -> ProductsFailureState(
            productsResult.error.message ?: "UnknownError"
        )
        ResponseStatus.Loading -> ProductsLoading()
        is ResponseStatus.Success<*> -> ProductsGrid((productsResult as ResponseStatus.Success).result)
    }
}