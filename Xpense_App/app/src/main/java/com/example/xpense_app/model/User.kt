package com.example.xpense_app.model

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id") val id: Int?,
    @SerializedName("prename") val prename: String,
    @SerializedName("lastname") val lastname: String,
    @SerializedName("email") val email: String,
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("country") val country: String,
    @SerializedName("language") val language: String,
    @SerializedName("weeklyWorkingHours") val weeklyWorkingHours: Int?,
    @SerializedName("holidayWorkingSchedule") val holidayWorkingSchedule: Int?
)