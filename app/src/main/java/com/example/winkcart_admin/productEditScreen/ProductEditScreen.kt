package com.example.winkcart_admin.productEditScreen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.winkcart_admin.R
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.model.Image
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.productsScreen.components.AdminAlert

@Composable
fun ProductEditScreen(navHostController: NavHostController,viewModel: ProductEditViewModel) {

    val productState by viewModel.productState.collectAsState()
    val scrollState = rememberScrollState()
    var isNewProduct by remember { mutableStateOf(false) }
    var showAlert by remember { mutableStateOf(false) }
    var productID by remember { mutableStateOf("Product ID will be generated automatically") }
    var productTitle by remember { mutableStateOf("") }
    var productType by remember { mutableStateOf("") }
    var productTags by remember { mutableStateOf("") }
    var productVendor by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPrice by remember { mutableDoubleStateOf(0.0) }
    val productImages = remember { mutableStateListOf<String>() }
    var productImageLink by remember { mutableStateOf("") }

    /*    var productTitle by remember { mutableStateOf(if (isNewProduct) "" else product.title) }
        var productType by remember { mutableStateOf(if (isNewProduct) "" else product.product_type ?: "") }
        var productTags by remember { mutableStateOf(if (isNewProduct) "" else product.tags ?: "") }
        var productVendor by remember { mutableStateOf(if (isNewProduct) "" else product.vendor ?: "") }
        var productDescription by remember { mutableStateOf(if (isNewProduct) "" else product.body_html ?: "") }
        val productImages = remember { mutableStateListOf(*(product.images?.map { it.src?: "" } ?: listOf()).toTypedArray()) }
        var productImageLink by remember { mutableStateOf("") }*/

    LaunchedEffect(productState) {
        when (productState) {
            is ResponseStatus.Success -> {
                val updatedProduct = (productState as ResponseStatus.Success).result

                isNewProduct= updatedProduct.id== 0L
                productID = if(isNewProduct){
                    "Product ID will be generated automatically"
                } else{
                    updatedProduct.id.toString()
                }
                productTitle = updatedProduct.title
                productTags = updatedProduct.tags ?: ""
                productType = updatedProduct.product_type ?: ""
                productDescription = updatedProduct.body_html ?: ""
                productVendor = updatedProduct.vendor ?: ""
                productImages.clear()
                productImages.addAll(updatedProduct.images?.map { it.src  } ?: listOf())
            }
            is ResponseStatus.Error -> {
                // Handle error state, e.g., show an error message
                Log.e("ProductEdit", "Error: ${(productState as ResponseStatus.Error).error}")
            }
            else -> { /* Loading state, no action needed */ }
        }
    }
    //var productTitle by remember { mutableStateOf(product.) }
    Scaffold { padding ->
        Log.i("TAG", "ProductEditScreen: ${productID}")
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            LazyRow(Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(200.dp)) {
                items(items = productImages){ image ->
                    ImageCard(
                        imageSrc = image,
                        onDeleteImageAction = {
                            productImages.remove(image)
                        }
                    )
                }
            }
            TextField(
                value = productID,
                onValueChange = {},
                readOnly = true,
                label = { Text("ID")},
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value =productImageLink,
                onValueChange = {
                    productImageLink=it
                },
                label = { Text("Image URL")},
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        content = {
                            Icon(Icons.Default.Add, contentDescription = "Adding Icon")
                        },
                        onClick ={
                            productImages.add(productImageLink)
                        }
                    )
                },
                singleLine = true
            )
            OutlinedTextField(
                value =productTitle,
                onValueChange = {
                    productTitle=it
                },
                label = { Text("Title")},
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value =productType,
                onValueChange = {
                    productType=it
                },
                label = { Text("Type")},
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value =productTags,
                onValueChange = {
                    productTags=it
                },
                label = { Text("Tags")},
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value =productVendor,
                onValueChange = {
                    productVendor=it
                },
                label = { Text("Vendor")},
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            )
            OutlinedTextField(
                value =productDescription,
                onValueChange = {
                    productDescription=it
                },
                label = { Text("Description")},
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
            )
            /*OutlinedTextField(
                value =productPrice.toString(),
                onValueChange = {
                    productPrice=it.toDouble()
                },
                label = { Text("Price")},
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
            )*/
            Button(onClick = {
                if(productTitle.isNotBlank()){
                    val product =if(isNewProduct) {
                        Product()
                    }else{
                        (productState as ResponseStatus.Success).result
                    }
                    product.title=productTitle
                    product.tags=productTags
                    product.product_type=productType
                    product.body_html=productDescription
                    product.vendor=productVendor
                    //product.
                    if(isNewProduct){
                        product.images= productImages.toList().map {
                            Image(
                                src = it,
                                id = null,
                                product_id = null
                            )
                        }
                        viewModel.createProduct(product)
                    }else{
                        viewModel.editProduct(product,productImages.toList())
                    }
                }
                else{
                    showAlert=true
                }

            }) {
                Text(text = if(isNewProduct){
                    "Add Product"
                }else{
                    "Modify Product"
                })
            }
        }
        if (showAlert){
            AdminAlert(
                title = "Missing Title",
                message = "Products Must have a title",
                confirmMessage = "Confirm",
                onDismissAction = { showAlert=false },
                onConfirmAction = { showAlert=false }
            )
        }
    }
}

@Composable
fun ImageCard(imageSrc: String,onDeleteImageAction:()->Unit) {

    Card(
        modifier =Modifier
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Box(modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = imageSrc,
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp)),
                contentDescription = "product Image",
                error = painterResource(R.drawable.errorimg),
                placeholder = painterResource(R.drawable.product_placeholder)
            )
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(40.dp),
                onClick = {
                    onDeleteImageAction()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Product"
                )
            }
        }

    }
}
