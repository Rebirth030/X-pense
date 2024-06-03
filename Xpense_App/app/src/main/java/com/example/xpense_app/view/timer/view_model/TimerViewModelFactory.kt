package com.example.xpense_app.view.timer.view_model

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.xpense_app.model.User

/**
 * Factory class for creating instances of [TimerViewModel].
 *
 * This factory is responsible for instantiating [TimerViewModel] with the provided [currentUser] state.
 * It implements the [ViewModelProvider.Factory] interface to support ViewModel creation in the ViewModelProvider.
 *
 * @property currentUser The mutable state of the current user, used to initialize the TimerViewModel.
 * @constructor Creates a TimerViewModelFactory with the specified current user state.
 */
class TimerViewModelFactory(private val currentUser: MutableState<User>) : ViewModelProvider.Factory {
    /**
     * Creates a new instance of the specified ViewModel class.
     *
     * This method is called by the ViewModelProvider to create a new instance of the ViewModel.
     * It checks if the specified model class is assignable from [TimerViewModel] and returns an instance
     * of [TimerViewModel] initialized with the provided [currentUser] state.
     * If the model class is not [TimerViewModel], it throws an [IllegalArgumentException].
     *
     * @param modelClass The class of the ViewModel to create.
     * @return A new instance of the specified ViewModel class.
     * @throws IllegalArgumentException If the specified model class is not [TimerViewModel].
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            return TimerViewModel(currentUser) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
