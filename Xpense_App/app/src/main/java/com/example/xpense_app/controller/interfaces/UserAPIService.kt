package com.example.xpense_app.controller.interfaces

import com.example.xpense_app.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserAPIService: APIService {
    @Headers("Content-Type: application/json")
    @GET("users/{id}")
    suspend fun getUser(@Header("Authorization") token: String, @Path("id") id: Int): User

    @GET("users")
    suspend fun getUsers(@Header("Authorization") token: String): Response<List<User>>


    @PUT("users/{id}")
    suspend fun updateUser(@Header("Authorization") token: String, @Path("id") id: Long, @Body user: User): Response<User>

    @GET("users/getByToken")
    suspend fun getUserByToken(@Header("Authorization") token: String): Response<User>


    /*
    @DELETE("expenses/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Unit>
     */

}