package com.example.xpense_app.controller.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.xpense_app.controller.interfaces.UserAPIService
import com.example.xpense_app.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserService {

    fun registerUser(
        user: User,
        onSuccess: suspend (User) -> Unit,
        onError: suspend (Exception) -> Unit
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