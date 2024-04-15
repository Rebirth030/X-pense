package com.example.xpense_app.navigation

/**
 * Enum containing the different screens in the app
 */
enum class Screen {
    HOME,
    LOGIN,
    REGISTER,
    PROFILES,
    MANUAL
}

/**
 * Sealed class for the different navigation items in the app
 * Each item has a route which is the name of the screen
 */
sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object Login : NavigationItem(Screen.LOGIN.name)
    data object Register : NavigationItem(Screen.REGISTER.name)
    data object Profiles : NavigationItem(Screen.PROFILES.name)
    data object Manual : NavigationItem(Screen.MANUAL.name)


}