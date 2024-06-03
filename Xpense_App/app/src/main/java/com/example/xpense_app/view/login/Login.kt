package com.example.xpense_app.view.login


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration

import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.xpense_app.controller.services.AuthenticationService
import com.example.xpense_app.controller.services.UserService
import com.example.xpense_app.model.User
import com.example.xpense_app.navigation.AppViewModel
import com.example.xpense_app.navigation.NavigationItem
import com.example.xpense_app.view.timer.Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Creates the login form.
 *
 * @param navHostController the navigation controller
 * @param user the user object
 * @param appViewModel the app view model
 */
@Composable
fun LoginForm(
    navHostController: NavHostController,
    user: MutableState<User>,
    appViewModel: AppViewModel
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            CreateLoginForm(navHostController, user, appViewModel)
        }
    }
}

/**
 * Creates the form for the login with a button to submit the form and a link to the registration page.
 *
 * @param navHostController the navigation controller
 * @param user the user object
 * @param appViewModel the app view model
 * @return the form
 */
@Composable
private fun CreateLoginForm(
    navHostController: NavHostController,
    user: MutableState<User>,
    appViewModel: AppViewModel
) {
    val username = createTextField(fieldName = "username")
    val password = createPasswordField()
    val context = LocalContext.current
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        "Registrieren",
        textDecoration = TextDecoration.Underline,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.clickable(onClick = { navHostController.navigate(NavigationItem.Register.route) })
    )
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        onClick = {
            loginAction(
                username.value,
                password.value,
                navHostController,
                context,
                user
            )
            appViewModel.setLoggedIn(true)
        },
        enabled = true,
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )

    ) {
        Text("Login")
    }
}


/**
 * Submits the form to the server and navigates to the login page if successful.
 *
 * @param username the username
 * @param password the password
 * @param navController the navigation controller
 * @param context the context
 * @param userResult the user object
 */
fun loginAction(
    username: String,
    password: String,
    navController: NavHostController,
    context: Context,
    userResult: MutableState<User>,
) {

    AuthenticationService.loginUser(
        user = User(
            username = username,
            password = password
        ),
        onSuccess = { user ->
            withContext(Dispatchers.Main) {
                userResult.value = user
                userResult.value.token = "Bearer " + user.token
                navController.navigate(NavigationItem.Overview.route)
            }
        },
        onError = {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    "An Error has occurred while login!: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )
}




