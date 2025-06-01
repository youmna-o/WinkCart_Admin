package com.example.winkcart_admin.productsScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.winkcart_admin.productsScreen.SearchFilter

@Composable
fun ProductsSearchBar(onQueryChanged: (String) -> Unit,onFilterTypeChanged: (SearchFilter) -> Unit) {
    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var chosenFilter by remember { mutableStateOf("Search By Name") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { newQuery ->
                searchText = newQuery
                onQueryChanged.invoke(newQuery)
            },
            modifier = Modifier.weight(9f),
            label = { Text(text = chosenFilter) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (searchText.isNotBlank()) {
                    IconButton(
                        onClick = {
                            searchText = ""
                            onQueryChanged.invoke("")
                        },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    )
                }
            },
            keyboardOptions = when(chosenFilter){
                "Search By Name" -> KeyboardOptions(keyboardType = KeyboardType.Text)
                "Search By ID" ->KeyboardOptions(keyboardType = KeyboardType.Number)
                else -> KeyboardOptions(keyboardType = KeyboardType.Text)
            },
            singleLine = true,
        )
        //Spacer(Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1f)) {
            IconButton(
                onClick = { expanded = true },
                content = {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "filter"
                    )
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                SearchFilter.entries.forEach {
                    DropdownMenuItem(
                        text = {
                            when (it) {
                                SearchFilter.BY_NAME -> Text(text = "Search By Name")
                                SearchFilter.BY_ID -> Text(text = "Search By ID")
                            }
                        },
                        onClick = {
                            expanded = false
                            onFilterTypeChanged(it)
                            searchText = ""
                            chosenFilter = when (it) {
                                SearchFilter.BY_ID -> "Search By ID"
                                SearchFilter.BY_NAME -> "Search By Name"
                            }
                        }
                    )
                }
            }
        }
    }

}

