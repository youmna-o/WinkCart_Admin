package com.example.winkcart_admin.productsScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import com.example.winkcart_admin.model.Product
import com.example.winkcart_admin.R
import com.example.winkcart_admin.Screens

@Composable
fun ProductCard(product: Product, onDeleteAction: (Long) -> Unit,onProductClickAction:(Product,Screens)->Unit) {

    var dropDownMenuExpand by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .clickable(
                    onClick = {
                        dropDownMenuExpand = !dropDownMenuExpand
                        // 
                    }
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box{
                AsyncImage(
                    model = product.images?.firstOrNull()?.src,
                    contentDescription = product.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder =painterResource(id = R.drawable.product_placeholder),
                    error = painterResource(id = R.drawable.errorimg)
                )
                DropdownMenu(
                    expanded = dropDownMenuExpand,
                    onDismissRequest = { dropDownMenuExpand = false }
                ) { 
                    DropdownMenuItem(
                        text = {Text("Edit Product")},
                        onClick = {
                            dropDownMenuExpand=false
                            onProductClickAction(product,Screens.ProductEditSrc)
                        },
                    )
                    DropdownMenuItem(
                        text = {Text("Inventory")},
                        onClick = {
                            dropDownMenuExpand=false
                            onProductClickAction(product,Screens.InventoryScr)
                        },
                    )

                }

            }
            
            Spacer(modifier = Modifier.height(8.dp))

            Row( verticalAlignment = Alignment.Top) {
                Column (modifier = Modifier.weight(7f)){
                    Text(
                        text = product.title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "ID: ${product.id}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                IconButton(
                        onClick = { onDeleteAction.invoke(product.id) },
                modifier = Modifier.weight(2f)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }

}

