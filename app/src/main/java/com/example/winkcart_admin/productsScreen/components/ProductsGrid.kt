package com.example.winkcart_admin.productsScreen.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.winkcart_admin.model.Product

@Composable
fun ProductsGrid(products: List<Product>,onProductDeleteAction:(Long)->Unit,onProductClickAction:(Product)->Unit) {
    var showDialog by remember { mutableStateOf(false) }
    var productIdToBeDeleted by remember { mutableLongStateOf(0L)  }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) {
            ProductCard(
                product = it,
                onDeleteAction = { id ->
                    showDialog = true
                    productIdToBeDeleted = id
                },
                onProductClickAction = onProductClickAction
            )
        }
    }
    if (showDialog) {
        AdminAlert(
            title = "Confirm Deletion",
            message = "Are you sure you want to delete this product?",
            confirmMessage = "Delete",
            onDismissAction = { showDialog = false },
            onConfirmAction = {
                Log.i("TAG", "ProductsGrid: ${productIdToBeDeleted}")
                onProductDeleteAction(productIdToBeDeleted)
                showDialog = false
            }
        )
    }

}

@Composable
fun AdminAlert(
    title: String,
    message: String,
    confirmMessage: String,
    onDismissAction: () -> Unit,
    onConfirmAction: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissAction,
        title = {
            Text(text = title)
        },
        text = {
            Text(text = message)
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(
                    onClick = onDismissAction,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = onConfirmAction,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = confirmMessage)
                }
            }
        },
        dismissButton = {},
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        containerColor = MaterialTheme.colorScheme.surface
    )
}
