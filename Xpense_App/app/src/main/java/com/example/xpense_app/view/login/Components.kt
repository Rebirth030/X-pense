@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.xpense_app.view.login
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.xpense_app.R

/**
 * Create a TextField for the given fieldname.
 *
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
        placeholder = { Text(stringResource(R.string.placeholder_enter_your, fieldName)) },
        leadingIcon = { CreateLeadingIcon(fieldName) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    )
    return text
}

/**
 * Create a password field hides the text and shows a trailing icon to toggle the visibility.
 *
 * @return the value of the password field
 */
@Composable
fun createPasswordField(isLastPasswordField: Boolean = true): MutableState<String> {
    val password = remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    TextField(
        value = password.value,
        onValueChange = { it: String -> password.value = it },
        label = { Text(stringResource(R.string.password).uppercase()) },
        placeholder = { Text(stringResource(R.string.placeholder_enter_your_password)) },
        leadingIcon = { CreateLeadingIcon(stringResource(R.string.password)) },
        trailingIcon = { passwordVisibility = createTrailingIcon() },
        visualTransformation = if (!passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            imeAction = if (isLastPasswordField) ImeAction.Done else ImeAction.Next,
            keyboardType = KeyboardType.Password
        ),
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        modifier = Modifier.fillMaxWidth()
    )
    return password
}

/**
 * Create Leading Icon for the TextField.
 *
 * @param fieldname the name of the field
 */
@Composable
fun CreateLeadingIcon(fieldname: String) {
    Icon(
        when (fieldname) {
            stringResource(R.string.email) -> Icons.Default.Email
            stringResource(R.string.username) -> Icons.Default.Person
            stringResource(R.string.password) -> Icons.Default.Lock
            else -> Icons.Default.Create
        },
        contentDescription = "",
        tint = MaterialTheme.colorScheme.primary
    )
}

/**
 * Create a trailing icon for the password field.
 *
 * @return the visibility of the password
 */
@Composable
fun createTrailingIcon(): Boolean {
    var isPasswordVisible by remember { mutableStateOf(false) }
    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
        Icon(
            if (isPasswordVisible) Icons.Default.Person else Icons.Default.Lock,
            contentDescription = stringResource(R.string.toggle_visibility),
            tint = MaterialTheme.colorScheme.primary
        )
    }
    return isPasswordVisible
}