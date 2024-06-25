package com.example.xpense_app.navigation

import EncryptedSharedPreferencesHelper
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.xpense_app.R
import com.example.xpense_app.controller.services.UserService
import com.example.xpense_app.model.User
import com.example.xpense_app.view.create_project.CreateProjectScreen
import com.example.xpense_app.view.info_view.CreateInfoView
import com.example.xpense_app.view.timer.Timer
import com.example.xpense_app.view.login.CreateRegister
import com.example.xpense_app.view.login.IPEntry
import com.example.xpense_app.view.login.LoginForm
import com.example.xpense_app.view.manual_booking.AddExpense
import com.example.xpense_app.view.overview.CreateOverview
import com.example.xpense_app.view.profile.Profile
import com.example.xpense_app.view.projects_overview.ProjectsOverview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Composable function defining the navigation graph of the Xpense app.
 * Manages navigation between different screens and provides a navigation drawer for accessing app features.
 *
 * @param context The context of the calling component.
 * @param appViewModel The view model for the entire application.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(context: Context, appViewModel: AppViewModel) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedNavItem = remember { mutableStateOf<NavigationItem?>(null) }
    val ipInput = remember {
        mutableStateOf("")
    }
    val showIPModal = remember {
        mutableStateOf(true)
    }
    val title = getTitle(navController)
    val currentUser = remember { mutableStateOf(User(password = "", username = "")) }
    IPEntry(showDialog = showIPModal, inputText = ipInput)
    val hasLoggedIn by appViewModel.hasLoggedIn.collectAsState(initial = false)

    CheckUserLoggedIn(navController, appViewModel, currentUser)

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                for (item in NavigationItem.values()
                    .filter { it != NavigationItem.Login && it != NavigationItem.Register }) {
                    var navItemIconId = -1
                    when(item.toString()) {
                        "CreateProject" -> navItemIconId = R.drawable.create_project_24
                        "Info" -> navItemIconId = R.drawable.info
                        "Manual" -> navItemIconId = R.drawable.manual
                        "Overview" -> navItemIconId = R.drawable.overview
                        "Profiles" -> navItemIconId = R.drawable.profile
                        "ProjectsOverview" -> navItemIconId = R.drawable.project_overview
                        "Timer" -> navItemIconId = R.drawable.timer
                    }
                    CreateNavigationItem(
                        stringResource(item.titleResourceId),
                        coroutineScope,
                        drawerState,
                        navController,
                        item,
                        selectedNavItem,
                        navItemIconId
                    )
                }
            }
        }
    ) {
        Scaffold(topBar = {
            CreateTopAppBar(navController, coroutineScope, drawerState)
        }, content = { padding ->
            NavHost(
                navController = navController,
                startDestination = if (hasLoggedIn) NavigationItem.Overview.route else NavigationItem.Login.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(NavigationItem.Login.route) { LoginForm(navController, currentUser, appViewModel) }
                composable(NavigationItem.Register.route) { CreateRegister(navController) }
                composable(NavigationItem.Timer.route) {
                    Timer(currentUser, onNavigateToLoginScreen = {
                        navController.navigate(NavigationItem.Login.route)
                    }, appViewModel)
                }
                composable(NavigationItem.Profiles.route) { Profile(currentUser) }
                composable(NavigationItem.Manual.route) { AddExpense(navController, currentUser) }
                composable(NavigationItem.Overview.route) { CreateOverview(currentUser.value, navController) }
                composable(NavigationItem.CreateProject.route) { CreateProjectScreen(currentUser, context) }
                composable(NavigationItem.Info.route) { CreateInfoView(navController, currentUser) }
                composable(NavigationItem.ProjectsOverview.route) { ProjectsOverview(currentUser.value, navController) }
            }
        })
    }
}

/**
 * Composable function to check if the user is already logged in.
 * If the user is logged in, the app navigates to the overview screen.
 *
 * @param navController The navigation controller for managing navigation.
 * @param appViewModel The view model for the entire application.
 * @param currentUser The current user of the application.
 */
@Composable
fun CheckUserLoggedIn(navController: NavHostController, appViewModel: AppViewModel, currentUser: MutableState<User>) {
    val token = EncryptedSharedPreferencesHelper(LocalContext.current).getToken()
    if (token.isNullOrEmpty()) {
        return
    }
    val context = LocalContext.current
    LaunchedEffect(key1 = "token check") {
        UserService.getUserByToken(
            token = token,
            onSuccess = { user ->
                if (user == null) {
                    appViewModel.setLoggedIn(false)
                    return@getUserByToken
                }
                withContext(Dispatchers.Main) {
                    currentUser.value = user
                    currentUser.value.token = token
                    appViewModel.setLoggedIn(true)
                    navController.navigate(NavigationItem.Overview.route)
                    Toast.makeText(context, "Autologin Successful.", Toast.LENGTH_SHORT).show()
                }
            },
            onError = {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Autologin Failed, please login again.", Toast.LENGTH_SHORT).show()
                    appViewModel.setLoggedIn(false)
                }
            }
        )
    }
}

/**
 * Composable function to create the top app bar of the Xpense app.
 *
 * @param navController The navigation controller for managing navigation.
 * @param coroutineScope The coroutine scope for launching actions.
 * @param drawerState The state of the navigation drawer.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CreateTopAppBar(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState
) {
    if (navController.currentDestination?.route != NavigationItem.Login.route && navController.currentDestination?.route != NavigationItem.Register.route) {
        TopAppBar(title = { Text(text = getTitle(navController)) }, navigationIcon = {
            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                Icon(
                    Icons.Rounded.Menu,
                    contentDescription = "Menu",
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
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
    selectedNavItem: MutableState<NavigationItem?>,
    navItemIconId: Int
) {
    val isSelected = selectedNavItem.value == navRoute
    val itemColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.inversePrimary
    }

    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .padding(horizontal = 8.dp)
            .background(itemColor, shape = RoundedCornerShape(26.dp))
    ) {
        NavigationDrawerItem(
            icon = {
                if(navItemIconId > 0) {
                    Icon(
                        painter = painterResource(navItemIconId),
                        contentDescription = "create project",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            },
            label = { Text(text = text) },
            selected = isSelected,
            onClick = {
                selectedNavItem.value = navRoute
                coroutineScope.launch {
                    drawerState.close()
                }
                navController.navigate(navRoute.route) {
                    popUpTo(0)
                }
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
    val context = LocalContext.current
    return try {
        context.getString(NavigationItem.fromRoute(currentRoute ?: NavigationItem.Login.route).titleResourceId)
    } catch (e: IllegalArgumentException) {
        "Title not found"
    }
}