package com.example.xpense_app.view.login


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration

import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.xpense_app.navigation.NavigationItem


@Composable
fun LoginForm(navHostController: NavHostController) {
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
            Spacer(modifier = Modifier.height(10.dp))
            LabeledCheckbox()
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Registrieren",
                textDecoration = TextDecoration.Underline,
                color = Color.Blue,
                modifier = Modifier.clickable( onClick = {navHostController.navigate(NavigationItem.Register.route)})
            )
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




