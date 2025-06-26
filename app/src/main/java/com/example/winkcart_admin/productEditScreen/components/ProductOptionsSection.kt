package com.example.winkcart_admin.productEditScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProductOptionsSection(
    productOptionsMap: SnapshotStateMap<String, SnapshotStateList<String>>,
    onAddOptionAction: (String) -> Unit,
    onDeleteOptionAction:(String)->Unit,
    isOptionFieldEnabled: () -> Boolean,
    onAddValueToOptionAction:(String, String)->Unit,
    onDeleteValueToOptionAction:(String, String)->Unit
) {
    var productOption by remember { mutableStateOf("") }
    var isOptionExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(productOptionsMap.keys.firstOrNull() ?:"") }
    var optionValue by remember { mutableStateOf("") }
    OutlinedTextField(
        value = productOption,
        onValueChange = {
            productOption = it
        },
        label = { Text("Option Name") },
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        trailingIcon = {
            IconButton(
                content = {
                    Icon(Icons.Default.Add, contentDescription = "Adding Icon")
                },
                onClick = {
                    if (productOption.isNotBlank()) {
                        onAddOptionAction(productOption)
                        productOption = ""
                    }
                }
            )
        },
        singleLine = true,
        enabled = isOptionFieldEnabled()
    )
    DeletableStringCards(
        productOptionsMap.keys.toList(),
        onDeleteOptionAction = { onDeleteOptionAction(it) },
    )
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedTextField(
            value = optionValue,
            onValueChange = {
                optionValue = it
            },
            label = { Text("Option value") },
            modifier = Modifier
                .padding(end = 8.dp),
            trailingIcon = {
                IconButton(
                    content = {
                        Icon(Icons.Default.Add, contentDescription = "Adding Icon")
                    },
                    onClick = {
                        if (selectedOption.isNotBlank() and optionValue.isNotBlank()) {
                            onAddValueToOptionAction(optionValue, selectedOption)
                            optionValue = ""
                        }
                    }
                )
            },
            singleLine = true,
            enabled = selectedOption.isNotBlank()
        )
        Box {
            Row (verticalAlignment = Alignment.CenterVertically){
                Text(text = selectedOption)
                IconButton(
                    onClick = { isOptionExpanded = true  },
                    content = {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "filter"
                        )
                    }
                )
            }

            DropdownMenu(
                expanded = isOptionExpanded,
                onDismissRequest = { isOptionExpanded = false }
            ) {
                productOptionsMap.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.key) },
                        onClick = {
                            selectedOption = it.key
                            isOptionExpanded = false
                        }
                    )
                }
            }
        }
    }
    DeletableStringCards(
        stringList = productOptionsMap[selectedOption]?.toList() ?: listOf() ,
        onDeleteOptionAction = {toBeDeletedValue-> onDeleteValueToOptionAction(toBeDeletedValue,selectedOption) }
    )
}

