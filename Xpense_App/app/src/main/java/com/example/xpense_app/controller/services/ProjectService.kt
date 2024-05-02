package com.example.xpense_app.controller.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xpense_app.controller.RetrofitInstance
import com.example.xpense_app.controller.interfaces.ProjectAPIService
import com.example.xpense_app.controller.service.ExpenseAPIService
import com.example.xpense_app.model.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProjectService {
    companion object {
        private val apiService = RetrofitInstance.getAPIService(ProjectAPIService::class) as ProjectAPIService

        fun getProjects(
            token: String,
            onSuccess: suspend (List<Project>) -> Unit,
            onError: suspend (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.getProjects(token)
                    onSuccess(response.body().orEmpty())
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }
    }


}