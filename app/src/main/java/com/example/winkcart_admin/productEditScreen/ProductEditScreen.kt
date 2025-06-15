package com.example.winkcart_admin.productEditScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.model.Image
import com.example.winkcart_admin.model.Option
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.productEditScreen.components.ImageSection
import com.example.winkcart_admin.productEditScreen.components.ProductFieldSection
import com.example.winkcart_admin.productEditScreen.components.ProductOptionsSection
import com.example.winkcart_admin.productEditScreen.components.ProductVariantsSection
import com.example.winkcart_admin.productsScreen.components.AdminAlert

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ProductEditScreen(navHostController: NavHostController,viewModel: ProductEditViewModel) {

    val productState by viewModel.productState.collectAsState()
    val scrollState = rememberScrollState()
    var isNewProduct by remember { mutableStateOf(false) }
    var isTitleErrorAlert by remember { mutableStateOf(false) }
    var modifiedProduct by remember { mutableStateOf(Product()) }

    val productImages = remember { mutableStateListOf<String>() }
    val productOptionsMap = remember {
        mutableStateMapOf<String, SnapshotStateList<String>>()
    }
    var confirmOptionDeleteShowAlert by remember { mutableStateOf(false) }
    var confirmOptionValueDeleteShowAlert by remember { mutableStateOf(false) }
    var optionSelectedToDelete by remember { mutableStateOf("") }
    var optionValueSelectedToDelete by remember { mutableStateOf("") }
    BackHandler {
        navHostController.previousBackStackEntry?.savedStateHandle?.set("shouldRefresh", true)
        navHostController.popBackStack()
    }
    LaunchedEffect(productState) {
        when (val state = productState) {
            is ResponseStatus.Success -> {
                val updatedProduct = state.result
                isNewProduct = updatedProduct.id == 0L
                modifiedProduct = updatedProduct

                productImages.clear()
                productImages.addAll(updatedProduct.images?.map { it.src  } ?: listOf())

                productOptionsMap.clear()
                updatedProduct.options.forEach {
                    productOptionsMap[it.name] = it.values.toMutableStateList()
                }

            }
            is ResponseStatus.Error -> {

                Log.e("ProductEdit", "Error: ${(productState as ResponseStatus.Error).error}")
            }
            else -> {  }
        }
    }
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            ImageSection(
                productImages = productImages,
                onDeleteImageAction = { productImages.remove(it) },
                onAddImageAction = { productImages.add(it) },
            )
            TextField(
                value = if (isNewProduct) "Product ID will be generated automatically" else modifiedProduct.id.toString(),
                onValueChange = {},
                readOnly = true,
                label = { Text("ID")},
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            )

            ProductFieldSection(
                product = modifiedProduct,
                onProductChange = { modifiedProduct = it }
            )

            ProductOptionsSection(
                productOptionsMap = productOptionsMap,
                onAddOptionAction = { optionName ->
                    productOptionsMap[optionName] = mutableStateListOf()
                    val newOption = Option(
                        id = 0,
                        product_id =0,
                        name = optionName,
                        values = listOf()
                    )
                    modifiedProduct = modifiedProduct.copy(
                        options = modifiedProduct.options + newOption
                    )
                },
                isOptionFieldEnabled = { productOptionsMap.size < 3 },
                onAddValueToOptionAction = { optionValue, selectedOption ->
                    productOptionsMap[selectedOption]?.add(optionValue)

                },
                onDeleteOptionAction = {
                    optionSelectedToDelete = it
                    confirmOptionDeleteShowAlert = true
                },
                onDeleteValueToOptionAction = { optionValue, selectedOption ->
                    optionSelectedToDelete=selectedOption
                    optionValueSelectedToDelete=optionValue
                    confirmOptionValueDeleteShowAlert=true
                },
            )
            ProductVariantsSection(
                modifiedProduct = modifiedProduct,
                productOptionsMap = productOptionsMap,
                onProductChange = {
                    modifiedProduct = it
                }
            )



            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                onClick = {
                if(modifiedProduct.title.isBlank()) {
                    isTitleErrorAlert = true
                    return@Button
                }
                val options = productOptionsMap.map { (key, values) ->
                    if(values.isEmpty()){
                        return@Button
                    }
                    Option(0, modifiedProduct.id, key, values.toList())
                }
                val finalProduct = modifiedProduct.copy(
                    options = options,
                    images = productImages.map { Image(null, null, it) }
                )
                if (isNewProduct) {
                    viewModel.createProduct(finalProduct)
                } else {
                    viewModel.editProduct(finalProduct)
                }
            }) {
                if(productState is ResponseStatus.Loading){
                    Log.i("TAG", "ProductEditScreen: loooooooading pridcut edit/create")
                    CircularProgressIndicator(color = Color.LightGray)
                }
                else{
                    Text(if (isNewProduct) "Add Product" else "Modify Product")
                }

            }
        }
        if (isTitleErrorAlert){
            AdminAlert(
                title = "Missing Title",
                message = "Products Must have a title",
                confirmMessage = "Confirm",
                onDismissAction = { isTitleErrorAlert=false },
                onConfirmAction = { isTitleErrorAlert=false }
            )
        }

        if (confirmOptionDeleteShowAlert){
            AdminAlert(
                title = "Delete Option",
                message = "Are you Sure You want to Delete the Option With its Variants",
                confirmMessage = "Confirm",
                onDismissAction = { confirmOptionDeleteShowAlert=false },
                onConfirmAction = {
                    productOptionsMap.remove(optionSelectedToDelete)
                    val optionIndex = modifiedProduct.options.indexOfFirst { it.name == optionSelectedToDelete }
                    if (optionIndex in 0..2) {
                        val updatedVariants = modifiedProduct.variants.map { variant ->
                            when (optionIndex) {
                                0 -> variant.copy(option1 = null)
                                1 -> variant.copy(option2 = null)
                                2 -> variant.copy(option3 = null)
                                else -> variant
                            }
                        }
                        val updatedOptions = modifiedProduct.options.filter { it.name != optionSelectedToDelete }
                        modifiedProduct = modifiedProduct.copy(
                            variants = updatedVariants,
                            options = updatedOptions
                        )
                    }

                    confirmOptionDeleteShowAlert = false
                }

            )
        }
        if (confirmOptionValueDeleteShowAlert){
            AdminAlert(
                title = "Delete Option Value",
                message = "Are you Sure You want to Delete \"${optionValueSelectedToDelete}\" for option \"${optionSelectedToDelete}\"",
                confirmMessage = "Confirm",
                onDismissAction = { confirmOptionValueDeleteShowAlert=false },
                onConfirmAction = {
                    productOptionsMap[optionSelectedToDelete]?.remove(optionValueSelectedToDelete)

                    confirmOptionValueDeleteShowAlert=false
                }
            )
        }
    }
}

