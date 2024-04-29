package com.example.xpense_app.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class AppViewModel: ViewModel() {
    private val _hasLoggedIn = MutableStateFlow(false)
    val hasLoggedIn: Flow<Boolean> = _hasLoggedIn

    val viewState: Flow<ViewState> = hasLoggedIn.map { hasLoggedIn ->
        if (hasLoggedIn) {
            ViewState.LoggedIn
        } else {
            ViewState.NotLoggedIn
        }
    }

    fun setLoggedIn(loggedIn: Boolean) {
        _hasLoggedIn.value = loggedIn
    }
}

/**
 * sealed class representing the possible view states of the application
 */
sealed class ViewState {
    object Loading: ViewState() // hasLoggedIn = unknown
    object LoggedIn: ViewState() // hasLoggedIn = true
    object NotLoggedIn: ViewState() // hasLoggedIn = false
}