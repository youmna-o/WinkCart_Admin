package com.example.winkcart_admin.model

import com.google.gson.annotations.SerializedName

data class DiscountCodeRequest(
    @SerializedName("discount_code")
    val discountCode: DiscountCode
)
data class DiscountCode(
    val code: String
)
data class DiscountCodeResponse(
    @SerializedName("discount_codes")
    val discountCodeList: List<DiscountCodeDetails>?=null
)

data class DiscountCodeDetails(
    val id: Long,
    @SerializedName("price_rule_id")
    val priceRuleId: Long,
    val code: String,
    @SerializedName("usage_count")
    val usageCount: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at:")
    val updatedAt: String
)

