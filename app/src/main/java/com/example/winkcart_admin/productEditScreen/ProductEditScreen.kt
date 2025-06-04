package com.example.winkcart_admin.productEditScreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.model.Image
import com.example.winkcart_admin.model.Option
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.model.Variant
import com.example.winkcart_admin.productEditScreen.components.ImageSection
import com.example.winkcart_admin.productEditScreen.components.ProductFieldSection
import com.example.winkcart_admin.productEditScreen.components.ProductOptionsSection
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
                Text(if (isNewProduct) "Add Product" else "Modify Product")
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

@Composable
fun ProductVariantsSection(
    modifiedProduct: Product,
    productOptionsMap: SnapshotStateMap<String, SnapshotStateList<String>>,
    onProductChange: (Product) -> Unit
) {
    var variantList = modifiedProduct.variants.toMutableList()
    val isOptionExpanded = remember { mutableStateMapOf<String, Boolean>() }

    SideEffect {
        productOptionsMap.keys.forEach { key ->
            modifiedProduct.variants.forEachIndexed { index, _ ->
                val mapKey = "$index-$key"
                if (mapKey !in isOptionExpanded) {
                    isOptionExpanded[mapKey] = false
                }
            }
        }
    }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(width = 2.dp, color = Color.Black)
    ) {
        Row (modifier = Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Variants:", modifier = Modifier.padding(12.dp))
            IconButton(onClick = {
                val newVariant = Variant(
                    id =0,
                    product_id = modifiedProduct.id,
                    title = "default",
                    price = "0",
                    position = modifiedProduct.variants.size + 1,
                    inventory_policy = "deny",
                    compare_at_price = null,
                    option1 = null,
                    option2 = null,
                    option3 = null,
                    sku = null,
                    inventory_quantity = 0,
                    inventory_item_id = 0
                )
                onProductChange(modifiedProduct.copy(variants = modifiedProduct.variants + newVariant))
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Variant"
                )
            }
        }

        modifiedProduct.variants.forEachIndexed { index, variant ->
            var isWarningDisplayed by remember { mutableStateOf(false) }
            Row{
                Text(text = "Variant ${index + 1}:", modifier = Modifier.padding(4.dp), fontSize = 14.sp)
                if(isWarningDisplayed){
                    Text(text="You have to select value For each Option", color = Color.Red, fontSize = 14.sp)
                }

            }

            Row {
                OutlinedTextField(
                    value = variant.title,
                    onValueChange = {
                        variantList[index] = variant.copy(title = it)
                        onProductChange(modifiedProduct.copy(variants = variantList))
                    },
                    label = { Text("Title") },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(2f),
                    singleLine = true
                )
                OutlinedTextField(
                    value = variant.price,
                    onValueChange = {
                        variantList[index] = variant.copy(price = it)
                        onProductChange(modifiedProduct.copy(variants = variantList))
                    },
                    label = { Text("Price") },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = variant.inventory_quantity.toString(),
                    onValueChange = {
                        variantList[index] = variant.copy(inventory_quantity = it.toIntOrNull() ?: 0)
                        onProductChange(modifiedProduct.copy(variants = variantList))
                    },
                    label = { Text("Quantity") },
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Row(modifier = Modifier.padding(12.dp)) {
                productOptionsMap.keys.forEachIndexed { optIndex, key ->
                    val mapKey = "$index-$key"
                    val optionValue = when (optIndex) {
                        0 -> variant.option1
                        1 -> variant.option2
                        2 -> variant.option3
                        else -> null

                    }

                    val displayText = if (!optionValue.isNullOrBlank()) {
                        optionValue
                    } else {
                        key
                    }
                    isWarningDisplayed = displayText==key
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {

                                isOptionExpanded[mapKey] = !(isOptionExpanded[mapKey] ?: false)

                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(text = displayText)
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = isOptionExpanded[mapKey] ?: false,
                            onDismissRequest = { isOptionExpanded[mapKey] = false }
                        ) {
                            productOptionsMap[key]?.forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        val updatedVariant = when (optIndex) {
                                            0 -> variant.copy(option1 = option)
                                            1 -> variant.copy(option2 = option)
                                            2 -> variant.copy(option3 = option)
                                            else -> variant
                                        }
                                        variantList[index] = updatedVariant

                                        onProductChange(modifiedProduct.copy(variants = variantList))

                                        isOptionExpanded[mapKey] = false
                                    },
                                    text = { Text(option) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
