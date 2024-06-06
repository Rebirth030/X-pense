package com.example.xpense_app.view.manual_booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.util.Date

/**
 * Date dialog to select a date
 *
 * @param onDateSelected the callback when a date is selected
 * @param onDismiss the callback when the dialog is dismissed
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateDialog(
    onDateSelected: (Date) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Date().time,
    )


    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                val selectedDate = datePickerState.selectedDateMillis?.let { Date(it) } ?: Date()
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = "Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

/**
 * Time dialog to select a time
 *
 * @param time the time to be selected
 * @param onDismiss the callback when the dialog is dismissed
 * @param title the title of the dialog
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeDialog(
    time: MutableState<Time>,
    onDismiss: () -> Unit,
    title: String
) {
    val timePickerState = rememberTimePickerState(
        initialHour = time.value.hour,
        initialMinute = time.value.minute,
        is24Hour = false
    )

    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(size = 12.dp)
            ),
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color.LightGray.copy(alpha = 0.3f)
                )
                .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)

            TimePicker(
                state = timePickerState
            )
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                // dismiss button
                TextButton(onClick = onDismiss) {
                    Text(text = "Dismiss")
                }

                // confirm button
                TextButton(
                    onClick = {
                        time.value.hour = timePickerState.hour
                        time.value.minute = timePickerState.minute
                        onDismiss()
                    }
                ) {
                    Text(text = "Confirm")
                }
            }
        }
    }
}