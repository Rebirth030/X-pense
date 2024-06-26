package com.example.xpense_app.controller.services

import com.example.xpense_app.controller.RetrofitInstance
import com.example.xpense_app.controller.interfaces.UserAPIService
import com.example.xpense_app.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserService {
    companion object {
        private val apiService =
            RetrofitInstance.getAPIService(UserAPIService::class) as UserAPIService

        fun updateUser(
            user: User,
            token: String,
            onSuccess: suspend (User) -> Unit,
            onError: suspend (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response =apiService.updateUser(token, user.id!!, user)
                    onSuccess(response.body()!!)
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }

        fun getUserByToken(
            token: String,
            onSuccess: suspend (User?) -> Unit,
            onError: suspend (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.getUserByToken(token)
                    onSuccess(response.body())
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    }

}