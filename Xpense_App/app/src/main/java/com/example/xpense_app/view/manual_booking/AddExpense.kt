package com.example.xpense_app.view.manual_booking

import Expense
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.xpense_app.R
import com.example.xpense_app.controller.services.ExpenseService
import com.example.xpense_app.controller.services.ProjectService
import com.example.xpense_app.model.Project
import com.example.xpense_app.model.User
import com.example.xpense_app.navigation.NavigationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.text.DateFormat
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

/**
 * This Composable function displays the user interface for adding expenses.
 *
 * @param navController The NavController used for navigation between Composables.
 * @param user The currently logged in user.
 */
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
        mutableStateOf(Project(null, "None", null, null, null, null, null, null))
    }
    val showDatePicker = remember {
        mutableStateOf(false)
    }

    val showStartTimePicker = remember {
        mutableStateOf(false)
    }
    val showEndTimePicker = remember {
        mutableStateOf(false)
    }

    val startTime = remember {
        mutableStateOf(Time(12, 0))
    }
    val endTime = remember {
        mutableStateOf(Time(12, 0))
    }

    val showStartBreakPicker = remember {
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
    if (showStartBreakPicker.value) {
        TimeDialog(time = breakStartTime, onDismiss = {
            showStartBreakPicker.value = false
            showEndBreakePicker = true
        }, title = stringResource(R.string.time_dialog_start_break_title))
    }
    if (showEndBreakePicker) {
        TimeDialog(
            time = breakEndTime,
            onDismiss = { showEndBreakePicker = false },
            title = stringResource(R.string.time_dialog_end_break_title)
        )
    }

    if (showDatePicker.value) {
        DateDialog(onDateSelected = { date = it }, onDismiss = { showDatePicker.value = false })
    }
    if (showStartTimePicker.value) {
        TimeDialog(
            time = startTime, onDismiss = { showStartTimePicker.value = false }, title = stringResource(
                R.string.start_time
            )
        )
    }
    if (showEndTimePicker.value) {
        TimeDialog(
            time = endTime, onDismiss = { showEndTimePicker.value = false }, title = stringResource(
                R.string.end_time
            )
        )
    }
    //endregion

    GetProjects(user, projects, context)

    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            DatePickerTextField(date, showDatePicker)
            Spacer(modifier = Modifier.height(16.dp))
            TimePickerCardField(
                stringResource(R.string.start_time), showStartTimePicker, startTime.value
            )
            TimePickerCardField(stringResource(R.string.end_time), showEndTimePicker, endTime.value)
            Spacer(modifier = Modifier.height(16.dp))
            AddBreakTimeField(showStartBreakPicker, breakStartTime.value, breakEndTime.value)
            if (breakTimeExists(breakStartTime.value, breakEndTime.value)) {
                BreakTimeField(
                    startTime = breakStartTime.value, endTime = breakEndTime.value
                )
            }
            DropDown(projects = projects, selectedProject = selectedProject)
            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.description)) },
                modifier = Modifier.padding(16.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SaveButton(
                    selectedProject,
                    context,
                    startTime,
                    endTime,
                    breakStartTime,
                    breakEndTime,
                    date,
                    description,
                    user,
                    navController
                )
                Button(
                    onClick = { navController.navigate(NavigationItem.Overview.route) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    modifier = Modifier

                        .height(IntrinsicSize.Min)
                ) {
                    Text(
                        text = stringResource(R.string.cancel),
                    )
                }
            }
        }

    }
}

/**
 * This Composable function fetches the projects from the server.
 *
 * @param user The currently logged in user.
 * @param projects The list of projects to fill.
 * @param context The application context.
 */
@Composable
private fun GetProjects(
    user: MutableState<User>, projects: MutableList<Project>, context: Context
) {
    LaunchedEffect(key1 = Unit) {
        ProjectService.getProjects(token = user.value.token, onSuccess = { projects.addAll(it) }, onError = {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context, context.getString(R.string.error_message_error_loading_projects), Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}

/**
 * This Composable function displays the button for saving the expense.
 * It validates the input fields before saving the expense.
 *
 * @param selectedProject The currently selected project.
 * @param context The application context.
 * @param startTime The start time of the work.
 * @param endTime The end time of the work.
 * @param breakStartTime The start time of the break.
 * @param breakEndTime The end time of the break.
 * @param date The date of the expense.
 * @param description The description of the expense.
 * @param user The currently logged in user.
 * @param navController The NavController used for navigation between Composables.
 */
@Composable
private fun SaveButton(
    selectedProject: MutableState<Project>,
    context: Context,
    startTime: MutableState<Time>,
    endTime: MutableState<Time>,
    breakStartTime: MutableState<Time>,
    breakEndTime: MutableState<Time>,
    date: Date,
    description: String,
    user: MutableState<User>,
    navController: NavController
) {
    Button(
        onClick = {
            if (checkConditionsToCreateExpense(selectedProject, context, startTime, endTime, breakStartTime, breakEndTime)) {
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
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        Text(
            text = stringResource(R.string.save),
        )
    }
}

/**
 * This function checks the conditions for creating an expense.
 * It checks:
 * if a project is selected,
 * if the start time is before the end time
 * and if a break exists,
 * if the start time of the break is before the end time of the break,
 * as well as if the break time is within the work time.
 * If any of these conditions are not met, a Toast message is displayed.
 *
 * @param selectedProject The currently selected project.
 * @param context The application context.
 * @param startTime The start time of the work.
 * @param endTime The end time of the work.
 * @param breakStartTime The start time of the break.
 * @param breakEndTime The end time of the break.
 */
private fun checkConditionsToCreateExpense(
    selectedProject: MutableState<Project>,
    context: Context,
    startTime: MutableState<Time>,
    endTime: MutableState<Time>,
    breakStartTime: MutableState<Time>,
    breakEndTime: MutableState<Time>
) : Boolean {
    try {
        require(selectedProject.value.id != null) {
            context.getString(R.string.error_message_please_select_a_project)
        }
        require(firstTimeBeforeSecondTime(startTime.value, endTime.value)) {
            context.getString(R.string.error_message_work_start_time_must_be_before_work_end_time)
        }
        if (breakTimeExists(breakStartTime.value, breakEndTime.value)) {
            require(firstTimeBeforeSecondTime(breakStartTime.value, breakEndTime.value)) {
                context.getString(R.string.error_message_break_time_start_must_be_before_end)
            }
            require(firstTimeBeforeSecondTime(startTime.value, breakStartTime.value)) {
                context.getString(R.string.error_message_break_time_must_be_within_work_time)
            }
            require(firstTimeBeforeSecondTime(breakEndTime.value, endTime.value)) {
                context.getString(R.string.error_message_break_time_must_be_within_work_time)
            }
        }
    } catch (e: IllegalArgumentException) {
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        return false
    }
    return true
}

/**

 * This Composable function displays a button for adding a break time.
 *
 * @param showStartBreakPicker The state of the break time picker dialog.
 * @param breakStartTime The start time of the break.
 * @param breakEndTime The end time of the break.
 */
@Composable
private fun AddBreakTimeField(
    showStartBreakPicker: MutableState<Boolean>, breakStartTime: Time, breakEndTime: Time
) {
    AssistChip(onClick = { showStartBreakPicker.value = true }, label = {
        Text(
            text = if (breakTimeExists(
                    breakStartTime, breakEndTime
                )
            ) stringResource(R.string.edit_break_time) else stringResource(R.string.add_break_time), style = TextStyle(fontSize = 24.sp)
        )
    })
}

/**
 * This Composable function displays a card field for selecting a time.
 *
 * @param name The name of the field.
 * @param showTimePicker The state of the time picker dialog.
 * @param time The time to be displayed.
 */
@Composable
private fun TimePickerCardField(
    name: String, showTimePicker: MutableState<Boolean>, time: Time
) {
    Card(
        modifier = Modifier
            .padding(
                vertical = 4.dp, horizontal = 16.dp
            )
            .fillMaxWidth()
            .clickable { showTimePicker.value = true },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Text(
            text = "${name}: ${convertTo12HourFormat(time)}",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(16.dp)
        )
    }
}

/**
 * This Composable function displays a text field for selecting a date.
 *
 * @param date The date to be displayed.
 * @param showDatePicker The state of the date picker dialog.
 */
@Composable
fun DatePickerTextField(date: Date, showDatePicker: MutableState<Boolean>) {
    OutlinedTextField(value = DateFormat.getDateInstance(
        DateFormat.DEFAULT, Locale.getDefault()
    ).format(date), onValueChange = {}, label = { Text(stringResource(R.string.date)) }, readOnly = true, trailingIcon = {
        IconButton(onClick = { showDatePicker.value = true }) {
            Icon(Icons.Filled.DateRange, contentDescription = stringResource(R.string.select_date))
        }
    }, modifier = Modifier.padding(16.dp)
    )
}


/**
 * This function validates the time entries for an expense. It checks if the start time is before the end time
 * and if the break time (if any) is within the work time.
 *
 * @param context The application context.
 * @param date The date of the expense.
 * @param startTime The start time of the work.
 * @param endTime The end time of the work.
 * @param breakStartTime The start time of the break.
 * @param breakEndTime The end time of the break.
 * @param description The description of the expense.
 * @param user The user who is creating the expense.
 * @param project The project associated with the expense.
 */
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
    if (breakTimeExists(breakStartTime, breakEndTime)) {
        // Before Break
        createExpense(context, date, startTime, breakStartTime, description, user, project)
        // After Break
        createExpense(context, date, breakEndTime, endTime, description, user, project)
    } else {
        createExpense(context, date, startTime, endTime, description, user, project)
    }
}

/**
 * This function sends a request to the server to create an expense entry.
 *
 * @param context The application context.
 * @param date The date of the expense.
 * @param startTime The start time of the work.
 * @param endTime The end time of the work.
 * @param description The description of the expense.
 * @param user The user who is creating the expense.
 * @param project The project associated with the expense.
 */
private fun createExpense(
    context: Context, date: Date, startTime: Time, endTime: Time, description: String, user: User, project: Project
) {
    val startDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).withHour(startTime.hour)
        .withMinute(startTime.minute).format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    val endDateTime = ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).withHour(endTime.hour)
        .withMinute(endTime.minute).format(DateTimeFormatter.ISO_ZONED_DATE_TIME)

    ExpenseService.createExpense(expense = Expense(
        id = null,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        state = null,
        userId = user.id,
        projectId = project.id,
        description = description
    ), token = user.token, onSuccess = {
        withContext(Dispatchers.Main) {
            Toast.makeText(
                context, context.getString(R.string.expense_saved), Toast.LENGTH_SHORT
            ).show()
        }
    }, onError = {
        withContext(Dispatchers.Main) {
            Toast.makeText(
                context, context.getString(R.string.error_message_saving_expense), Toast.LENGTH_SHORT
            ).show()
        }
        it.printStackTrace()
    })
}

/**
 * This function checks if the first time is before the second time.
 * @param firstTime The first time.
 * @param secondTime The second time.
 */
fun firstTimeBeforeSecondTime(firstTime: Time, secondTime: Time): Boolean {
    return (LocalTime.of(firstTime.hour, firstTime.minute).isBefore(LocalTime.of(secondTime.hour, secondTime.minute)))
}

/**
 * This function checks if the start time of the break is not equal to the end time of the break.
 *
 * @param breakStartTime The start time of the break.
 * @param breakEndTime The end time of the break.
 * @return True if the start time is not equal to the end time, false otherwise.
 */
private fun breakTimeExists(breakStartTime: Time, breakEndTime: Time): Boolean {
    return !(LocalTime.of(breakStartTime.hour, breakStartTime.minute)
        .equals(LocalTime.of(breakEndTime.hour, breakEndTime.minute)))
}

/**
 * This Composable function displays a field for the break time.
 *
 * @param startTime The start time of the break.
 * @param endTime The end time of the break.
 */
@Composable
@ExperimentalMaterial3Api
fun BreakTimeField(
    startTime: Time, endTime: Time
) {
    val breakTime = stringResource(
        R.string.breaktime, convertTo12HourFormat(startTime), convertTo12HourFormat(endTime)
    )

    OutlinedTextField(
        value = breakTime, onValueChange = {}, readOnly = true, modifier = Modifier.padding(16.dp)
    )
}

/**
 * This Composable function displays a dropdown menu for selecting a project.
 *
 * @param projects The list of available projects.
 * @param selectedProject The currently selected project.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(
    projects: List<Project>, selectedProject: MutableState<Project>
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded, onExpandedChange = {
                expanded = !expanded
            }, modifier = Modifier.align(Alignment.Center)
        ) {
            TextField(
                value = selectedProject.value.name ?: stringResource(R.string.no_description_available),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                projects.forEach { item ->
                    DropdownMenuItem(text = {
                        Text(
                            text = item.name ?: stringResource(R.string.no_description_available)
                        )
                    }, onClick = {
                        selectedProject.value = item
                        expanded = false
                    })
                }
            }
        }
    }
}

/**
 * This function converts a Time object to a 12-hour format string.
 *
 * @param time The time to be converted.
 * @return The time in 12-hour format as a string.
 */
fun convertTo12HourFormat(time: Time): String {
    val localTime = LocalTime.of(time.hour, time.minute)
    val formatter = if (time.is24hour) {
        DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    } else {
        DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
    }
    return localTime.format(formatter)
}

data class Time(var hour: Int, var minute: Int, var is24hour: Boolean = false)



