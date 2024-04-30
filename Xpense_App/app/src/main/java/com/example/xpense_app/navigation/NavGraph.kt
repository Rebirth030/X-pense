package com.example.xpense_app.navigation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.xpense_app.model.User
import com.example.xpense_app.view.timer.Timer
import com.example.xpense_app.view.login.CreateRegister
import com.example.xpense_app.view.login.LoginForm
import com.example.xpense_app.view.manualBooking.AddExpense
import com.example.xpense_app.view.overview.CreateOverview
import com.example.xpense_app.view.timer.view_model.TimerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(context: Context, timerViewModel: TimerViewModel, appViewModel: AppViewModel) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedNavItem = remember {
        mutableStateOf<NavigationItem?>(null)
    }
    val title = getTitle(navController)
    val currentUser = remember { mutableStateOf(User(password = "", username = "")) }
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                // Drawer content goes here

               // CreateNavigationItem(NavigationItem.Login.name, coroutineScope, drawerState, navController, NavigationItem.Login, selectedNavItem)
                //CreateNavigationItem(NavigationItem.Register.name, coroutineScope, drawerState, navController, NavigationItem.Register, selectedNavItem)
                CreateNavigationItem(NavigationItem.Timer.name, coroutineScope, drawerState, navController, NavigationItem.Timer, selectedNavItem)
                CreateNavigationItem(NavigationItem.Profiles.name, coroutineScope, drawerState, navController, NavigationItem.Profiles, selectedNavItem)
                CreateNavigationItem(NavigationItem.Manual.name, coroutineScope, drawerState, navController, NavigationItem.Manual, selectedNavItem)
                CreateNavigationItem(NavigationItem.Overview.name, coroutineScope, drawerState, navController, NavigationItem.Manual, selectedNavItem)
            }
        }
    ) {
        Scaffold(topBar = {
            if(navController.currentDestination?.route != NavigationItem.Login.route && navController.currentDestination?.route != NavigationItem.Register.route){
            TopAppBar(title = { Text(text = title) }, navigationIcon = {
                IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                        Icon(Icons.Rounded.Menu, contentDescription = "Menu", modifier = Modifier.padding(horizontal = 8.dp))
                    }
            })}
        }, content = {  padding -> NavHost(navController = navController, startDestination = NavigationItem.Timer.route) {
            composable(NavigationItem.Login.route) { LoginForm(navController, currentUser, appViewModel) }
            composable(NavigationItem.Register.route) { CreateRegister(navController) }
            composable(NavigationItem.Timer.route) { Timer(timerViewModel, onNavigateToLoginScreen = {
                navController.navigate(NavigationItem.Login.route)
            }, appViewModel) }
            composable(NavigationItem.Profiles.route) {  }
            composable(NavigationItem.Manual.route) { AddExpense(navController, user = currentUser) }
            composable(NavigationItem.Overview.route) { CreateOverview(currentUser.value, navController, padding)}

        }})
    }
}

@Composable
private fun CreateNavigationItem(
    text: String,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState,
    navController: NavHostController,
    navRoute: NavigationItem,
    selectedNavItem: MutableState<NavigationItem?>
) {
    val isSelected = selectedNavItem.value == navRoute

    val itemColor = when {
        isSelected -> Color.LightGray
        else -> Color.White
    }

    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .padding(horizontal = 8.dp)
            .background(itemColor, shape = RoundedCornerShape(26.dp))
    ) {
    NavigationDrawerItem(
        label = { Text(text = text) },
        selected = isSelected,
        onClick = {
            selectedNavItem.value = navRoute
            coroutineScope.launch {
                drawerState.close()
            }
            navController.navigate(navRoute.route)
        }
    )
}}

/**
 * Function to get title of the current screen
 */
@Composable
private fun getTitle(navHostController: NavHostController): String {
    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    try {
        return NavigationItem.fromRoute(currentRoute ?: NavigationItem.Login.route).name
    } catch (e: IllegalArgumentException) {
        return "Title not found"
    }
}