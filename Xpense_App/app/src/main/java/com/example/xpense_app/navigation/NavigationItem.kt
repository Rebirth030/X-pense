package com.example.xpense_app.navigation

import com.example.xpense_app.R

/**
 * Enum containing the different screens in the app
 */
enum class Screen {
    TIMER,
    LOGIN,
    REGISTER,
    PROFILES,
    MANUAL,
    OVERVIEW,
    CREATE,
    INFO
}

/**
 * Sealed class for the different navigation items in the app
 * Each item has a route which is the name of the screen
 */
sealed class NavigationItem(val route: String, val name: String, val titleResourceId: Int) {
    data object Timer : NavigationItem(Screen.TIMER.name, "Timer", R.string.timer)
    data object Login : NavigationItem(Screen.LOGIN.name, "Login", R.string.login)
    data object Register : NavigationItem(Screen.REGISTER.name, "Register", R.string.register)
    data object Profiles : NavigationItem(Screen.PROFILES.name, "Profiles", R.string.profile)
    data object Manual : NavigationItem(Screen.MANUAL.name, "Manual Booking", R.string.manual)
    data object Overview : NavigationItem(Screen.OVERVIEW.name, "Overview", R.string.overview)
    data object Create : NavigationItem(Screen.CREATE.name, "Create Project", R.string.create_project)
    data object Info : NavigationItem(Screen.INFO.name, "Info", R.string.info)

    companion object {

        /**
         * Get all values of the sealed class
         *
         * @return a list of all values
         */
        fun values() = NavigationItem::class.sealedSubclasses.map { it.objectInstance as NavigationItem }

        /**
         * Get a navigation item from a route
         *
         * @param route the route of the navigation item
         * @return the navigation item
         */
        fun fromRoute(route: String): NavigationItem {
            return values().find { it.route == route }
                ?: throw IllegalArgumentException("Route $route not found")
        }
    }

}