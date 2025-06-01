package com.example.winkcart_admin

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
    data object ProductEditSrc:Screens()

}
