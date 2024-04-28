package com.example.xpense_app.controller.interfaces

import com.example.xpense_app.model.User
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface AuthenticationAPIService: APIService {
    @Headers("Content-Type: application/json")
    @POST("/auth/login")
    suspend fun loginUser(@Body user: User): User

    @Headers("Content-Type: application/json")
    @POST("/auth/signup")
    suspend fun registerUser(@Body user: User): User
}