package com.example.xpense_app.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun navGraph(context: Context) {
    var navController = rememberNavController()
    var navHost = NavHost(navController, startDestination = NavigationItem.Login.route, builder = {
        composable(NavigationItem.Login.route) {
            com.example.xpense_app.login.LoginForm()
        }
        composable(NavigationItem.Register.route) {
            com.example.xpense_app.login.CreateRegister()
        }

    })
}
