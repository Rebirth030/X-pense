package com.example.xpense_app.controller.interfaces

import com.example.xpense_app.model.Project
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProjectAPIService: APIService {


    @GET("projects")
    suspend fun getProjects(): Response<List<Project>>

    @GET("projects")
    suspend fun getProjects(@Header("Authorization") token: String): Response<List<Project>>

    @POST("projects")
    suspend fun createProject(@Header("Authorization") token: String, @Body project: Project): Project

    @PUT("projects/{id}")
    suspend fun updateProject(@Header("Authorization") token: String, @Path("id") id: Int, @Body project: Project): Project

    @DELETE("projects/{id}")
    suspend fun deleteProject(@Header("Authorization") token: String, @Path("id") id: Int): Response<Unit>

}