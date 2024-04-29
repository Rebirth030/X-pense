package com.example.xpense_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.xpense_app.controller.services.ExpenseService
import com.example.xpense_app.navigation.AppViewModel
import com.example.xpense_app.navigation.NavGraph
import com.example.xpense_app.view.theme.XPense_AppTheme
import com.example.xpense_app.view.timer.view_model.TimerViewModel

class MainActivity : ComponentActivity() {
    private val appViewModel: AppViewModel by viewModels()
    private val timerViewModel: TimerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val expenseService: ExpenseService = ExpenseService()
        super.onCreate(savedInstanceState)
        setContent {
            XPense_AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(applicationContext, timerViewModel, appViewModel)
                }
            }
        }
    }
}