package com.example.xpense_app.controller.interfaces

import com.example.xpense_app.model.User
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

const val BASE_URL_API = "http://10.0.2.2:8080/"

interface UserAPIService {
    @Headers("Content-Type: application/json")
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): User

    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @POST("/auth/signup")
    suspend fun registerUser(@Body user: User): User

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: User): User

    @POST("/auth/login")
    suspend fun loginUser(@Body user: User): User

    /*
    @DELETE("expenses/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
     */

    /**
     * Creates API Service Calls
     */
    companion object {
        private val json = GsonBuilder()
            .setLenient()
            .create()

        var apiService: UserAPIService? = null
        fun getInstance(): UserAPIService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL_API)
                    .addConverterFactory(GsonConverterFactory.create(json))
                    .build().create(UserAPIService::class.java)
            }
            return apiService!!
        }
    }
}