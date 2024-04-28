package com.example.xpense_app.navigation

/**
 * Enum containing the different screens in the app
 */
enum class Screen {
    TIMER,
    LOGIN,
    REGISTER,
    PROFILES,
    MANUAL,
    OVERVIEW
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
    data object Overview: NavigationItem(Screen.OVERVIEW.name, "Overview")

    companion object {
        fun values() = NavigationItem::class.sealedSubclasses.map { it.objectInstance as NavigationItem }
        fun fromRoute(route: String): NavigationItem {
            return values().find { it.route == route } ?: throw IllegalArgumentException("Route $route not found")
        }
    }

}