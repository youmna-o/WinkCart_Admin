package com.example.winkcart_admin.productEditScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.winkcart_admin.R

@Composable
fun ImageCard(imageSrc: String,onDeleteImageAction:()->Unit) {

    Card(
        modifier = Modifier
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
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