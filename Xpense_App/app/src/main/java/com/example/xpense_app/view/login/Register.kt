package com.example.xpense_app.view.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.xpense_app.MainActivity
import com.example.xpense_app.controller.services.UserService
import com.example.xpense_app.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Base64

@Composable
fun CreateRegister() {
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            RegisterForm()
        }
    }
}

/**
 * Creates the form for the registration
 * with a button to submit the form
 */
@Composable
fun RegisterForm() {
    val email = createTextField("email")
    val prename = createTextField("prename")
    val lastname = createTextField("lastname")
    val country = "Deutschland"
    val language = "DE"
    val username = createTextField("username")
    val password = createPasswordField(false)
    val repeatPassword = createPasswordField()
    val context = LocalContext.current

    Spacer(modifier = Modifier.height(10.dp))
    LabeledCheckbox()
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        onClick = { submitAction(prename, lastname, email, username, password, repeatPassword, country, language, context) },
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier.fillMaxWidth()
    ){
        Text("Register")
    }
}

/**
 * Submits the form to the server
 * and navigates to the login page if successful
  */
fun submitAction(
    prename: MutableState<String>,
    lastname: MutableState<String>,
    email: MutableState<String>,
    username: MutableState<String>,
    password: MutableState<String>,
    repeatPassword: MutableState<String>,
    country: String,
    language: String,
    context: Context
) {
    if (password.value != repeatPassword.value) {
        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
        return
    }
    // TODO: Add good encryption
    val encoder = Base64.getEncoder()
    val user = User(
        id = null,
        prename = prename.value,
        lastname = lastname.value,
        email = email.value,
        username = username.value,
        password = String(encoder.encode(password.value.toByteArray())),
        country = country,
        language = language,
        weeklyWorkingHours = null,
        holidayWorkingSchedule = null
    )
    CoroutineScope(Dispatchers.IO).launch {
        val userService = UserService()
        userService.registerUser(
            user = user,
            onSuccess = { user -> /* TODO: Navigate to login */ Toast.makeText(context, "Registry Successful", Toast.LENGTH_SHORT).show()},
            onError = { exception -> /* TODO: Implement better exception handling */ Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()})
    }
}

