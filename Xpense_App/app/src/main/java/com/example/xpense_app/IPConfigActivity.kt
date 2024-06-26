package com.example.xpense_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.xpense_app.view.login.IPEntry
import com.example.xpense_app.view.theme.XPense_AppTheme

class IPConfigActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            XPense_AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val ipInput = remember {
                        mutableStateOf("")
                    }
                    val showIPModal = remember {
                        mutableStateOf(true)
                    }
                    IPEntry(showDialog = showIPModal, inputText = ipInput)
                }
            }
        }
    }
}
