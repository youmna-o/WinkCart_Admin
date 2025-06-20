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
    @Serializable
    data class CouponsEditScr(val couponId:Long):Screens()
    @Serializable
    data object LoginScr:Screens()
    @Serializable
    data object AboutUsScr:Screens()
    @Serializable
    data object ProfileScr:Screens()

}
