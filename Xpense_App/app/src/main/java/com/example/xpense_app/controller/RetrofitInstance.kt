package com.example.xpense_app.controller

import com.example.xpense_app.controller.interfaces.APIService
import com.example.xpense_app.controller.interfaces.AuthenticationAPIService
import com.example.xpense_app.controller.interfaces.ProjectAPIService
import com.example.xpense_app.controller.interfaces.UserAPIService
import com.example.xpense_app.controller.service.ExpenseAPIService
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import kotlin.reflect.KClass
import kotlin.reflect.KType

object RetrofitInstance {
    private const val BASE_URL_API = "http://10.0.2.2:8080"

    private val json = GsonBuilder()
        .setLenient()
        .create()

    private var apiService = Retrofit.Builder()
        .baseUrl(BASE_URL_API)
        .addConverterFactory(GsonConverterFactory.create(json))
        .build()

    private var projectAPIService = apiService.create(ProjectAPIService::class.java)
    private var expenseAPIService = apiService.create(ExpenseAPIService::class.java)
    private var userAPIService = apiService.create(UserAPIService::class.java)
    private var authenticationAPIService = apiService.create(AuthenticationAPIService::class.java)

    fun <T: Any> getAPIService (kType: KClass<T>): APIService {
        return when (kType) {
            ProjectAPIService::class -> projectAPIService
            ExpenseAPIService::class -> expenseAPIService
            UserAPIService::class -> userAPIService
            AuthenticationAPIService::class -> authenticationAPIService
            else -> throw IllegalArgumentException("Invalid API Service")
        }
    }
}