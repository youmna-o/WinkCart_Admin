package com.example.winkcart_admin.productEditScreen.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ImageSection(
    productImages: SnapshotStateList<String>,
    onDeleteImageAction: (String) -> Unit,
    onAddImageAction:(String)->Unit,
) {
    var productImageLink by remember { mutableStateOf("") }
    LazyRow(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(200.dp)
    ) {
        items(items = productImages) { image ->
            ImageCard(
                imageSrc = image,
                onDeleteImageAction = { onDeleteImageAction(image) }
            )
        }
    }
    OutlinedTextField(
        value = productImageLink,
        onValueChange = {
            productImageLink = it
        },
        label = { Text("Image URL") },
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        trailingIcon = {
            IconButton(
                content = {
                    Icon(Icons.Default.Add, contentDescription = "Adding Icon")
                },
                onClick = {
                    onAddImageAction(productImageLink)
                    productImageLink = ""
                }
            )
        },
        singleLine = true
    )
}