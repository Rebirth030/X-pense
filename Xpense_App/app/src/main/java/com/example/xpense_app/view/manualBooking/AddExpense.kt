package com.example.xpense_app.view.manualBooking

import Expense
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.xpense_app.controller.services.ExpenseService
import com.example.xpense_app.controller.services.ProjectService
import com.example.xpense_app.model.Project
import com.example.xpense_app.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.util.Date
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Composable
@ExperimentalMaterial3Api
fun AddExpense(navController: NavController, user: MutableState<User>) {

    //region variables
    var date by remember {
        mutableStateOf(getCurrentDate())
    }
    var description by remember{
        mutableStateOf("")
    }
    var selectedProject by remember {
        mutableStateOf<Project?>(null)
    }
    val projects = remember {
        mutableStateOf<List<Project>>(listOf())
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
    val context = LocalContext.current
    //endregion
    //region openDialogs
    if (showStartBreakPicker) {
        TimeDialog(time = breakStartTime, onDismiss = {
            showStartBreakPicker = false
            showEndBreakePicker = true
        }, title = "Break Start Time")
    }
    if (showEndBreakePicker) {
        TimeDialog(
            time = breakEndTime,
            onDismiss = { showEndBreakePicker = false },
            title = "Break End Time"
        )
    }

    if (showDatePicker) {
        DateDialog(onDateSelected = { date = it }, onDismiss = { showDatePicker = false })
    }
    if (showStartTimePicker) {
        TimeDialog(
            time = startTime,
            onDismiss = { showStartTimePicker = false },
            title = "Start Time"
        )
    }
    if (showEndTimePicker) {
        TimeDialog(time = endTime, onDismiss = { showEndTimePicker = false }, title = "End Time")
    }
    //endregion

    LaunchedEffect(key1 = Unit) {
        ProjectService.getProjects(
            token = user.value.token,
            onSuccess = { projects.value = it },
            onError = { withContext(Dispatchers.Main) {Toast.makeText(context, "Error loading projects", Toast.LENGTH_SHORT).show()}}
        )
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
                    .clickable { showStartTimePicker = true },
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                Text(
                    text = "Start Time: ${convertTo12HourFormat(startTime.value)}",
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
                    .clickable { showEndTimePicker = true },
                colors = CardDefaults.cardColors(containerColor = Color.LightGray)
            ) {
                Text(
                    text = "End Time: ${convertTo12HourFormat(endTime.value)}",
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
                    startTime = breakStartTime.value,
                    endTime = breakEndTime.value
                )
            }
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.padding(16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        saveExpense(
                            context,
                            date,
                            startTime.value,
                            endTime.value,
                            breakStartTime.value,
                            breakEndTime.value,
                            description,
                            user.value
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    modifier = Modifier
                        .height(IntrinsicSize.Min)
                ) {
                    Text(
                        text = "Save",
                    )
                }
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    modifier = Modifier

                        .height(IntrinsicSize.Min)
                ) {
                    Text(
                        text = "Cancel",
                    )
                }
            }
        }

    }
}

fun saveExpense(
    context: Context,
    date: String,
    startTime: Time,
    endTime: Time,
    breakStartTime: Time,
    breakEndTime: Time,
    description: String,
    user: User
) {
    if (isValidDate(date)) {
        if (breakStartTime.hour != breakEndTime.hour || breakStartTime.minute != breakEndTime.minute) {
            //Vor der Pause
            createExpense(context, date, startTime, breakStartTime, description, user)
            //Nach der Pause
            createExpense(context, date, breakEndTime, endTime ,description, user)

        } else {
            createExpense(context, date, startTime, endTime, description, user)
        }

    } else {
        Toast.makeText(
            context,
            "Invalid date format. Please enter a valid date in the format dd.mm.yyyy",
            Toast.LENGTH_SHORT
        ).show()
    }
}

private fun createExpense(
    context: Context,
    date: String,
    startTime: Time,
    endTime: Time,
    description: String,
    user: User
) {
    ExpenseService.createExpense(expense = Expense(
        id = null,
        startDateTime = LocalDateTime.parse(date).plusHours(startTime.hour.toLong())
            .plusMinutes(startTime.minute.toLong()).format(
                DateTimeFormatter.ISO_DATE_TIME
            ),
        endDateTime = LocalDateTime.parse(date).plusHours(endTime.hour.toLong())
            .plusMinutes(endTime.minute.toLong()).format(
                DateTimeFormatter.ISO_DATE_TIME
            ),
        state = null,
        userId = user.id,
        projectId = null,
        weeklyTimecardId = null,
        description = description
    ),
        token = user.token,
        onSuccess = { Toast.makeText(context, "Expense saved", Toast.LENGTH_SHORT).show() },
        onError = {
            Toast.makeText(context, "Error saving expense", Toast.LENGTH_SHORT).show()

        })
}


@Composable
@ExperimentalMaterial3Api
fun BreakTimeField(
    startTime: Time,
    endTime: Time
) {

    val breakTime = remember {
        mutableStateOf(
            "Break: " + convertTo12HourFormat(startTime) + " - " + convertTo12HourFormat(
                endTime
            )
        )
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


fun convertTo12HourFormat(time: Time): String {
    return if (time.is24hour) {
        String.format("%02d:%02d", time.hour, time.minute)
    } else {
        val hourIn12Format =
            if (time.hour > 12) time.hour - 12 else if (time.hour == 0) 12 else time.hour
        val period = if (time.hour >= 12) "PM" else "AM"
        String.format("%02d:%02d %s", hourIn12Format, time.minute, period)
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
