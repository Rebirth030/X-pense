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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.xpense_app.view.TimerHead
import com.example.xpense_app.view.login.CreateRegister
import com.example.xpense_app.view.login.LoginForm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(context: Context) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val selectedNavItem = remember {
        mutableStateOf<NavigationItem?>(null)
    }
    val title = getTitle(selectedNavItem.value)
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                // Drawer content goes here

               // CreateNavigationItem(NavigationItem.Login.name, coroutineScope, drawerState, navController, NavigationItem.Login, selectedNavItem)
               // CreateNavigationItem(NavigationItem.Register.name, coroutineScope, drawerState, navController, NavigationItem.Register, selectedNavItem)
                CreateNavigationItem(NavigationItem.Timer.name, coroutineScope, drawerState, navController, NavigationItem.Timer, selectedNavItem)
                CreateNavigationItem(NavigationItem.Profiles.name, coroutineScope, drawerState, navController, NavigationItem.Profiles, selectedNavItem)
                CreateNavigationItem(NavigationItem.Manual.name, coroutineScope, drawerState, navController, NavigationItem.Manual, selectedNavItem)
            }
        }
    ) {
        Scaffold(topBar = {
            TopAppBar(title = { Text(text = title) }, navigationIcon = {
                IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                        Icon(Icons.Rounded.Menu, contentDescription = "Menu", modifier = Modifier.padding(horizontal = 8.dp))
                    }
            })
        }, content = {  padding -> NavHost(navController = navController, startDestination = NavigationItem.Login.route) {
            composable(NavigationItem.Login.route) { LoginForm() }
            composable(NavigationItem.Register.route) { CreateRegister() }
            composable(NavigationItem.Timer.route) { TimerHead() }
            composable(NavigationItem.Profiles.route) {  }
            composable(NavigationItem.Manual.route) {  }
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
            navController.navigate(navRoute.route) {
                popUpTo(0)
            }
        }
    )
}}

@Composable
private fun getTitle(selectedNavItem: NavigationItem?): String {
    return selectedNavItem?.name ?: "Default Title"
}
