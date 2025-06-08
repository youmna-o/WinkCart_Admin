package com.example.winkcart_admin.productsScreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.winkcart_admin.BottomNavigationBar
import com.example.winkcart_admin.Screens
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.productsScreen.components.ProductViewer
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun ProductsScreen(navHostController: NavHostController, viewModel: ProductsViewModel) {

    val productsResult=viewModel.filteredProducts.collectAsState()
    val navBackStackEntry = navHostController.currentBackStackEntryAsState()

    val shouldRefresh = navBackStackEntry.value?.savedStateHandle?.getLiveData<Boolean>("shouldRefresh")?.observeAsState()

    LaunchedEffect(shouldRefresh?.value) {
        if (shouldRefresh?.value == true) {
            viewModel.fetchProducts()
            navBackStackEntry.value?.savedStateHandle?.set("shouldRefresh", false)
        }
    }
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
                onProductClickAction = { product,screenToNavigate ->
                    navHostController.currentBackStackEntry?.savedStateHandle?.set("product",product)
                    navHostController.navigate(screenToNavigate)
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


