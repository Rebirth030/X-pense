package com.example.xpense_app.view.timer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xpense_app.R
import com.example.xpense_app.model.Project
import com.example.xpense_app.view.timer.view_model.TimerViewModel
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timer

@Composable
fun CurrentDate() {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val currentDate = LocalDateTime.now().format(formatter)
    DisplayDateTime(text = currentDate)
}

@Composable
fun CurrentTime() {
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, bottom = 5.dp, top = 10.dp)
    ) {
        Text(
            text = "Projekte:",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(10.dp)
                .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(20.dp)
            ) {
                items(projects.size) { index ->
                    ProjectItem(
                        project = projects[index],
                        timerViewModel = timerViewModel
                    )
                }
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun ProjectItem(project: Project, timerViewModel: TimerViewModel) {
    var showProjectChangeDialog by remember { mutableStateOf(false) }
    val projectTimers by timerViewModel.projectTimers.collectAsState()
    val projectStartTimers by timerViewModel.projectTimersStartTime.collectAsState()
    val isProjectOnRun by timerViewModel.projectTimersOnRun.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = { showProjectChangeDialog = true }),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProjectLabel(project = project)
            ProjectName(project = project)
            Box(
                modifier = Modifier.padding(10.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Text(
                    text = formatTime(timeMi = projectTimers[project.id!!]!!),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
        }
    }
    LaunchedEffect(isProjectOnRun[project.id!!]) {
        if (isProjectOnRun[project.id]!!) {
            while (isProjectOnRun[project.id]!!) {
                delay(1000)
                timerViewModel.setProjectTime()
            }
        }
    }
    if (showProjectChangeDialog) {
        ProjectChangeDialog(
            project = project,
            timerViewModel = timerViewModel,
            onDismiss = {
                showProjectChangeDialog = false
            }
        )
    }
}

@Composable
fun ProjectLabel(project: Project) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color = Color.Blue, shape = CircleShape),
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
            .padding(start = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = project.name!!,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
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
        text = { Text(text = "Sicher dass du das Projekt wechseln willst?") },
        confirmButton = {
            Button(
                onClick = {
                    timerViewModel.changeProject(project)
                    onDismiss(false)
                }
            ) {
                Text(
                    text = "Ja",
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
                    text = "Abbrechen",
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
    Row {
        IconButton(
            onClick = {
                if (isProjectOnRun[currentProject!!.id!!]!!) {
                    timerViewModel.toggleProjectTimer(false)
                } else {
                    timerViewModel.setProjectStartTime()
                    timerViewModel.toggleProjectTimer(true)
                }
            }
        ) {
            val icon = if(isProjectOnRun[currentProject!!.id!!]!!) {
                R.drawable.pause_solid
            } else {
                R.drawable.play_solid
            }
            Icon(painterResource(id = icon), contentDescription = "Play/Pause timer")
        }
        Spacer(modifier = Modifier.height(16.dp))
        IconButton(onClick = {
            timerViewModel.stopAllProjectTimers()
            timerViewModel.updateExpense()
        }) {
            Icon(painterResource(id = R.drawable.stop_solid), contentDescription = "Stop timer")
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
    val hours = TimeUnit.MILLISECONDS.toHours(timeMi)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMi) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMi) % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}