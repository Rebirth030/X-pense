package com.example.xpense_app.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.xpense_app.R
import com.example.xpense_app.home.model.DateTimeModel
import com.example.xpense_app.home.model.ExpenseViewModel
import com.example.xpense_app.home.service.Expense
import com.example.xpense_app.ui.theme.Typography
import kotlinx.coroutines.delay
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit


@Composable
fun TimerHead() {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        TopSection()
        Spacer(modifier = Modifier.height(10.dp))
        ExpenseTimer()
    }

}

// include in nav header
@Composable
fun TopSection() {
    Spacer(modifier = Modifier.height(50.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Timer",
            style = Typography.headlineLarge,
            modifier = Modifier
                .weight(1f)
                .wrapContentWidth(Alignment.CenterHorizontally),
        )
        ProfilePicture(
            image = painterResource(id = R.drawable.example_profile_pic_xpense),
            modifier = Modifier.size(48.dp) // Adjust size as needed
        )
    }
}

@Composable
fun ProfilePicture(
    image: Painter,
    modifier: Modifier = Modifier
) {
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .size(70.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun CurrentDateTime() {

}

@Composable
fun ExpenseTimer() {
    var time by remember {
        mutableStateOf(0L)
    }
    var isRunning by remember {
        mutableStateOf(false)
    }
    var startTime by remember {
        mutableStateOf(0L)
    }
    val projectId by remember {
        mutableStateOf(54L) // dummy project id
    }
    val userId by remember {
        mutableStateOf(202L) // dummy user id
    }
    val weeklyTimecardId by remember {
        mutableStateOf(2L) // dummy weekly time card id
    }
    var expense by remember {
        mutableStateOf(
            Expense(
                null,
                DateTimeModel(
                    Instant.now(Clock.system(ZoneId.systemDefault())).toEpochMilli()
                ).getLocalDateTime(),
                null,
                "PENDING",
                userId,
                projectId,
                weeklyTimecardId
            )
        )
    }

    val context = LocalContext.current
    val expenseViewModel = ExpenseViewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = formatTime(timeMi = time),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(9.dp)
        )
        Spacer(modifier = Modifier.height(18.dp))
        Row {
            IconButton(
                onClick = {
                    if (isRunning) {
                        isRunning = false
                        expense = expense.copy(state = "PAUSED")
                    } else {
                        startTime = System.currentTimeMillis() - time
                        isRunning = true
                        expense = expense.copy(state = "RUNNING")
                    }
                }) {
                val icon = if (isRunning) {
                    R.drawable.pause_solid
                } else {
                    R.drawable.play_solid
                }
                Icon(painterResource(id = icon), contentDescription = "Play/Pause timer")
            }
            Spacer(modifier = Modifier.height(16.dp))
            IconButton(onClick = {
                time = 0
                isRunning = false
                expense = expense.copy(
                    endDateTime = DateTimeModel(
                        Instant.now(Clock.system(ZoneId.systemDefault())).toEpochMilli()
                    ).getLocalDateTime()
                )
            }) {
                Icon(painterResource(id = R.drawable.stop_solid), contentDescription = "Stop timer")
            }
        }

    }
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000)
            time = System.currentTimeMillis() - startTime
        }
    }
    LaunchedEffect(expense) {
        expenseViewModel.getExpenses(1L,
            onSuccess = {
                val expenses = it
                val d = 0
            },
            onError = {})
        expenseViewModel.saveExpense(
            expense,
            onSuccess = {
                expense = it
            },
            onError = {}
        )
    }
}

@Composable
fun formatTime(timeMi: Long): String {
    val hours = TimeUnit.MICROSECONDS.toHours(timeMi)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeMi) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeMi) % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}
