package com.example.xpense_app.controller.services

import Expense
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.xpense_app.controller.RetrofitInstance
import com.example.xpense_app.controller.interfaces.ProjectAPIService
import com.example.xpense_app.model.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProjectService {

    private var _projects = mutableListOf<Project>()
    private val apiService =
        RetrofitInstance.getAPIService(ProjectAPIService::class) as ProjectAPIService
    fun getProjects(
        onSuccess: (List<Project>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {

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

    companion object {
        private val apiService =
            RetrofitInstance.getAPIService(ProjectAPIService::class) as ProjectAPIService

        /**
         * Get all projects from the server.
         *
         * @param onSuccess the callback when the projects are successfully fetched
         * @param onError the callback when an error occurs
         */
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

        fun createProject(
            project: Project,
            token: String,
            onSuccess: suspend (Project) -> Unit,
            onError: suspend (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService.createProject(token, project)
                    onSuccess(response.body()!!)
                } catch (e: Exception) {
                    onError(e)
                }
            }

        }

    }


}