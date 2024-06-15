package com.example.xpense_app.view.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.xpense_app.R
import com.example.xpense_app.controller.services.AuthenticationService
import com.example.xpense_app.model.User
import com.example.xpense_app.navigation.NavigationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// optional TODO: Add error handling
// optional TODO: Make fields required or optional
// optional TODO: Add display country and language field

/**
 * Creates the registration page with a form
 *
 * @param navController the navigation controller
 */
@Composable
fun CreateRegister(navController: NavHostController) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            RegisterForm(navController)
        }
    }
}

/**
 * Creates the form for the registration
 * with a button to submit the form
 */
@Composable
fun RegisterForm(navController: NavHostController) {
    val email = createTextField(stringResource(R.string.email))
    val firstname = createTextField(stringResource(R.string.firstname))
    val lastname = createTextField(stringResource(R.string.lastname))
    val country = "Deutschland"
    val language = "DE"
    val username = createTextField(stringResource(R.string.username))
    val password = createPasswordField(false)
    val repeatPassword = createPasswordField()
    val context = LocalContext.current

    Spacer(modifier = Modifier.height(20.dp))
    Text(
        stringResource(R.string.login),
        textDecoration = TextDecoration.Underline,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.clickable(onClick = { navController.navigate(NavigationItem.Login.route) })
    )
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        onClick = {
            submitAction(
                firstname,
                lastname,
                email,
                username,
                password,
                repeatPassword,
                country,
                language,
                context,
                navController
            )
        },
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(stringResource(R.string.register))
    }
}

/**
 * Submits the form to the server
 * and navigates to the login page if successful
 */
fun submitAction(
    firstname: MutableState<String>,
    lastname: MutableState<String>,
    email: MutableState<String>,
    username: MutableState<String>,
    password: MutableState<String>,
    repeatPassword: MutableState<String>,
    country: String,
    language: String,
    context: Context,
    navController: NavHostController
) {
    if (password.value != repeatPassword.value) {
        Toast.makeText(context,
            context.getString(R.string.error_message_passwords_do_not_match), Toast.LENGTH_SHORT).show()
        return
    }
    val user = User(
        id = null,
        prename = firstname.value,
        lastname = lastname.value,
        email = email.value,
        username = username.value,
        password = password.value,
        country = country,
        language = language,
        weeklyWorkingHours = 40,
        holidayWorkingSchedule = null
    )

    AuthenticationService.registerUser(
        user = user,
        onSuccess = {
            withContext(Dispatchers.Main) {
                navController.navigate(NavigationItem.Login.route)
            }
        },
        onError = { e ->
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_message_an_error_has_occurred_while_creating_a_user),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )

}

