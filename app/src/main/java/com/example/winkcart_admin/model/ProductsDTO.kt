package com.example.winkcart_admin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ProductListResponse(
    val products: MutableList<Product>
)

data class SingleProductResponse(
    val product: Product
)

@Parcelize
data class Product(
    var id: Long=0L,
    var title: String="",
    var body_html: String? = "",
    var vendor: String? = "",
    var product_type: String? = "",
    var created_at: String? = "",
    var handle: String? = "",
    var updated_at: String? = "",
    var published_at: String? = "",
    var tags: String? = "",
    var status: String? = "active",
    var variants: List<Variant> = listOf(),
    var options: List<Option> = listOf(),
    var images: List<Image>? = listOf()
) : Parcelable

@Parcelize
data class Variant(
    var id: Long?=0,
    var product_id: Long,
    var title: String,
    var price: String,
    var position: Int,
    var inventory_policy: String,
    var compare_at_price: String?,
    var option1: String?,
    var option2: String?,
    var option3: String?,
    var sku: String?,
    var inventory_quantity: Int,
    var inventory_item_id: Long
):Parcelable
@Parcelize
data class Option(
    val id: Long,
    val product_id: Long,
    val name: String,
    val values: List<String>
):Parcelable
@Parcelize
data class Image(
    val id: Long?,
    val product_id: Long?,
    val src: String
):Parcelable
