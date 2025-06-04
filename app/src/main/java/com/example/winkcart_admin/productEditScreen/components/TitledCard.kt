package com.example.winkcart_admin.productEditScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TitledCard(title:String,onDeleteOptionAction:(String)->Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box {
            Text(text = title, modifier = Modifier.padding(16.dp))

            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp),
                onClick = { onDeleteOptionAction(title) }

            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Product"
                )
            }
        }

    }
}