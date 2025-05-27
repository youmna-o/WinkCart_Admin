package com.example.winkcart_admin.model

data class ProductListResponse(
    val products: List<Product>
)

data class SingleProductResponse(
    val product: Product
)

data class Product(
    val id: Long,
    val title: String,
    val body_html: String?,
    val vendor: String?,
    val product_type: String?,
    val created_at: String?,
    val handle: String?,
    val updated_at: String?,
    val published_at: String?,
    val tags: String?,
    val status: String?,
    val variants: List<Variant>?,
    val options: List<Option>?,
    val images: List<Image>?
)

data class Variant(
    val id: Long,
    val product_id: Long,
    val title: String,
    val price: String,
    val position: Int,
    val inventory_policy: String,
    val compare_at_price: String?,
    val option1: String?,
    val option2: String?,
    val option3: String?,
    val sku: String?,
    val inventory_quantity: Int,
    val inventory_item_id: Long
)

data class Option(
    val id: Long,
    val product_id: Long,
    val name: String,
    val values: List<String>
)

data class Image(
    val id: Long,
    val product_id: Long,
    val src: String
)
