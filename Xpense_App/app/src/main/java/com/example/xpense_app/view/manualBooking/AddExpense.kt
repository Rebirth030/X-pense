package com.example.xpense_app.view.manualBooking

import Expense
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.example.xpense_app.navigation.NavigationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@Composable
@ExperimentalMaterial3Api
fun AddExpense(navController: NavController, user: MutableState<User>) {


    //region variables
    var date by remember {
        mutableStateOf(Date())
    }
    var description by remember {
        mutableStateOf("")
    }
    val projects = remember {
        mutableListOf<Project>()
    }
    val selectedProject = remember {
        mutableStateOf(Project(null, "None", null, null, null, null))
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
            onSuccess = { projects.addAll(it) },
            onError = {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "Error loading projects",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
                value = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault())
                    .format(date),
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

            DropDown(projects = projects, selectedProject = selectedProject)
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
                        if (selectedProject.value.id != null) { //TODO: Implement weeklyTimecardId validation
                            if (startTime.value.hour < endTime.value.hour ||
                                (startTime.value.hour == endTime.value.hour && startTime.value.minute < endTime.value.minute)
                            ) {
                                if ((breakStartTime.value.hour > startTime.value.hour ||
                                            (breakStartTime.value.hour == startTime.value.hour && breakStartTime.value.minute >= startTime.value.minute)) &&
                                    (breakEndTime.value.hour < endTime.value.hour ||
                                            (breakEndTime.value.hour == endTime.value.hour && breakEndTime.value.minute <= endTime.value.minute))
                                ) {
                                    saveExpense(
                                        context,
                                        date,
                                        startTime.value,
                                        endTime.value,
                                        breakStartTime.value,
                                        breakEndTime.value,
                                        description,
                                        user.value,
                                        selectedProject.value
                                    )
                                    navController.navigate(NavigationItem.Overview.route)
                                } else {
                                    Toast.makeText(context, "Break time must be within work time and start must be before end", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Work start time must be before work end time", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Please select a project", Toast.LENGTH_SHORT).show()
                        }
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
                    onClick = { navController.navigate(NavigationItem.Overview.route) },
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
    date: Date,
    startTime: Time,
    endTime: Time,
    breakStartTime: Time,
    breakEndTime: Time,
    description: String,
    user: User,
    project: Project
) {
    if (breakStartTime.hour != breakEndTime.hour || breakStartTime.minute != breakEndTime.minute) {
        //Vor der Pause
        createExpense(context, date, startTime, breakStartTime, description, user, project)
        //Nach der Pause
        createExpense(context, date, breakEndTime, endTime, description, user, project)

    } else {
        createExpense(context, date, startTime, endTime, description, user, project)
    }
}

private fun createExpense(
    context: Context,
    date: Date,
    startTime: Time,
    endTime: Time,
    description: String,
    user: User,
    project: Project
) {

    val startDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        .withHour(startTime.hour)
        .withMinute(startTime.minute)
        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME)


    val endDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault())
        .withHour(endTime.hour)
        .withMinute(endTime.minute)
        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME)

    ExpenseService.createExpense(expense = Expense(
        id = null,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        state = null,
        userId = user.id,
        projectId = project.id,
        weeklyTimecardId = 1, //TODO: Implement weeklyTimecardId
        description = description
    ),
        token = user.token,
        onSuccess = {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Expense saved",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        onError = {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "Error saving expense",
                    Toast.LENGTH_SHORT
                ).show()
            }
            it.printStackTrace()
        })
}


@Composable
@ExperimentalMaterial3Api
fun BreakTimeField(
    startTime: Time,
    endTime: Time
) {
    val breakTime = "Break: " + convertTo12HourFormat(startTime) + " - " + convertTo12HourFormat(endTime)

    OutlinedTextField(
        value = breakTime,
        onValueChange = {},
        readOnly = true,
        modifier = Modifier.padding(16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(
    projects: List<Project>,
    selectedProject: MutableState<Project>
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier.align(Alignment.Center)
        ) {
            TextField(
                value = selectedProject.value.name!!,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                projects.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name!!) },
                        onClick = {
                            selectedProject.value = item
                            expanded = false
                        }
                    )
                }
            }
        }
    }
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

class Time(var hour: Int, var minute: Int, var is24hour: Boolean = false)
