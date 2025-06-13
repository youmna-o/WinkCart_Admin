package com.example.winkcart_admin.CouponsEditScreen.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.winkcart_admin.model.CouponFormState

@Composable
fun CouponEditForm(
    formState: CouponFormState,
    isNew: Boolean,
    onFieldChange: (CouponFormState) -> Unit,
    onFinishUpdate: () -> Unit,
    onProductIdChange: (Long)->Boolean,
    onDeleteCode:(Long)->Unit
) {
    var newCode by remember { mutableStateOf("") }
    var newProductId by remember { mutableStateOf("") }
    var isProductIdValid by remember { mutableStateOf(true) }
    var context =LocalContext.current

    val allocationMethod = formState.allocationMethod
    val targetSelection = when (allocationMethod) {
        "each" -> "entitled"
        else -> formState.targetSelection
    }

    LaunchedEffect(allocationMethod) {
        if (allocationMethod == "each" && formState.targetSelection != "entitled") {
            onFieldChange(formState.copy(targetSelection = "entitled"))
        }
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // ID
        OutlinedTextField(
            value = if (isNew) "ID will be generated automatically" else formState.id.toString(),
            onValueChange = {},
            enabled = false,
            label = { Text("ID") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Title
        OutlinedTextField(
            value = formState.title,
            onValueChange = { onFieldChange(formState.copy(title = it)) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Target Type
        DropdownSelector(
            label = "Target Type",
            options = listOf("line_item", "shipping_line"),
            selected = formState.targetType,
            onSelect = { onFieldChange(formState.copy(targetType = it)) }
        )

        Spacer(Modifier.height(8.dp))

        // Allocation Method
        DropdownSelector(
            label = "Allocation Method",
            options = listOf("across", "each"),
            selected = formState.allocationMethod,
            onSelect = { onFieldChange(formState.copy(allocationMethod = it)) }
        )

        Spacer(Modifier.height(8.dp))
        // Target Selection
        if (allocationMethod == "each") {
            OutlinedTextField(
                value = "entitled",
                onValueChange = {},
                enabled = false,
                label = { Text("Target Selection (auto-set)") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            DropdownSelector(
                label = "Target Selection",
                options = listOf("all", "entitled"),
                selected = targetSelection,
                onSelect = { onFieldChange(formState.copy(targetSelection = it)) }
            )
        }

        Spacer(Modifier.height(8.dp))

        if (targetSelection == "entitled") {
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween)
            {
                Text("Entitled Product IDs", style = MaterialTheme.typography.titleMedium)
                if (formState.entitledProductIds.isEmpty()){
                    Text(text = "Can't Be empty", color = Color.Red)
                }
            }
            Spacer(Modifier.height(8.dp))

            formState.entitledProductIds.forEach { id ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                ) {
                    Text(text = id.toString(), modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        val updatedList = formState.entitledProductIds.toMutableList().apply {
                            remove(id)
                        }
                        onFieldChange(formState.copy(entitledProductIds = updatedList))
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Remove Product ID")
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = newProductId,
                    onValueChange = {
                        newProductId = it
                        isProductIdValid = onProductIdChange(it.toLongOrNull() ?: 0L)
                    },
                    label = { Text("New Product ID") },
                    modifier = Modifier.weight(1f),
                    trailingIcon = {
                        if (!isProductIdValid) Icon(
                            Icons.Default.Warning,
                            contentDescription = "warning",
                            tint = Color.Red
                        )
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (newProductId.isNotBlank()) {
                        val updated = formState.entitledProductIds.toMutableList().apply {
                            newProductId.trim().toLongOrNull()?.let { add(it) }
                        }
                        onFieldChange(formState.copy(entitledProductIds = updated))
                        newProductId = ""
                    }
                }, enabled = isProductIdValid) {
                    Text("Add")
                }
            }

            Spacer(Modifier.height(8.dp))
        }


        // Value Type
        DropdownSelector(
            label = "Value Type",
            options = listOf("percentage", "fixed_amount", /*"shipping"*/),
            selected = formState.valueType,
            onSelect = { onFieldChange(formState.copy(valueType = it)) }
        )

        Spacer(Modifier.height(8.dp))

        // Value
        OutlinedTextField(
            value = formState.value,
            onValueChange = { onFieldChange(formState.copy(value = it)) },
            label = { Text("Discount Value (Default -10.0)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))



        DateTimePickerField(
            label = "Start Date & Time(Default: Now)",
            isoDateTime = formState.startsAt,
            onDateTimeSelected = { iso ->
                onFieldChange(formState.copy(startsAt = iso))
            }
        )
        Spacer(Modifier.height(8.dp))


        DateTimePickerField(
            label = "End Date & Time(Default:1 M later)",
            isoDateTime = formState.endsAt,
            onDateTimeSelected = { iso ->
                onFieldChange(formState.copy(endsAt = iso))
            }
        )

        Spacer(Modifier.height(8.dp))

        // Usage Limit
        OutlinedTextField(
            value = formState.usageLimit,
            onValueChange = { onFieldChange(formState.copy(usageLimit = it)) },
            label = { Text("Usage Limit (default 10)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Minimum Subtotal
        OutlinedTextField(
            value = formState.subtotalMin,
            onValueChange = {
                //val valid = it.toFloatOrNull()?.let { value -> value >= 50 } ?: false
                onFieldChange(formState.copy(subtotalMin = it))

            },
            label = { Text("Minimum Subtotal (≥ 50)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Discount Codes
        Text("Discount Codes", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        (formState.discountCodes.values.toList() + formState.newDiscountCodes).forEach { code ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = code,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(onClick = {
                    if (formState.newDiscountCodes.contains(code)) {
                        val updatedNewDiscountCodes =
                            formState.newDiscountCodes.toMutableList().apply {
                                remove(code)
                            }.toList()
                        onFieldChange(formState.copy(newDiscountCodes = updatedNewDiscountCodes))
                    } else {
                        val keyToRemove =
                            formState.discountCodes.entries.firstOrNull { it.value == code }?.key
                        if (keyToRemove != null) {
                            onDeleteCode(keyToRemove)
                            val updatedDiscountCodes =
                                formState.discountCodes.toMutableMap().apply {
                                    remove(keyToRemove)
                                }
                            onFieldChange(formState.copy(discountCodes = updatedDiscountCodes))
                        }
                    }
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove Code")
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newCode,
                onValueChange = { newCode = it },
                label = { Text("New Discount Code") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newCode.isNotBlank()) {
                        val updatedNewDiscountCodes =
                            formState.newDiscountCodes.toMutableList().apply {
                                add(newCode.trim())
                            }.toList()

                        onFieldChange(formState.copy(newDiscountCodes = updatedNewDiscountCodes))
                        newCode = ""
                    }
                }
            ) {
                Text("Add")
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (formState.entitledProductIds.isEmpty()){
                    Toast.makeText(context,"product ids cant be empty while target selection entitled ",Toast.LENGTH_SHORT).show()
                }
                else{
                    onFinishUpdate()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isNew) "Create Coupon" else "Update Coupon")
        }
    }
}