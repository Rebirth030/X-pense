package com.example.xpense_app.view.timer.ui

import android.util.Log
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun CurrentDate() {
    val formatter = DateTimeFormatter.ofPattern(stringResource(R.string.date_time_formatter_date_only))
    val currentDate = LocalDateTime.now().format(formatter)
    DisplayDateTime(text = currentDate)
}

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

@Composable
@ExperimentalMaterial3Api
fun ProjectList(projects: List<Project>, timerViewModel: TimerViewModel) {
    var projectList by remember { mutableStateOf(projects) }
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

@ExperimentalMaterial3Api
@Composable
fun ProjectItem(
    project: Project,
    timerViewModel: TimerViewModel,
    onReorderProject: (Boolean) -> Unit
) {
    var showProjectChangeDialog by remember { mutableStateOf(false) }
    val projectTimers by timerViewModel.projectTimers.collectAsState()
    val isProjectOnRun by timerViewModel.projectTimersOnRun.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = { showProjectChangeDialog = true }),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            ProjectLabel(project = project, isProjectOnRun[project.id!!]!!)
            ProjectName(project = project)
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier.padding(10.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Text(
                    text = formatTimeProject(timeMi = projectTimers[project.id]!!),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }
    }
    Log.d("TEST", "I am here")
    LaunchedEffect(isProjectOnRun[project.id!!]) {
        if (isProjectOnRun[project.id]!!) {
            while (isProjectOnRun[project.id]!!) {
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

@Composable
fun ProjectLabel(project: Project, projectIsOnRun: Boolean) {
    val color = if (projectIsOnRun) {
        Color.Green
    } else {
        Color.Blue
    }
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color = color, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = project.name!!.substring(0, 2),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Composable
fun ProjectName(project: Project) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .padding(start = 10.dp)
            .widthIn(max = 175.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = project.name!!,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ProjectChangeDialog(
    project: Project,
    timerViewModel: TimerViewModel,
    onDismiss: (Boolean) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss(false) },
        title = { Text(text = project.name!!) },
        text = { Text(text = stringResource(R.string.dialog_message_change_projects)) },
        confirmButton = {
            Button(
                onClick = {
                    timerViewModel.changeProject(project)
                    onDismiss(true)
                }
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
                    // change current project
                    onDismiss(false)
                }
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = Color.White
                )
            }
        }
    )
}


@Composable
fun TimerButtons(timerViewModel: TimerViewModel) {
    val currentProject by timerViewModel.currentProject.collectAsState()
    val isProjectOnRun by timerViewModel.projectTimersOnRun.collectAsState()
    val projectStartTimes by timerViewModel.projectTimersStartTime.collectAsState()
    var time by remember {
        mutableLongStateOf(0L)
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
                    if (isProjectOnRun[currentProject!!.id!!]!!) {
                        timerViewModel.toggleProjectTimer(false)
                    } else {
                        timerViewModel.setProjectStartTime(null)
                        timerViewModel.toggleProjectTimer(true)
                    }
                }
            ) {
                val icon = if (isProjectOnRun[currentProject!!.id!!]!!) {
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
    LaunchedEffect(isProjectOnRun[currentProject!!.id!!]) {
        if (isProjectOnRun[currentProject!!.id]!!) {
            while (isProjectOnRun[currentProject!!.id]!!) {
                delay(1000)
                time = timerViewModel.getOverallTime()
            }
        }
    }
}


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

@Composable
fun formatTime(timeMi: Long): String {
    return LocalTime.ofNanoOfDay(timeMi).format(DateTimeFormatter.ofPattern(stringResource(R.string.date_time_formatter_hours_minutes_seconds)))
}

@Composable
fun formatTimeProject(timeMi: Long): String {
    return LocalTime.ofNanoOfDay(timeMi).format(DateTimeFormatter.ofPattern(stringResource(R.string.date_time_format_hour_minute)))
}