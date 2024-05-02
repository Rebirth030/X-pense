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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.xpense_app.controller.services.AuthenticationService
import com.example.xpense_app.model.User
import com.example.xpense_app.navigation.NavigationItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Base64

// optional TODO: Add error handling
// optional TODO: Make fields required or optional

@Composable
fun CreateRegister(navController: NavHostController) {
    Surface {
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
    val email = createTextField("email")
    val firstname = createTextField("firstname")
    val lastname = createTextField("lastname")
    val country = "Deutschland"
    val language = "DE"
    val username = createTextField("username")
    val password = createPasswordField(false)
    val repeatPassword = createPasswordField()
    val context = LocalContext.current

    Spacer(modifier = Modifier.height(20.dp))
    Text(
        "Login",
        textDecoration = TextDecoration.Underline,
        color = Color.Blue,
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
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Register")
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
        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
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
        weeklyWorkingHours = null,
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
                    "An Error has occurred while creating a user!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )

}

