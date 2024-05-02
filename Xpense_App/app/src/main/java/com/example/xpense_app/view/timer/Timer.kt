package com.example.xpense_app.view.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.xpense_app.navigation.AppViewModel
import com.example.xpense_app.navigation.ViewState
import com.example.xpense_app.view.timer.ui.CurrentDate
import com.example.xpense_app.view.timer.ui.CurrentTime
import com.example.xpense_app.view.timer.ui.ProjectList
import com.example.xpense_app.view.timer.ui.TimerButtons
import com.example.xpense_app.view.timer.view_model.TimerViewModel


@Composable
@ExperimentalMaterial3Api
fun Timer(timerViewModel: TimerViewModel, onNavigateToLoginScreen: () -> Unit = {}, appViewModel: AppViewModel
    ) {

    val projects by timerViewModel.projects.collectAsState()
    val viewState by appViewModel.viewState.collectAsState(initial = ViewState.Loading)
    when (viewState) {
        ViewState.NotLoggedIn -> {
            LaunchedEffect(viewState) {
                onNavigateToLoginScreen()
            }
        }
        ViewState.LoggedIn -> {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Spacer(modifier = Modifier.height(50.dp)) // header
                CurrentDate()
                Spacer(modifier = Modifier.height(10.dp))
                CurrentTime()
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(1.dp)
                        .background(color = Color.DarkGray)
                        .align(Alignment.CenterHorizontally)
                ) // divider
                ProjectList(projects, timerViewModel)
                Spacer(modifier = Modifier.height(10.dp))
                TimerButtons(timerViewModel)
            }
        }
        ViewState.Loading -> {
            // TO DO
        }
    }

}

