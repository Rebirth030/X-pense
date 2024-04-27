package com.example.xpense_app.navigation

/**
 * Enum containing the different screens in the app
 */
enum class Screen {
    TIMER,
    LOGIN,
    REGISTER,
    PROFILES,
    MANUAL
}

/**
 * Sealed class for the different navigation items in the app
 * Each item has a route which is the name of the screen
 */
sealed class NavigationItem(val route: String, val name:String) {
    data object Timer : NavigationItem(Screen.TIMER.name, "Timer")
    data object Login : NavigationItem(Screen.LOGIN.name, "Login")
    data object Register : NavigationItem(Screen.REGISTER.name, "Register")
    data object Profiles : NavigationItem(Screen.PROFILES.name, "Profiles")
    data object Manual : NavigationItem(Screen.MANUAL.name, "Manual Booking")

    companion object {
        fun fromRoute(route: String): NavigationItem {
            return when(route) {
                Screen.TIMER.name -> Timer
                Screen.LOGIN.name -> Login
                Screen.REGISTER.name -> Register
                Screen.PROFILES.name -> Profiles
                Screen.MANUAL.name -> Manual
                else -> throw IllegalArgumentException("Route $route not found")
            }
        }
    }

}