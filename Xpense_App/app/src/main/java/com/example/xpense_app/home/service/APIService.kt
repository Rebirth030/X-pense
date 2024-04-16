package com.example.xpense_app.home.service

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
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

const val BASE_URL_API = "http://10.0.2.2:8080/"

interface APIService {

    @Headers("Content-Type: application/json")
    @GET("expenses/{id}")
    suspend fun getExpense(@Path("id") id: Int): Expense

    @GET("expenses")
    suspend fun getExpenses(): Response<List<Expense>>

    @POST("expenses")
    suspend fun createExpense(@Body expense: Expense): Expense

    @PUT("expenses/{id}")
    suspend fun updateExpense(@Path("id") id: Int, @Body expense: Expense): Expense

    @DELETE("expenses/{id}")
    suspend fun deleteExpense(@Path("id") id: Int): Response<Unit>

    /**
     * Creates API Service Calls
     */
    companion object {
        private val json = GsonBuilder()
            .setLenient()
            .create()

        var apiService: APIService? = null
        fun getInstance(): APIService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL_API)
                    .addConverterFactory(GsonConverterFactory.create(json))
                    .build().create(APIService::class.java)
            }
            return apiService!!
        }
    }
}