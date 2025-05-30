package com.example.winkcart_admin

import com.example.winkcart_admin.model.Product
import kotlinx.serialization.Serializable

@Serializable
sealed class Screens {
    @Serializable
    data object DashboardScr:Screens()
    @Serializable
    data object ProductsScr:Screens()
    @Serializable
    data object InventoryScr:Screens()
    @Serializable
    data object CouponsScr:Screens()
    @Serializable
    data class ProductDetailsSrc(val product: Product):Screens()
}