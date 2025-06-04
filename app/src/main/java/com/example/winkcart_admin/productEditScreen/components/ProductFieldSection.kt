package com.example.winkcart_admin.productEditScreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.winkcart_admin.model.Product


@Composable
fun ProductFieldSection(
    product: Product,
    onProductChange: (Product) -> Unit
) {
    OutlinedTextField(
        value = product.title,
        onValueChange = { onProductChange(product.copy(title = it)) },
        label = { Text("Title") },
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    )

    OutlinedTextField(
        value = product.product_type.orEmpty(),
        onValueChange = { onProductChange(product.copy(product_type = it)) },
        label = { Text("Type") },
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    )

    OutlinedTextField(
        value = product.tags.orEmpty(),
        onValueChange = { onProductChange(product.copy(tags = it)) },
        label = { Text("Tags") },
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    )

    OutlinedTextField(
        value = product.vendor.orEmpty(),
        onValueChange = { onProductChange(product.copy(vendor = it)) },
        label = { Text("Vendor") },
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    )

    OutlinedTextField(
        value = product.body_html.orEmpty(),
        onValueChange = { onProductChange(product.copy(body_html = it)) },
        label = { Text("Description") },
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
    )
}