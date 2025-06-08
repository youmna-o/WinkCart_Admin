package com.example.winkcart_admin.model

data class InventoryLevelSetRequest(
    val location_id: Long,
    val inventory_item_id: Long,
    val available: Int
)
