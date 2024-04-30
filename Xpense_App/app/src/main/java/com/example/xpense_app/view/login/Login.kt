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


@Composable
fun LoginForm(navHostController: NavHostController, user: MutableState<User>, appViewModel: AppViewModel) {
    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
        ) {
            var username = createTextField(fieldName = "username")
            var password = createPasswordField()
            val context = LocalContext.current
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Registrieren",
                textDecoration = TextDecoration.Underline,
                color = Color.Blue,
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }
    }
}


/**
 * Submits the form to the server
 * and navigates to the login page if successful
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




