package com.example.xpense_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.xpense_app.controller.service.ExpenseService
import com.example.xpense_app.navigation.NavGraph
import com.example.xpense_app.view.theme.XPense_AppTheme

class MainActivity : ComponentActivity() {
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
                    NavGraph(applicationContext, expenseService)
                }
            }
        }
    }
}