package com.example.xpense_app.controller.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xpense_app.controller.interfaces.UserAPIService
import com.example.xpense_app.controller.service.APIService
import com.example.xpense_app.controller.service.Expense
import com.example.xpense_app.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserService {

    var errorMessage: String by mutableStateOf("")

    fun registerUser(
        user: User,
        onSuccess: (User) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val apiService = UserAPIService.getInstance()
            try {
                apiService.registerUser(user)
                onSuccess(user)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}