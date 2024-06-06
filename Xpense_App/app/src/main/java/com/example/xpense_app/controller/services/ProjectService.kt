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

    companion object {
        private val apiService2 =
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
                    val response = apiService2.getProjects(token)
                    onSuccess(response.body().orEmpty())
                } catch (e: Exception) {
                    onError(e)
                }
            }
        }

        /**
         * Create a new project on the server.
         *
         * @param project Project object to be created.
         * @param token Authentication token.
         * @param onSuccess Callback invoked when project is successfully created.
         * @param onError Callback invoked when an error occurs during creation.
         */
        fun createProject(
            project: Project,
            token: String,
            onSuccess: suspend (Project) -> Unit,
            onError: suspend (Exception) -> Unit
        ) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = apiService2.createProject(token, project)
                    onSuccess(response.body()!!)
                } catch (e: Exception) {
                    onError(e)
                }
            }

        }

    }


}