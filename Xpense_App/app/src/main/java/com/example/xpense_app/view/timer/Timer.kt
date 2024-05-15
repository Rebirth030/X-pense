package com.example.xpense_app.view.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.xpense_app.model.User
import com.example.xpense_app.navigation.AppViewModel
import com.example.xpense_app.navigation.ViewState
import com.example.xpense_app.view.timer.ui.CurrentDate
import com.example.xpense_app.view.timer.ui.CurrentTime
import com.example.xpense_app.view.timer.ui.ProjectList
import com.example.xpense_app.view.timer.ui.TimerButtons
import com.example.xpense_app.view.timer.view_model.TimerViewModel
import com.example.xpense_app.view.timer.view_model.TimerViewModelFactory


@Composable
@ExperimentalMaterial3Api
fun Timer(currentUser: MutableState<User>, onNavigateToLoginScreen: () -> Unit = {}, appViewModel: AppViewModel) {
    val viewState by appViewModel.viewState.collectAsState(initial = ViewState.Loading)
    when (viewState) {
        ViewState.NotLoggedIn -> {
            LaunchedEffect(viewState) {
                onNavigateToLoginScreen()
            }
        }

        ViewState.LoggedIn -> {
            val timerViewModel: TimerViewModel = viewModel(factory = TimerViewModelFactory(currentUser))
            val projects by timerViewModel.projects.collectAsState()
            val expenses by timerViewModel.expenses.collectAsState()
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
                if (projects.isNotEmpty()) {
                    ProjectList(projects, timerViewModel)
                    Spacer(modifier = Modifier.weight(1f))
                    TimerButtons(timerViewModel)
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Keine Projekte verfügber.\nBitte erstelle zuvor ein Projekt.",
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        ViewState.Loading -> {
            // TO DO
        }
    }
}


