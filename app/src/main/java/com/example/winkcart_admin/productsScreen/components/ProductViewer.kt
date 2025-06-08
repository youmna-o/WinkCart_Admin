package com.example.winkcart_admin.productsScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.example.winkcart_admin.Screens
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.productsScreen.SearchFilter

@Composable
fun ProductViewer(
    productsResult: ResponseStatus<List<Product>>,
    onSearchQueryChanged: (String) -> Unit,
    onSearchFilterChanged: (SearchFilter) -> Unit,
    onProductDeleteAction: (Long)->Unit,
    onProductClickAction:(Product,Screens)->Unit
) {

    when(productsResult){
        is ResponseStatus.Error -> ProductsFailureState(
            productsResult.error.message ?: "UnknownError"
        )
        ResponseStatus.Loading -> ProductsLoading()
        is ResponseStatus.Success<*> -> {
            Column {
                ProductsSearchBar(onSearchQueryChanged,onSearchFilterChanged)
                ProductsGrid(
                    products = (productsResult as ResponseStatus.Success).result,
                    onProductDeleteAction = onProductDeleteAction,
                    onProductClickAction=onProductClickAction
                )
            }

        }
    }
}