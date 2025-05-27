package com.example.winkcart_admin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.remote.RemoteDataSource
import com.example.winkcart_admin.data.remote.RemoteDataSourceImpl
import com.example.winkcart_admin.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_admin.data.repository.ProductRepo
import com.example.winkcart_admin.data.repository.ProductRepoImpl
import com.example.winkcart_admin.productsScreen.ProductsViewModel
import com.example.winkcart_admin.ui.theme.WinkCart_AdminTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewmodel=ProductsViewModel(ProductRepoImpl(
            remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.productService)
        ))
        setContent {
            WinkCart_AdminTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WinkCart_AdminTheme {
        Greeting("Android")
    }
}