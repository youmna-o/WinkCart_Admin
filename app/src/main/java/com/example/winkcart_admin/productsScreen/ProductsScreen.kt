package com.example.winkcart_admin.productsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.winkcart_admin.BottomNavigationBar
import com.example.winkcart_admin.productsScreen.components.ProductViewer

@Composable
fun ProductsScreen(navHostController: NavHostController, viewModel: ProductsViewModel) {

    val productsResult=viewModel.filteredProducts.collectAsState()
    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) }
    ) {innerPadding->

        Column (modifier = Modifier.padding(innerPadding)){
            ProductViewer(
                productsResult = productsResult.value,
                onSearchQueryChanged = { newQuery -> viewModel.onQueryChanged(newQuery) },
                onSearchFilterChanged = { selectedFilter ->
                    viewModel.onSelectedFilterChanged(selectedFilter)
                },
                onProductDeleteAction = { id->
                    viewModel.deleteProduct(id)
                }
            )

        }

    }


}


