package com.example.xpense_app.view.login

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
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