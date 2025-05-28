package com.example.winkcart_admin

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

import androidx.navigation.compose.rememberNavController
import com.example.winkcart_admin.data.remote.RemoteDataSourceImpl
import com.example.winkcart_admin.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_admin.data.repository.ProductRepoImpl
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.productsScreen.ProductsScreen
import com.example.winkcart_admin.productsScreen.ProductsViewModelFactory

@Composable
fun MainApp() {
    val navHostController= rememberNavController()
    NavHost(navController = navHostController, startDestination = Screens.ProductsScr) {
        composable<Screens.ProductsScr> {
            ProductsScreen(
                navHostController = navHostController,
                viewModel = viewModel(
                    factory = ProductsViewModelFactory(
                        repository = ProductRepoImpl(
                            remoteDataSource = RemoteDataSourceImpl(
                                adminServices = RetrofitHelper.productService
                            )
                        )
                    )
                )
            )
        }
        composable<Screens.DashboardScr> { 
            DashboardScreen(navHostController)
        }
        composable<Screens.CouponsScr> {
            CouponsScreen(navHostController)
        }
        composable<Screens.InventoryScr> {
            InventoryScreen(navHostController)
        }
        /*composable<Screens.ProductDetailsSrc> {
            val product=it.toRoute<Screens.ProductDetailsSrc>().product
            ProductDetailsScreen(navHostController,product)
        }*/

    }


}

@Composable
fun ProductDetailsScreen(navHostController: NavHostController, product: Product) {

    Scaffold{padding->

        Text(text = "Product Details", modifier = Modifier.padding(padding))
    }
}

@Composable
fun InventoryScreen(navHostController: NavHostController) {

    Scaffold(
        bottomBar = {BottomNavigationBar(navHostController)}
    ) {padding->

        Text(text = "Inventory Screen", modifier = Modifier.padding(padding))
    }
}

@Composable
fun CouponsScreen(navHostController: NavHostController) {

    Scaffold(
        bottomBar = {BottomNavigationBar(navHostController)}
    ) {padding->

        Text(text = "Coupons Screen", modifier = Modifier.padding(padding))
    }
}

@Composable
fun DashboardScreen(navHostController: NavHostController) {

    Scaffold(
        bottomBar = {BottomNavigationBar(navHostController)}
    ) {padding->

        Text(text = "DashBoard Screen", modifier = Modifier.padding(padding))
    }
    
}

@Composable
fun BottomNavigationBar(navHostController: NavHostController) {

    val bottomItems= listOf(
        Screens.DashboardScr,
        Screens.ProductsScr,
        Screens.InventoryScr,
        Screens.CouponsScr
    )
    NavigationBar {
        val navBackStackEntry=navHostController.currentBackStackEntryAsState()
        val currentRoute=navBackStackEntry.value?.destination?.route
        bottomItems.forEach{ screen ->
            NavigationBarItem(
                selected = currentRoute==screen::class.simpleName,
                onClick = {
                    navHostController.navigate(screen){
                        popUpTo(navHostController.graph.startDestinationId){saveState=true}
                        launchSingleTop=true
                        restoreState=true
                    }
                },
                icon = {
                    Icon(imageVector = when(screen){
                        is Screens.DashboardScr-> Icons.Default.Home
                        is Screens.CouponsScr -> Icons.Default.MailOutline
                        is Screens.InventoryScr -> Icons.Default.AddCircle
                        is Screens.ProductsScr -> Icons.Default.ShoppingCart
                        else->Icons.Default.CheckCircle
                    },
                        contentDescription = screen::class.simpleName
                    )

                },
                label = {
                    Text(
                        when (screen) {
                            is Screens.DashboardScr -> "Dashboard"
                            is Screens.ProductsScr -> "Products"
                            is Screens.InventoryScr -> "Inventory"
                            is Screens.CouponsScr -> "Coupons"
                            else -> "Other"
                        }
                    )
                }
            )

        }
    }

}
