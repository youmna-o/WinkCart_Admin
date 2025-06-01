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
