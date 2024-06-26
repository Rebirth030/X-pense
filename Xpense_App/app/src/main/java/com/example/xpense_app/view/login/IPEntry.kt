package com.example.xpense_app.view.login

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.xpense_app.MainActivity
import com.example.xpense_app.R
import com.example.xpense_app.controller.RetrofitInstance.testConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IPEntry(showDialog: MutableState<Boolean>, inputText: MutableState<String>) {
    var connectionError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val ipRegex = Regex("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")
    val context = LocalContext.current

    if (showDialog.value) {
        ModalDialog(
            onDismissRequest = {
            }
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = stringResource(R.string.enter_ip))
                    TextField(
                        value = inputText.value,
                        onValueChange = { inputText.value = it },
                        placeholder = { Text(stringResource(R.string.example_ip)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("IP-Adresse") }
                    )
                    Button(
                        onClick = {
                            if (inputText.value.matches(ipRegex)) {
                                scope.launch {
                                    connectionError = !withContext(Dispatchers.IO) {
                                        testConnection(inputText.value)
                                    }
                                    if (!connectionError) {
                                        showDialog.value = false
                                        val intent = Intent(context, MainActivity::class.java)
                                        context.startActivity(intent)
                                    }
                                }
                            } else {
                                connectionError = true
                            }
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                    Button(
                        onClick = {
                            showDialog.value = false
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(stringResource(R.string.testing_ip))
                    }
                    if (connectionError) {
                        Text(
                            text = stringResource(R.string.check_ip),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModalDialog(onDismissRequest: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        content()
    }
}