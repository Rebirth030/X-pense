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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.xpense_app.model.User
import com.example.xpense_app.view.createProject.CreateProjectScreen
import com.example.xpense_app.view.timer.Timer
import com.example.xpense_app.view.login.CreateRegister
import com.example.xpense_app.view.login.LoginForm
import com.example.xpense_app.view.manualBooking.AddExpense
import com.example.xpense_app.view.overview.CreateOverview
import com.example.xpense_app.view.timer.view_model.TimerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Composable function defining the navigation graph of the Xpense app.
 * Manages navigation between different screens and provides a navigation drawer for accessing app features.
 *
 * @param context The context of the calling component.
 * @param timerViewModel The view model for the timer feature.
 * @param appViewModel The view model for the entire application.
 */
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
                for (navigationItem in NavigationItem.values().filter { navItem -> !(navItem == NavigationItem.Login || navItem == NavigationItem.Register) }) {
                    CreateNavigationItem(
                        text = navigationItem.name,
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        navController = navController,
                        navRoute = navigationItem,
                        selectedNavItem = selectedNavItem
                    )
                }
            }
        }
    ) {
        Scaffold(topBar = {
            if(navController.currentDestination?.route != NavigationItem.Login.route && navController.currentDestination?.route != NavigationItem.Register.route){
            TopAppBar(title = { Text(text = title) }, navigationIcon = {
                IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                    Icon(
                        Icons.Rounded.Menu,
                        contentDescription = "Menu",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            })}
        }, content = { padding ->
            NavHost(navController = navController, startDestination = NavigationItem.Login.route) {
                composable(NavigationItem.Login.route) {
                    LoginForm(
                        navController,
                        currentUser,
                        appViewModel
                    )
                }
                composable(NavigationItem.Register.route) { CreateRegister(navController) }
                composable(NavigationItem.Timer.route) {
                    Timer(timerViewModel, onNavigateToLoginScreen = {
                        navController.navigate(NavigationItem.Login.route)
                    }, appViewModel)
                }
                composable(NavigationItem.Profiles.route) { }
                composable(NavigationItem.Manual.route) {
                    AddExpense(
                        navController,
                        user = currentUser
                    )
                }
                composable(NavigationItem.Overview.route) {
                    CreateOverview(
                        currentUser.value,
                        navController,
                        padding
                    )
                }
                composable(NavigationItem.Create.route) { CreateProjectScreen(currentUser, context) }

            }
        })
    }
}

/**
 * Composable function to create a navigation item in the navigation drawer.
 * Manages the selection and navigation to the specified route.
 *
 * @param text The text label of the navigation item.
 * @param coroutineScope The coroutine scope for launching actions.
 * @param drawerState The state of the navigation drawer.
 * @param navController The navigation controller for managing navigation.
 * @param navRoute The navigation item route.
 * @param selectedNavItem The state to track the currently selected navigation item.
 */
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
    }
}

/**
 * Retrieves the title of the current screen based on the navigation destination.
 *
 * @param navHostController The navigation controller managing the navigation within the app.
 * @return The title of the current screen, or "Title not found" if the title couldn't be determined.
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