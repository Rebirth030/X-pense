package com.example.xpense_app.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CreateRegister() {
    val email = createRegisterField("email")
    val username = createRegisterField("username")
    val password = createRegisterField("password");
    val submit = registerButton()
    if (submit.value) {
        LaunchedEffect(key1 = submit.value) {
            //TODO: registerUser(email.value, username.value, password.value)
        }
    }
}

@Composable
fun createRegisterField(fieldname: String): MutableState<String> {
    val text = remember { mutableStateOf("") }
    var isPasswordVisible = remember { mutableStateOf(false) }
    TextField(
        value = text.value,
        onValueChange = { it: String -> text.value = it },
        label = { Text(fieldname.uppercase()) },
        placeholder = { Text("Enter your $fieldname") },
        trailingIcon = { isPasswordVisible = createTrailingIcon(fieldname) },
        leadingIcon = { CreateLeadingIcon(fieldname) },
        visualTransformation = if(fieldname == "password" && isPasswordVisible.value) PasswordVisualTransformation() else VisualTransformation.None,
    )
    return text
}

@Composable
fun CreateLeadingIcon(fieldname: String) {
    Icon(
        when (fieldname) {
            "email" -> Icons.Default.Email
            "username" -> Icons.Default.Person
            "password" -> Icons.Default.Lock
            else -> Icons.Default.Create
        },
        contentDescription = "",
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun createTrailingIcon(fieldname: String): MutableState<Boolean> {
    val isPasswordVisible = remember { mutableStateOf(false) }
    if (fieldname == "password") {
        IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
            Icon(
                if (isPasswordVisible.value) Icons.Default.Person else Icons.Default.Lock,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
    return isPasswordVisible
}

@Composable
fun registerButton(): MutableState<Boolean> {
    val submit = remember { mutableStateOf(false) }
    Button(onClick = { submit.value = true }) {
        Text("Register")
    }
    return submit
}

