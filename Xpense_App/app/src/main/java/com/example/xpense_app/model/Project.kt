package com.example.xpense_app.model

import com.google.gson.annotations.SerializedName

data class Project(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") var name: String?,
    @SerializedName("description") var description: String?,
    @SerializedName("releaseDate") val releaseDate: String?,
    @SerializedName("expectedExpense") val expectedExpense: Long?,
    @SerializedName("currentExpense") val currentExpense: Long?,
    @SerializedName("companyId") val companyId: Long?,
    @SerializedName("userId") val userId: Long?
)