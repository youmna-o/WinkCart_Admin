package com.example.winkcart_admin.productEditScreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.model.Variant

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
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Variants:", modifier = Modifier.padding(12.dp))
            IconButton(onClick = {
                val newVariant = Variant(
                    id = 0,
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Variant ${index + 1}:",
                    modifier = Modifier.padding(4.dp),
                    fontSize = 14.sp
                )
                if (isWarningDisplayed) {
                    Text(
                        text = "You have to select value For each Option",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
                IconButton(onClick = {
                    val modifiedList = modifiedProduct.variants.toMutableList()
                    modifiedList.removeAt(index)
                    onProductChange(modifiedProduct.copy(variants = modifiedList.toList()))
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "remove Variant"
                    )
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
                        .padding(4.dp)
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
                        .padding(4.dp)
                        .weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = variant.inventory_quantity.toString(),
                    onValueChange = {
                        variantList[index] =
                            variant.copy(inventory_quantity = it.toIntOrNull() ?: 0)
                        onProductChange(modifiedProduct.copy(variants = variantList))
                    },
                    label = { Text("Quantity") },
                    modifier = Modifier
                        .padding(4.dp)
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
                    isWarningDisplayed = displayText == key
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