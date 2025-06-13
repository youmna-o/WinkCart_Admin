package com.example.winkcart_admin.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.winkcart_admin.utils.dateToIso8601
import java.time.LocalDateTime

data class CouponFormState(
    val id: Long = 0,
    val title: String = "",
    val targetType: String = "line_item",
    val allocationMethod: String = "across",
    val targetSelection: String ="all",
    val valueType: String = "percentage",
    val value: String = "",
    val startsAt: String = "",
    val endsAt: String = "",
    val usageLimit: String = "10",
    val subtotalMin: String = "50.0",
    val discountCodes: Map<Long, String> = emptyMap(),
    val newDiscountCodes:List<String> = listOf(),
    val entitledProductIds: List<Long> = listOf()
)
@RequiresApi(Build.VERSION_CODES.O)
fun CouponFormState.toPriceRule():PriceRule= PriceRule(
    id = this.id,
    title = this.title.ifBlank { "Discount" },
    targetType = this.targetType,
    targetSelection =this.targetSelection ,
    allocationMethod = this.allocationMethod,
    valueType = this.valueType,
    value = this.value.ifBlank { "-10.0" },
    customerSelection = "all",
    startsAt =this.startsAt.ifBlank {
        val now = LocalDateTime.now()
        dateToIso8601(now.year, now.monthValue, now.dayOfMonth, now.hour, now.minute)
    },
    endsAt = this.endsAt.ifBlank {
        val now = LocalDateTime.now().plusMonths(1)
        dateToIso8601(now.year, now.monthValue, now.dayOfMonth, now.hour, now.minute)
    },
    usageLimit = this.usageLimit.toIntOrNull()?:10,
    oncePerCustomer = true,
    prerequisiteSubtotalRange = PrerequisiteSubtotalRange(this.subtotalMin.ifBlank { "50.0" }),
    discountCodeMap = this.discountCodes.toMutableMap(),
    entitledProductIds = if(this.targetSelection=="entitled") this.entitledProductIds else listOf()
)