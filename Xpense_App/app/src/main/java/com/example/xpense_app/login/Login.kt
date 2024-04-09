package com.example.xpense_app.login


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection

import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun LoginForm() {
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            var username = EntryField()
            var password = EntryField(true)
            Spacer(modifier = Modifier.height(10.dp))
            LabeledCheckbox()
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {}, //TODO Save
                enabled = true,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Login")
            }
        }
    }
}

@Composable
fun EntryField(isPassword: Boolean = false): MutableState<String> {
    val value = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var isPasswordVisible by remember { mutableStateOf(false) }

    val trailingIcon = if (isPassword) {
        null
    } else {
        null
    }
    val leadingIcon = @Composable {
        Icon(
            if (isPassword) Icons.Default.Lock else Icons.Default.Person,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    TextField(
        value = value.value,
        onValueChange = { it: String -> value.value = it },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = leadingIcon,
        trailingIcon = {isPasswordVisible = createTrailingIcon(if(isPassword) "password" else "username").value},
        keyboardOptions = if (isPassword) {
            KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            )
        } else {
            KeyboardOptions(imeAction = ImeAction.Next)
        },
        keyboardActions = if (isPassword) KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }) else KeyboardActions(
            onDone = { } //TODO add Sumbit
        ),
        placeholder = { if (isPassword) Text("Enter your Password") else Text("Enter your Username") },
        label = { if (isPassword) Text("Password") else Text("Username") },
        singleLine = true,
        visualTransformation = if (isPassword) {
            if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
        } else VisualTransformation.None
    )
    return value
}

@Composable
fun LabeledCheckbox(
    isChecked: Boolean = true
) {
    Row(
        Modifier
            .clickable(
                onClick = {} //TODO Add remember me
            )
            .padding(4.dp)
    ) {
        Checkbox(checked = isChecked, onCheckedChange = null)
        Spacer(Modifier.size(6.dp))
        Text("Remember Me")
    }
}


