package com.example.winkcart_admin.productsScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.winkcart_admin.BottomNavigationBar
import com.example.winkcart_admin.Screens
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.productsScreen.components.ProductViewer

@Composable
fun ProductsScreen(navHostController: NavHostController, viewModel: ProductsViewModel) {

    val productsResult=viewModel.filteredProducts.collectAsState()
    Scaffold(
        bottomBar = { BottomNavigationBar(navHostController) }
    ) {innerPadding->

        Box(modifier = Modifier.padding(innerPadding)){
            ProductViewer(
                productsResult = productsResult.value,
                onSearchQueryChanged = { newQuery -> viewModel.onQueryChanged(newQuery) },
                onSearchFilterChanged = { selectedFilter ->
                    viewModel.onSelectedFilterChanged(selectedFilter)
                },
                onProductDeleteAction = { id ->
                    viewModel.deleteProduct(id)
                },
                onProductClickAction = { product ->
                    navHostController.currentBackStackEntry?.savedStateHandle?.set("product",product)
                    navHostController.navigate(Screens.ProductEditSrc)
                }
            )
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                onClick = {
                    navHostController.currentBackStackEntry?.savedStateHandle?.set("product",Product())
                    navHostController.navigate(Screens.ProductEditSrc)}
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new Product"
                )
            }
        }

    }


}


