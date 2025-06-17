package com.example.winkcart_admin.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

@Composable
fun NetworkAwareWrapper(content: @Composable () -> Unit) {
    val isConnected by rememberIsConnected()

    if (!isConnected) {
        NoInternetScreen()
    } else {
        content()
    }
}
