package com.example.xpense_app.controller.service

import Expense
import com.example.xpense_app.controller.interfaces.APIService
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

interface ExpenseAPIService: APIService {

    @Headers("Content-Type: application/json")
    @GET("expenses/{id}")
    suspend fun getExpense(@Header("Authorization") token: String, @Path("id") id: Int): Expense

    @GET("expenses/project/{id}")
    suspend fun getExpensesOfProject(@Header("Authorization") token: String, @Path("id") id: Long): List<Expense>

    @GET("expenses")
    suspend fun getExpenses(@Header("Authorization") token: String): Response<List<Expense>>

    @POST("expenses")
    suspend fun createExpense(@Header("Authorization") token: String, @Body expense: Expense): Response<Expense>

    @PUT("expenses/{id}")
    suspend fun updateExpense(@Header("Authorization") token: String, @Path("id") id: Long, @Body expense: Expense): Response<Expense>

    @DELETE("expenses/{id}")
    suspend fun deleteExpense(@Header("Authorization") token: String, @Path("id") id: Int): Response<Unit>
}
