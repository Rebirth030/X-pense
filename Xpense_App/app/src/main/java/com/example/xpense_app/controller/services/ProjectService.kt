package com.example.xpense_app.controller.services

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xpense_app.controller.interfaces.ProjectAPIService
import com.example.xpense_app.model.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProjectService {
    private var _projects = mutableListOf<Project>()
    var errorMessage: String by mutableStateOf("")


    fun getProjects(
        onSuccess: (List<Project>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val apiService = ProjectAPIService.getInstance()
            try {
                val response = apiService.getProjects();
                _projects.clear()
                _projects.addAll(response.body()!!)
                onSuccess(response.body()!!)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}