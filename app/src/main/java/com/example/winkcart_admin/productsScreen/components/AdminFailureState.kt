package com.example.winkcart_admin.productsScreen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winkcart_admin.R

@Composable
fun AdminFailureState(message: String) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier
                .size(250.dp)
                .padding(bottom = 32.dp),
            painter = painterResource(R.drawable.errorimg),
            contentDescription = "Error Image"
        )
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = "oops! an error occurred!",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            text = message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.error,
            fontSize = 16.sp

        )
    }

}