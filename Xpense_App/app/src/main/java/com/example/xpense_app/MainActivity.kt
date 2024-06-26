package com.example.xpense_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.xpense_app.navigation.AppViewModel
import com.example.xpense_app.navigation.NavGraph
import com.example.xpense_app.view.theme.XPense_AppTheme


class MainActivity : ComponentActivity() {
    private val appViewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XPense_AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(context = applicationContext, appViewModel = appViewModel)
                }
            }
        }
    }
}
