package com.example.winkcart_admin.InventoryScreen

import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.winkcart_admin.R
import com.example.winkcart_admin.Screens
import com.example.winkcart_admin.data.ResponseStatus
import com.example.winkcart_admin.model.Option
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.model.Variant
import com.example.winkcart_admin.productsScreen.components.AdminFailureState
import com.example.winkcart_admin.productsScreen.components.AdminLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    navHostController: NavHostController,
    viewModel: InventoryViewModel
) {
    val productState by viewModel.productState.collectAsState()

    BackHandler {
        navHostController.previousBackStackEntry?.savedStateHandle?.set("shouldRefresh", true)
        navHostController.popBackStack()
    }
    Scaffold(
        topBar = {
            if (productState is ResponseStatus.Success) {
                val product = (productState as ResponseStatus.Success<Product>).result
                TopAppBar(
                    title = {
                        Text(
                            text = product.title.take(25),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    },
                    actions = {
                        TextButton(onClick = {
                            navHostController.currentBackStackEntry?.savedStateHandle?.set(
                                "product", product
                            )
                            navHostController.navigate(Screens.ProductEditSrc)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Product",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit Product", color = MaterialTheme.colorScheme.primary)
                        }
                    }
                )
            } else {
                TopAppBar(
                    title = { Text("Inventory Management") }
                )
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {

            when (productState) {
                is ResponseStatus.Loading -> {
                    AdminLoading()
                }

                is ResponseStatus.Error -> {
                    AdminFailureState(
                        message = "Error loading product"
                    )
                }

                is ResponseStatus.Success -> {
                    val product = (productState as ResponseStatus.Success<Product>).result
                    val variants = product.variants

                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Product ID: ${product.id}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                        )

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                        ) {
                            items(variants.size) { index ->
                                val variant = variants[index]
                                VariantCard(
                                    index = index,
                                    total = variants.size,
                                    variant = variant,
                                    options = product.options,
                                    onQuantityChange = { newQuantity ->
                                        viewModel.updateVariantQuantity(variant.inventory_item_id, newQuantity)
                                    }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VariantCard(
    index: Int,
    total: Int,
    variant: Variant,
    options: List<Option>,
    onQuantityChange: (Int) -> Unit
) {
    var quantityText by remember { mutableStateOf(variant.inventory_quantity.toString()) }
    val context = LocalContext.current

    val option1Name = options.getOrNull(0)?.name ?: "Option 1"
    val option2Name = options.getOrNull(1)?.name ?: "Option 2"
    val option3Name = options.getOrNull(2)?.name ?: "Option 3"

    var originalQuantity by remember { mutableStateOf(variant.inventory_quantity) }
    val newQuantity = quantityText.toIntOrNull()
    val isValid = newQuantity != null && newQuantity != originalQuantity

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Variant ${index + 1} of $total",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = "Title: ${variant.title}", style = MaterialTheme.typography.titleSmall)

            variant.option1?.let { Text("$option1Name: $it") }
            variant.option2?.let { Text("$option2Name: $it") }
            variant.option3?.let { Text("$option3Name: $it") }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Inventory ID: ${variant.inventory_item_id}")
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = {
                    val clipboard = context.getSystemService(ClipboardManager::class.java)
                    clipboard?.setPrimaryClip(
                        ClipData.newPlainText("Inventory ID", variant.inventory_item_id.toString())
                    )
                    Toast.makeText(context, "Inventory ID copied", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.copy_icon),
                        contentDescription = "Copy Inventory ID",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }

            Text("Price: ${variant.price} ${variant.compare_at_price?.let { "(Was $it)" } ?: ""}")

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = quantityText,
                onValueChange = { quantityText = it },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    newQuantity?.let { onQuantityChange(it)
                        originalQuantity = it
                        Toast.makeText(context, "Quantity updated", Toast.LENGTH_SHORT).show()
                    }

                },
                enabled = isValid,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Update")
            }
        }
    }
}
