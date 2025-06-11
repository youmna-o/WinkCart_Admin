package com.example.winkcart_admin.model

import com.google.gson.annotations.SerializedName

data class PriceRulesResponse(
    val price_rules: List<PriceRule>
)
data class PriceRuleRequest(
    val price_rule: PriceRule
)


data class PriceRule(

    @SerializedName("id")
    val id:Long,

    @SerializedName("title")
    val title: String, // ✅ Required: Internal name of the price rule (e.g., "SAVE20")

    @SerializedName("target_type")
    val targetType: String, // ✅ Required: "line_item" (product discount) or "shipping_line" (shipping discount)

    @SerializedName("target_selection")
    val targetSelection: String, // ✅ Required: "all" (all items) or "entitled" (specific products/collections)

    @SerializedName("allocation_method")
    val allocationMethod: String, // ✅ Required: "across" (even distribution) or "each" (per item)

    @SerializedName("value_type")
    val valueType: String, // ✅ Required: "percentage", "fixed_amount", or "shipping"

    @SerializedName("value")
    val value: String, // ✅ Required: Always negative (e.g., "-10.0" for $10 off or "-20.0" for 20% off)

    @SerializedName("customer_selection")
    val customerSelection: String, // ✅ Required: "all" (all customers) or "prerequisite" (filtered customers)

    @SerializedName("starts_at")
    val startsAt: String, // ✅ Required: Start time in ISO 8601 format (e.g., "2025-06-09T00:00:00Z")

    //  Optional Fields

    @SerializedName("ends_at")
    val endsAt: String? = null, // ⛔ Optional: End time in ISO 8601 format

    @SerializedName("usage_limit")
    val usageLimit: Int? = null, // ⛔ Optional: Max total uses across all customers

    @SerializedName("once_per_customer")
    val oncePerCustomer: Boolean? = null, // ⛔ Optional: If true, each customer can use the code only once

    @SerializedName("prerequisite_subtotal_range")
    val prerequisiteSubtotalRange: PrerequisiteSubtotalRange? = null, // ⛔ Optional: Minimum cart value (e.g., ≥ $50)

    @SerializedName("entitled_product_ids")
    val entitledProductIds: List<Long>? = null, // ⛔ Optional: Product IDs eligible (if targetSelection = "entitled")

    @SerializedName("entitled_collection_ids")
    val entitledCollectionIds: List<Long>? = null, // ⛔ Optional: Collection IDs eligible (if targetSelection = "entitled")

    @SerializedName("prerequisite_customer_ids")
    val prerequisiteCustomerIds: List<Long>? = null, // ⛔ Optional: Customer IDs eligible for this discount

    var discountCodeMap: MutableMap<Long,String>
)

data class PrerequisiteSubtotalRange(
    @SerializedName("greater_than_or_equal_to")
    val greaterThanOrEqualTo: String // Min cart value (e.g., "50.0")
)


fun PriceRule.toFormState(): CouponFormState = CouponFormState(
    id = this.id,
    title = this.title,
    targetType = this.targetType,
    allocationMethod = this.allocationMethod,
    targetSelection = this.targetSelection,
    valueType = this.valueType,
    value = this.value,
    startsAt = this.startsAt,
    endsAt = this.endsAt ?: "",
    usageLimit = this.usageLimit.toString(),
    subtotalMin = this.prerequisiteSubtotalRange?.greaterThanOrEqualTo ?: "50.0",
    discountCodes = this.discountCodeMap,
    entitledProductIds = this.entitledProductIds?: listOf()
)
