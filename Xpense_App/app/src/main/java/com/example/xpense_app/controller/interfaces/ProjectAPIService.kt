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
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

//const val BASE_URL_API = "http://10.0.2.2:8080/"

interface ProjectAPIService {


    @GET("projects")
    suspend fun getProjects(): Response<List<Project>>

    @POST("projects")
    suspend fun createExpense(@Body project: Project): Project

    @PUT("projects/{id}")
    suspend fun updateExpense(@Path("id") id: Int, @Body project: Project): Project

    @DELETE("projects/{id}")
    suspend fun deleteExpense(@Path("id") id: Int): Response<Unit>

    /**
     * Creates API Service Calls
     */
    companion object {
        private val json = GsonBuilder()
            .setLenient()
            .create()

        var apiService: ProjectAPIService? = null
        fun getInstance(): ProjectAPIService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL_API)
                    .addConverterFactory(GsonConverterFactory.create(json))
                    .build().create(ProjectAPIService::class.java)
            }
            return apiService!!
        }
    }
}