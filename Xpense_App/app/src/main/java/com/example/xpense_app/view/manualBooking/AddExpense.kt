package com.example.xpense_app.view.manualBooking

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.ParseException
import java.util.Date
import java.text.SimpleDateFormat


@Composable
@ExperimentalMaterial3Api
fun AddExpense() {


    var date by remember {
        mutableStateOf(getCurrentDate())
    }
    var showDatePicker by remember {
        mutableStateOf(false)
    }

    var showStartTimePicker by remember {
        mutableStateOf(false)
    }
    var showEndTimePicker by remember {
        mutableStateOf(false)
    }

    val startTime = remember {
        mutableStateOf(Time(12, 0))
    }
    val endTime = remember {
        mutableStateOf(Time(12, 0))
    }

    var showStartBreakPicker by remember {
        mutableStateOf(false)
    }
    var showEndBreakePicker by remember {
        mutableStateOf(false)
    }

    val breakStartTime = remember {
        mutableStateOf(Time(12, 0))
    }
    val breakEndTime = remember {
        mutableStateOf(Time(12, 0))
    }

    if (showStartBreakPicker) {
        TimeDialog(time = breakStartTime, onDismiss = { showStartBreakPicker = false
            showEndBreakePicker = true }, title = "Break Start Time")
    }
    if (showEndBreakePicker) {
        TimeDialog(time = breakEndTime, onDismiss = { showEndBreakePicker = false }, title = "Break End Time")
    }

    if (showDatePicker) {
        DateDialog(onDateSelected = { date = it }, onDismiss = { showDatePicker = false })
    }
    if (showStartTimePicker) {
        TimeDialog(time = startTime, onDismiss = { showStartTimePicker = false }, title = "Start Time")
    }
    if (showEndTimePicker) {
        TimeDialog(time = endTime, onDismiss = { showEndTimePicker = false }, title = "End Time")
    }


    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            OutlinedTextField(
                value = date,
                onValueChange = {},
                label = { Text("Date") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Select date")
                    }
                },
                modifier = Modifier.padding(16.dp)
            )
            Card(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth()
                    .clickable { showStartTimePicker = true }
            ) {
                Text(
                    text = "Start Time: ${convertTo12HourFormat(startTime)}",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(16.dp)
                )
            }

            Card(
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ) // Reduzierter Abstand nach oben
                    .fillMaxWidth()
                    .clickable { showEndTimePicker = true }
            ) {
                Text(
                    text = "End Time: ${convertTo12HourFormat(endTime)}",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(16.dp)
                )
            }

            AssistChip(onClick = { showStartBreakPicker = true }, label = {
                Text(
                    text = if (breakStartTime.value.hour != breakEndTime.value.hour || breakStartTime.value.minute != breakEndTime.value.minute) "Edit break time" else "Add break time",
                    style = TextStyle(fontSize = 24.sp)
                )
            })
            if (breakStartTime.value.hour != breakEndTime.value.hour || breakStartTime.value.minute != breakEndTime.value.minute) {
                BreakTimeField(
                    startTime = breakStartTime,
                    endTime = breakEndTime
                )
            }
            Button(onClick = { saveExpense(date, startTime.value, endTime.value, breakStartTime.value, breakEndTime.value) }) {
                Text(text = "Save")
            }

        }

    }
}

fun saveExpense(date: String, startTime: Time, endTime: Time, breakStartTime: Time, breakEndTime: Time) {
    if (isValidDate(date)) {
        // Führen Sie hier den Code aus, der ausgeführt werden soll, wenn das Datum gültig ist
    } else {

    }
}




@Composable
@ExperimentalMaterial3Api
fun BreakTimeField(
    startTime: MutableState<Time>,
    endTime: MutableState<Time>
) {

    val breakTime = remember { mutableStateOf("Break: " + convertTo12HourFormat(startTime) + " - " + convertTo12HourFormat(endTime))
    }
    OutlinedTextField(
        value = breakTime.value,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.padding(16.dp)
    )
}




fun getCurrentDate(): String {
    val formatter = SimpleDateFormat.getDateInstance()
    return formatter.format(Date())
}



fun convertTo12HourFormat(time: MutableState<Time>): String {
    return if (time.value.is24hour) {
        String.format("%02d:%02d", time.value.hour, time.value.minute)
    } else {
        val hourIn12Format =
            if (time.value.hour > 12) time.value.hour - 12 else if (time.value.hour == 0) 12 else time.value.hour
        val period = if (time.value.hour >= 12) "PM" else "AM"
        String.format("%02d:%02d %s", hourIn12Format, time.value.minute, period)
    }
}

fun isValidDate(date: String): Boolean {
    return try {
        val formatter = SimpleDateFormat.getDateInstance()
        formatter.isLenient = false
        formatter.parse(date)
        true
    } catch (e: ParseException) {
        false
    }
}

class Time(var hour: Int, var minute: Int, var is24hour: Boolean = false)
