package com.example.xpense_app.controller

import com.example.xpense_app.controller.interfaces.APIService
import com.example.xpense_app.controller.interfaces.AuthenticationAPIService
import com.example.xpense_app.controller.interfaces.ProjectAPIService
import com.example.xpense_app.controller.interfaces.UserAPIService
import com.example.xpense_app.controller.interfaces.ExpenseAPIService
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import kotlin.reflect.KClass

object RetrofitInstance {
    private var baseURLApi = "http://10.0.2.2:8080"

    private val json = GsonBuilder()
        .setLenient()
        .create()

    private var apiService = Retrofit.Builder()
        .baseUrl(baseURLApi)
        .addConverterFactory(GsonConverterFactory.create(json))
        .build()

    private var projectAPIService = apiService.create(ProjectAPIService::class.java)
    private var expenseAPIService = apiService.create(ExpenseAPIService::class.java)
    private var userAPIService = apiService.create(UserAPIService::class.java)
    private var authenticationAPIService = apiService.create(AuthenticationAPIService::class.java)

    /**
     * Get the API service based on the type.
     *
     * @param kType the type of the API service
     * @return the API service
     */
    fun <T: Any> getAPIService (kType: KClass<T>): APIService {
        return when (kType) {
            ProjectAPIService::class -> projectAPIService
            ExpenseAPIService::class -> expenseAPIService
            UserAPIService::class -> userAPIService
            AuthenticationAPIService::class -> authenticationAPIService
            else -> throw IllegalArgumentException("Invalid API Service")
        }
    }

    fun setURL(ip: String) {
        baseURLApi = "http://$ip"
        apiService = Retrofit.Builder()
            .baseUrl(baseURLApi)
            .addConverterFactory(GsonConverterFactory.create(json))
            .build()

        projectAPIService = apiService.create(ProjectAPIService::class.java)
        expenseAPIService = apiService.create(ExpenseAPIService::class.java)
        userAPIService = apiService.create(UserAPIService::class.java)
        authenticationAPIService = apiService.create(AuthenticationAPIService::class.java)
    }

    interface ConnectivityCheckService {
        @GET("/auth/connectivity-check")
        fun checkConnection(): Call<Void>
    }

    fun testConnection(ip: String): Boolean {
        if (baseURLApi != "http://$ip") {
            setURL(ip)
        }
        val service = apiService.create(ConnectivityCheckService::class.java)
        return try {
            val response = service.checkConnection().execute()
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}