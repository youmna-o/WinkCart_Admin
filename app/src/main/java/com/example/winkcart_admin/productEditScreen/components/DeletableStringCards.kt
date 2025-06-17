package com.example.winkcart_admin.productEditScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DeletableStringCards(stringList: List<String>,onDeleteOptionAction:(String)->Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        items(stringList) { optionName ->
            TitledCard(
                title = optionName,
                onDeleteOptionAction = { onDeleteOptionAction(optionName) },
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
            )
        }
    }
}