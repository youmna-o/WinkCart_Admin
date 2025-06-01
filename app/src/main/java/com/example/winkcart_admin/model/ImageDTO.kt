package com.example.winkcart_admin.model

data class ImageRequest(
    val image: ImageData
)

data class ImageData(
    val src: String
)

data class ImageResponse(
    val image: ImageData
)
