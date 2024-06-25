package com.example.xpense_app.view.timer.ui

import Expense
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xpense_app.R
import com.example.xpense_app.model.Project
import com.example.xpense_app.view.timer.view_model.TimerViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

/**
 * Composable function to display the current date.
 *
 * This function retrieves the current date using [LocalDateTime.now()] and formats it into a string using the
 * specified date pattern ("yyyy-MM-dd"). The formatted date is then displayed using the [DisplayDateTime] composable.
 */
@Composable
fun CurrentDate() {
    val formatter = DateTimeFormatter.ofPattern(stringResource(R.string.date_time_formatter_date_only))
    val currentDate = LocalDateTime.now().format(formatter)
    DisplayDateTime(text = currentDate)
}

/**
 * Composable function to display the current time.
 *
 * This function retrieves the current time using [LocalDateTime.now()] and formats it into a string using the
 * specified time pattern ("HH:mm:ss"). The formatted time is then displayed using the [DisplayDateTime] composable.
 * It also updates the displayed time every second using a LaunchedEffect and a delay of 1000 milliseconds.
 */
@Composable
fun CurrentTime() {
    val formatter = DateTimeFormatter.ofPattern(stringResource(R.string.date_time_formatter_hours_minutes_seconds))
    var currentTime by remember {
        mutableStateOf(LocalDateTime.now().format(formatter))
    }
    DisplayDateTime(text = currentTime)
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = LocalDateTime.now().format(formatter)
        }
    }
}

/**
 * Composable function to display a list of projects.
 *
 * This function takes a list of [projects] and a [timerViewModel] as parameters and displays the projects in a LazyVerticalGrid.
 * It also provides functionality to reorder projects and displays an error message if reordering fails.
 *
 * @param projects The list of projects to display.
 * @param timerViewModel The TimerViewModel used for reordering projects and handling error messages.
 */
@Composable
@ExperimentalMaterial3Api
fun ProjectList(projects: List<Project>, timerViewModel: TimerViewModel) {
    var projectList by remember { mutableStateOf(projects) }
    projectList = timerViewModel.reorderProjects()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, bottom = 15.dp, top = 15.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.projects),
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
    ) {
        LazyVerticalGrid(
            contentPadding = PaddingValues(20.dp),
            columns = GridCells.Fixed(1),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = true,
            modifier = Modifier
                .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                .heightIn(max = 300.dp)
        ) {
            itemsIndexed(projectList, key = { _, projectKey -> projectKey.id!! })
            { _, project ->
                ProjectItem(
                    project = project,
                    timerViewModel = timerViewModel,
                    onReorderProject = {
                        if (it) {
                            projectList = timerViewModel.reorderProjects()
                        }
                    }
                )
            }
        }
    }
}

/**
 * Composable function to display a single project item.
 *
 * This function takes a [project], [timerViewModel], and [onReorderProject] callback as parameters and displays the details
 * of the project in a clickable row. It also handles the dialog for changing project details and updating project timers.
 *
 * @param project The project to display.
 * @param timerViewModel The TimerViewModel used for managing project timers and error messages.
 * @param onReorderProject Callback function invoked when the project is reordered.
 */
@ExperimentalMaterial3Api
@Composable
fun ProjectItem(
    project: Project,
    timerViewModel: TimerViewModel,
    onReorderProject: (Boolean) -> Unit
) {
    val context = LocalContext.current
    var showProjectChangeDialog by remember { mutableStateOf(false) }
    val projectTimers by timerViewModel.projectTimers.collectAsState()
    val isProjectOnRun by timerViewModel.projectTimersOnRun.collectAsState()
    val expenses by timerViewModel.expenses.collectAsState()

    val projectIdValue = requireNotNull(project.id) {
        Toast.makeText(context, stringResource(R.string.project_id_must_not_be_null), Toast.LENGTH_SHORT).show()
        return
    }
    val isProjectOnRunValue = isProjectOnRun[projectIdValue] ?: false
    val projectTimerValue = requireNotNull(projectTimers[projectIdValue]) {
        Toast.makeText(context, stringResource(R.string.project_timer_must_not_be_null), Toast.LENGTH_SHORT).show()
        return
    }
    val expenseIsPaused = expenses.find {expense -> expense.projectId == projectIdValue && expense.state == "PAUSED" && expense.endDateTime == null}
    val borderColor =
        if(isProjectOnRunValue) {
            Color(0xFF87CC92)
        } else if(expenseIsPaused != null) {
            Color(0xFFFAC384)
        } else {
            Color.Transparent
        }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = {
                showProjectChangeDialog = true
                /* Change project without dialog
                timerViewModel.changeProject(project);
                onReorderProject(true)
                 */
            })
            .border(1.dp, borderColor, shape = RoundedCornerShape(24.dp))
            .background(borderColor, shape = RoundedCornerShape(24.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            ProjectLabel(project = project, isProjectOnRunValue, projectTimerValue, expenseIsPaused)
            ProjectName(project = project)
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier.padding(10.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Text(
                    text = formatTimeProject(timeMi = projectTimerValue),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }
    }
    LaunchedEffect(isProjectOnRunValue) {
        if (isProjectOnRunValue) {
            while (isProjectOnRunValue) {
                delay(1000)
                timerViewModel.setProjectTime(project)
            }
        }
    }
    if (showProjectChangeDialog) {
        ProjectChangeDialog(
            project = project,
            timerViewModel = timerViewModel,
            onDismiss = {
                showProjectChangeDialog = false
                onReorderProject(it)
            }
        )
    }
}

/**
 * Composable function to display the label for a project.
 *
 * This function takes a [project] and a boolean value indicating whether the project is currently running ([projectIsOnRun]).
 * It displays a colored circular label representing the project, with the first letter of the project name inside.
 * The color of the label is determined based on whether the project is currently running.
 *
 * @param project The project for which to display the label.
 * @param projectIsOnRun Boolean value indicating whether the project is currently running.
 */
@Composable
fun ProjectLabel(project: Project, projectIsOnRun: Boolean, projectTimerValue: Long, expense: Expense?) {
    val context = LocalContext.current
    val color = if (projectIsOnRun) {
        MaterialTheme.colorScheme.tertiary
    } else {
        MaterialTheme.colorScheme.tertiaryContainer
    }
    val projectNameValue = requireNotNull(project.name) {
        Toast.makeText(context, stringResource(R.string.project_name_must_not_be_null), Toast.LENGTH_SHORT).show()
        return
    }
    require(projectNameValue.isNotBlank()) {
        Toast.makeText(context, stringResource(R.string.project_name_is_blank), Toast.LENGTH_SHORT).show()
        return
    }
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color = MaterialTheme.colorScheme.tertiaryContainer, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
         if (projectIsOnRun) {
            Icon(
                modifier = Modifier.size(24.dp, 24.dp),
                painter = painterResource(R.drawable.play_solid),
                contentDescription = "playLabel",
                tint = Color(0xFF35C44d)
            )
        } else if (expense != null) {
            Icon(
                painter = painterResource(R.drawable.pause_solid),
                contentDescription = "pauseLabel",
                tint = Color(0xFFE69335)
            )
        } else {
            Text(
                text = projectNameValue.substring(0, 1),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        }
    }
}

/**
 * Composable function to display the name of a project.
 *
 * This function takes a [project] as a parameter and displays its name in a Box with specified styling.
 *
 * @param project The project for which to display the name.
 */
@Composable
fun ProjectName(project: Project) {
    val context = LocalContext.current
    val projectName = requireNotNull(project.name) {
        Toast.makeText(
            context,
            stringResource(R.string.project_name_must_not_be_null), Toast.LENGTH_SHORT
        ).show()
        return
    }
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 10.dp)
            .widthIn(max = 175.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = projectName,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Composable function to display a dialog for changing the current project.
 *
 * This function takes a [project], [timerViewModel], and [onDismiss] callback as parameters and displays a dialog
 * with options to confirm or cancel the project change. It handles the project change when confirmed and displays
 * an error message if the change fails.
 *
 * @param project The project for which to display the change dialog.
 * @param timerViewModel The TimerViewModel used for changing the project and handling error messages.
 * @param onDismiss Callback function invoked when the dialog is dismissed, with a boolean indicating whether the project change was confirmed.
 */
@Composable
fun ProjectChangeDialog(
    project: Project,
    timerViewModel: TimerViewModel,
    onDismiss: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val projectName = requireNotNull(project.name) {
        Toast.makeText(context, R.string.project_name_must_not_be_null, Toast.LENGTH_SHORT).show()
        return
    }
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        title = { Text(text = projectName) },
        text = { Text(text = stringResource(R.string.dialog_message_change_projects)) },
        confirmButton = {
            Button(
                onClick = {
                    timerViewModel.changeProject(project)
                    onDismiss(true)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text(
                    text = stringResource(R.string.yes),
                    color = Color.White
                )
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss(false)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = Color.White
                )
            }
        }
    )
}

/**
 * Composable function to display timer control buttons.
 *
 * This function takes a [timerViewModel] as a parameter and displays buttons for controlling the timer,
 * including start/pause and stop buttons. It also updates the displayed time based on the timer's state.
 *
 * @param timerViewModel The TimerViewModel used for managing timer state and handling timer actions.
 */
@Composable
fun TimerButtons(timerViewModel: TimerViewModel) {
    val context = LocalContext.current
    val currentProject by timerViewModel.currentProject.collectAsState()
    val isProjectOnRun by timerViewModel.projectTimersOnRun.collectAsState()
    var time by remember {
        mutableLongStateOf(timerViewModel.getOverallTime())
    }
    val projectIdValue = requireNotNull(currentProject) {
        Toast.makeText(context, stringResource(R.string.current_project_must_not_be_null), Toast.LENGTH_SHORT).show()
        return
    }
    val isProjectOnRunValue = requireNotNull(isProjectOnRun[projectIdValue.id]) {
        Toast.makeText(context, stringResource(R.string.project_id_must_not_be_null), Toast.LENGTH_SHORT).show()
        return
    }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.padding(10.dp),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Text(
                text = formatTime(timeMi = time),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        }
        Row {
            IconButton(
                onClick = {
                    if (isProjectOnRunValue) {
                        timerViewModel.toggleProjectTimer(false)
                    } else {
                        timerViewModel.setProjectStartTime(null)
                        timerViewModel.toggleProjectTimer(true)
                    }
                }
            ) {
                val icon = if (isProjectOnRunValue) {
                    R.drawable.pause_solid
                } else {
                    R.drawable.play_solid
                }
                Icon(painterResource(id = icon), contentDescription = stringResource(R.string.play_pause_timer))
            }
            Spacer(modifier = Modifier.height(16.dp))
            IconButton(onClick = {
                timerViewModel.stopAllProjectTimers()
                time = 0L
            }) {
                Icon(painterResource(id = R.drawable.stop_solid), contentDescription = stringResource(R.string.stop_timer))
            }
        }
    }
    LaunchedEffect(isProjectOnRunValue) {
        if (isProjectOnRunValue) {
            while (isProjectOnRunValue) {
                delay(1000)
                time = timerViewModel.getOverallTime()
            }
        }
    }
}


/**
 * Composable function to display a date or time string.
 *
 * This function takes a [text] parameter representing the date or time string to be displayed
 * and arranges it within a bordered box with rounded corners.
 *
 * @param text The date or time string to be displayed.
 */
@Composable
fun DisplayDateTime(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
                .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = text)
            }
        }
    }
}

/**
 * Function to format a duration in milliseconds into the format "Hours:Minutes".
 *
 * This function accepts a duration in milliseconds and converts it into hours and minutes,
 * returning the result in the format "Hours:Minutes".
 *
 * @param timeMi The duration in milliseconds.
 * @return The formatted duration in the format "Hours:Minutes".
 */

@Composable
fun formatTime(timeMi: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(timeMi)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMi) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMi) % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

/**
 * Function to format a duration in milliseconds into the format "Hours:Minutes".
 *
 * This function accepts a duration in milliseconds and converts it into hours and minutes,
 * returning the result in the format "Hours:Minutes".
 *
 * @param timeMi The duration in milliseconds.
 * @return The formatted duration in the format "Hours:Minutes".
 */

@Composable
fun formatTimeProject(timeMi: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(timeMi)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMi) % 60

    return String.format("%02d:%02d", hours, minutes)
}
