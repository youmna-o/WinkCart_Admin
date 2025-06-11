package com.example.winkcart_admin.model

data class CouponsModel(
    val id:Long,
    val title: String,
    val targetType: String,
    val valueType: String,
    val value: String,
    val startsAt: String,
    val endsAt: String,
    val usageLimit: Int,
    val discountCodeMap: MutableMap<Long,String>

)