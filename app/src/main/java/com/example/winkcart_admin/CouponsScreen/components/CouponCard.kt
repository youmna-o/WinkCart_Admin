package com.example.winkcart_admin.CouponsScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.winkcart_admin.R
import com.example.winkcart_admin.model.CouponsModel

@Composable
fun CouponCard(
    coupon: CouponsModel,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val imageRes = when (coupon.valueType) {
        "percentage" -> R.drawable.percentage_discount
        "fixed_amount" -> R.drawable.fixed_discount
        else -> R.drawable.percentage_discount
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 8.dp)
                    )
                    Column {
                        Text(text = coupon.title, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "Value: ${coupon.value}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Start: ${coupon.startsAt.take(10)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "End: ${coupon.endsAt.take(10)}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (coupon.discountCodeMap.isNotEmpty()) {
                Text(
                    text = "Codes:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                coupon.discountCodeMap.values.forEach { code ->
                    Text(
                        text = "• $code",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            } else {
                Text(
                    text = "No discount codes available.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}