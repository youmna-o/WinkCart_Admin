package com.example.winkcart_admin

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle

import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.navigation.toRoute
import com.example.winkcart_admin.CouponsEditScreen.CouponsEditScreen
import com.example.winkcart_admin.CouponsScreen.CouponsScreen
import com.example.winkcart_admin.InventoryScreen.InventoryScreen
import com.example.winkcart_admin.InventoryScreen.InventoryViewModelFactory
import com.example.winkcart_admin.LoginScreen.LoginScreen
import com.example.winkcart_admin.ProfileScreen.ProfileScreen
import com.example.winkcart_admin.aboutusScreen.AboutUsScreen

import com.example.winkcart_admin.data.remote.RemoteDataSourceImpl
import com.example.winkcart_admin.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_admin.data.repository.AuthRepository
import com.example.winkcart_admin.data.repository.ProductRepoImpl
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.productEditScreen.ProductEditScreen
import com.example.winkcart_admin.productEditScreen.ProductEditViewModelFactory
/*import com.example.winkcart_admin.productEditScreen.ProductEditViewModelFactory*/
import com.example.winkcart_admin.productsScreen.ProductsScreen
import com.example.winkcart_admin.utils.NetworkAwareWrapper
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MainApp() {
    val navHostController= rememberNavController()
    val authRepo=AuthRepository(FirebaseAuth.getInstance())

    NetworkAwareWrapper {
        NavHost(
            navController = navHostController,
            startDestination = if (authRepo.isAdminLoggedIn()) Screens.ProductsScr else Screens.LoginScr
        ) {
            composable<Screens.ProductsScr> {
                ProductsScreen(navHostController = navHostController)

            }
            composable<Screens.DashboardScr> {
                DashboardScreen(navHostController)
            }
            composable<Screens.CouponsScr> {
                CouponsScreen(
                    navHostController = navHostController
                )
            }
            composable<Screens.InventoryScr> {
                val product =
                    navHostController.previousBackStackEntry?.savedStateHandle?.get<Product>("product")
                if (product != null) {
                    InventoryScreen(
                        navHostController = navHostController,
                        viewModel = viewModel(
                            factory = InventoryViewModelFactory(
                                repository = ProductRepoImpl(
                                    remoteDataSource = RemoteDataSourceImpl(
                                        adminServices = RetrofitHelper.productService
                                    )
                                ),
                                product = product
                            )
                        )
                    )
                } else {
                    Log.i("TAG", "MainApp: product is null before opening product Inventory")
                }


            }
            composable<Screens.ProductEditSrc> {
                val product =
                    navHostController.previousBackStackEntry?.savedStateHandle?.get<Product>("product")

                if (product != null) {
                    ProductEditScreen(
                        navHostController,
                        viewModel = viewModel(
                            factory = ProductEditViewModelFactory(
                                repository = ProductRepoImpl(
                                    remoteDataSource = RemoteDataSourceImpl(
                                        adminServices = RetrofitHelper.productService
                                    )
                                ),
                                product = product
                            )
                        )
                    )
                } else {
                    Log.i("TAG", "MainApp: product is null before opening product details")
                }
            }
            composable<Screens.CouponsEditScr> { navBackStack ->

                val data = navBackStack.toRoute<Screens.CouponsEditScr>()
                CouponsEditScreen(
                    navHostController = navHostController,
                    couponId = data.couponId
                )
            }
            composable<Screens.LoginScr> {
                LoginScreen(navController = navHostController)
            }
            composable<Screens.AboutUsScr> {
                AboutUsScreen()
            }
            composable<Screens.ProfileScr> {
                ProfileScreen(navHostController)
            }

        }
    }

}

@Composable
fun DashboardScreen(navHostController: NavHostController) {

    Scaffold(
        bottomBar = {BottomNavigationBar(navHostController)}
    ) {padding->

        Button(onClick = {}) { }
        Text(text = "DashBoard Screen", modifier = Modifier.padding(padding))
    }

}

@Composable
fun BottomNavigationBar(navHostController: NavHostController) {

    val bottomItems= listOf(
        Screens.ProductsScr,
        Screens.CouponsScr,
        Screens.ProfileScr
    )
    NavigationBar {
        val navBackStackEntry=navHostController.currentBackStackEntryAsState()
        val currentRoute=navBackStackEntry.value?.destination?.route
        bottomItems.forEach{ screen ->
            val isSelected = currentRoute == screen::class.qualifiedName

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navHostController.navigate(screen){
                        popUpTo(navHostController.graph.startDestinationId){saveState=true}
                        launchSingleTop=true
                        restoreState=true
                    }
                },
                icon = {
                    Icon(imageVector = when(screen){
                        //is Screens.DashboardScr-> Icons.Default.Home
                        is Screens.CouponsScr -> Icons.Default.MailOutline
                        is Screens.ProductsScr -> Icons.Default.ShoppingCart
                        is Screens.ProfileScr ->Icons.Default.MoreHoriz
                        else->Icons.Default.CheckCircle
                    },
                        contentDescription = screen::class.simpleName
                    )

                },
                label = {
                    Text(
                        when (screen) {
                            //is Screens.DashboardScr -> "Dashboard"
                            is Screens.ProductsScr -> "Products"
                            is Screens.CouponsScr -> "Coupons"
                            is Screens.ProfileScr-> "More"
                            else -> "Other"
                        }
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

        }
    }

}
