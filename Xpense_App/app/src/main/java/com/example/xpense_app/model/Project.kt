package com.example.xpense_app.model

import com.google.gson.annotations.SerializedName

data class Project(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("releaseDate") val releaseDate: String?,
    @SerializedName("expectedExpense") val expectedExpense: Long?,
    @SerializedName("currentExpense") val currentExpense: Long?,
)