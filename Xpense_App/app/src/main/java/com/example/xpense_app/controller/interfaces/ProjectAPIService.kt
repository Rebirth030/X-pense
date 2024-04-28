package com.example.xpense_app.controller.interfaces

import com.example.xpense_app.controller.service.ExpenseAPIService
import com.example.xpense_app.model.Project
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProjectAPIService: APIService {


    @GET("projects")
    suspend fun getProjects(): Response<List<Project>>

    @POST("projects")
    suspend fun createExpense(@Header("Authorization") token: String, @Body project: Project): Project

    @PUT("projects/{id}")
    suspend fun updateExpense(@Header("Authorization") token: String, @Path("id") id: Int, @Body project: Project): Project

    @DELETE("projects/{id}")
    suspend fun deleteExpense(@Header("Authorization") token: String, @Path("id") id: Int): Response<Unit>

}