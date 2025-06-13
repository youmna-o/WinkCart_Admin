
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
import androidx.lifecycle.lifecycleScope
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.data.remote.RemoteDataSource
import com.example.winkcart_admin.data.remote.RemoteDataSourceImpl
import com.example.winkcart_admin.data.remote.retrofit.RetrofitHelper
import com.example.winkcart_admin.data.repository.ProductRepo
import com.example.winkcart_admin.data.repository.ProductRepoImpl
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.productsScreen.ProductsViewModel
import com.example.winkcart_admin.ui.theme.WinkCart_AdminTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        /*val viewModel=ProductsViewModel(ProductRepoImpl(
            remoteDataSource = RemoteDataSourceImpl(RetrofitHelper.productService)
        ))*/
        //testCRUDOperations(viewModel)

        setContent {
            WinkCart_AdminTheme {
                MainApp()
            }
        }
    }



    //test function that should be deleted .
    //it was created to test the CRUD methods of Product
    //each operation should be done alone since we are not keeping the data
    private fun testCRUDOperations(viewModel: ProductsViewModel) {
        // Create a Product (mock data)
        val newProduct = Product(
            id = 0, // ID will be generated after creating
            title = "Test Product",
            body_html = "This is a test product",
            vendor = "Test Vendor",
            product_type = "Test Type",
            created_at = "2023-05-28T00:00:00Z",
            handle = "test-product",
            updated_at = "2023-05-28T00:00:00Z",
            published_at = "2023-05-28T00:00:00Z",
            tags = "test,product",
            status = "active",
            variants = listOf(),
            options = listOf(),
            images = listOf()
        )

        lifecycleScope.launch {
            //create then update
            /*viewModel.createProduct(newProduct)
            viewModel.productState.collect { result ->
                when (result) {
                    is ResponseStatus.Loading -> {
                        Log.d("MainActivity", "Creating product...")
                    }
                    is ResponseStatus.Success -> {
                        val createdProduct = result.result
                        Log.d("MainActivity", "Product created: ${createdProduct.title}")

                        // Step 2: Update the product
                        val updatedProduct = createdProduct.copy(
                            title = "Updated Test Product",
                            vendor = "Updated Test Vendor"
                        )
                        Log.i("TAG", "testCRUDOperations: tesssssssssst")
                        viewModel.updateProduct(createdProduct.id, updatedProduct)
                    }
                    is ResponseStatus.Error -> {
                        Log.e("MainActivity", "Error creating product: ${result.error.message}")
                    }
                }
            }*/

            //update image URL
            /*val imageUrl = "https://cdn.shopify.com/s/files/1/0758/1132/4152/files/product_29_image1.jpg?v=1748153929"
            viewModel.addImageToProduct(9085607608568, imageUrl)*/



            //deleting image from product
            /*
            viewModel.getProductById(9085607608568)
            viewModel.productState.collect { result ->
                when (result) {
                    is ResponseStatus.Loading -> {
                        Log.d("MainActivity", "Got product...")
                    }
                    is ResponseStatus.Success -> {
                        val gotProduct = result.result
                        Log.d("MainActivity", "Product Got: ${gotProduct.title}")
                        val imageId = gotProduct.images?.first()?.id
                        if (imageId != null) {
                            viewModel.deleteImageFromProduct(gotProduct.id, imageId)
                        }
                    }
                    is ResponseStatus.Error -> {
                        Log.e("MainActivity", "Error creating product: ${result.error.message}")
                    }
                }
            }*/

            //deleting a product
            //viewModel.deleteProduct(9085607608568)

        }
    }
}

