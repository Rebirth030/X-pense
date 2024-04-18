package com.example.xpense_app.view.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Create a TextField for the given fieldname
 * @param fieldName the name of the field
 * @return the value of the textfield
 */
@Composable
fun createTextField(fieldName: String): MutableState<String> {
    val text = remember { mutableStateOf("") }
    TextField(
        value = text.value,
        onValueChange = { it: String -> text.value = it },
        label = { Text(fieldName.uppercase()) },
        placeholder = { Text("Enter your $fieldName") },
        leadingIcon = { CreateLeadingIcon(fieldName) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true
    )
    return text
}

/**
 * Create a password field
 * hides the text and shows a trailing icon to toggle the visibility
 * @return the value of the password field
 */
@Composable
fun createPasswordField(isLastPasswordField: Boolean = true): MutableState<String> {
    val password = remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    TextField(
        value = password.value,
        onValueChange = { it: String -> password.value = it },
        label = { Text("PASSWORD") },
        placeholder = { Text("Enter your password") },
        leadingIcon = { CreateLeadingIcon("password") },
        trailingIcon = { passwordVisibility = createTrailingIcon() },
        visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = if(isLastPasswordField) ImeAction.Done else ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
        singleLine = true
    )
    return password
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
fun createTrailingIcon(): Boolean {
    var isPasswordVisible by remember { mutableStateOf(false) }
    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
        Icon(
            if (isPasswordVisible) Icons.Default.Person else Icons.Default.Lock,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    return isPasswordVisible
}