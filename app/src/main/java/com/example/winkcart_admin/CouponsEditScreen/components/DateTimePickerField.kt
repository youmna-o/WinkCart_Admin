package com.example.winkcart_admin.CouponsEditScreen.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.winkcart_admin.utils.dateToIso8601
import java.util.Calendar

@Composable
fun DateTimePickerField(
    label: String,
    isoDateTime: String,
    onDateTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current

    var displayValue by remember { mutableStateOf(isoDateTime.take(16).replace('T', ' ')) }

    val calendar = Calendar.getInstance()

    OutlinedTextField(
        value = displayValue,
        onValueChange = {},
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val dateListener =
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val timeListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                            val iso = dateToIso8601(year, month + 1, dayOfMonth, hour, minute)
                            displayValue = iso.take(16).replace('T', ' ')
                            onDateTimeSelected(iso)
                        }

                        TimePickerDialog(
                            context, timeListener,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    }

                DatePickerDialog(
                    context,
                    dateListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
        label = { Text(label) },
        enabled = false,
        trailingIcon = {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
        }
    )
}