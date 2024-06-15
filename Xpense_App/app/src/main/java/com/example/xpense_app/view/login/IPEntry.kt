package com.example.xpense_app.view.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.xpense_app.controller.RetrofitInstance
import com.example.xpense_app.controller.RetrofitInstance.setURL
import com.example.xpense_app.controller.RetrofitInstance.testConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IPEntry(showDialog:MutableState<Boolean>, inputText:MutableState<String>) {
    var connectionError by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    if (showDialog.value) {
        ModalDialog(
            onDismissRequest = {
                // Prevent dismissal by not calling `showDialog = false`
            }
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Bitte füllen Sie dieses Feld aus")
                    TextField(
                        value = inputText.value,
                        onValueChange = { inputText.value = it },
                        label = { Text("IP-Adresse") }
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                connectionError = !withContext(Dispatchers.IO) {
                                    RetrofitInstance.testConnection(inputText.value)
                                }
                                if (!connectionError) {
                                    showDialog.value = false
                                }
                            }
                        },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Bestätigen")
                    }
                    if (connectionError) {
                        Text(text = "Verbindung fehlgeschlagen. Bitte überprüfen Sie die IP-Adresse.", color = MaterialTheme.colorScheme.error)
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