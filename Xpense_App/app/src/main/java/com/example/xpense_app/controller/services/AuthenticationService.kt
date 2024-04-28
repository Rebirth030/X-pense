package com.example.xpense_app.controller.services

import com.example.xpense_app.controller.RetrofitInstance
import com.example.xpense_app.controller.interfaces.AuthenticationAPIService
import com.example.xpense_app.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log

class AuthenticationService {
    companion object {
        private val apiService =
            RetrofitInstance.getAPIService(AuthenticationAPIService::class) as AuthenticationAPIService

        fun registerUser(
            user: User,
            onSuccess: suspend (User) -> Unit,
            onError: suspend (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    apiService.registerUser(user)
                    onSuccess(user)
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }

        fun loginUser(
            user: User,
            onSuccess: suspend (User) -> Unit,
            onError: suspend (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val loginUser = apiService.loginUser(user)
                    onSuccess(loginUser)
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    }
}