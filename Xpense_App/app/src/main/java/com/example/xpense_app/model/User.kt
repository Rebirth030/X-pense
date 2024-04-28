package com.example.xpense_app.model

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id") val id: Int? = null,
    @SerializedName("prename") val prename: String = "",
    @SerializedName("lastname") val lastname: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("country") val country: String = "Deutschland",
    @SerializedName("language") val language: String = "DE",
    @SerializedName("weeklyWorkingHours") val weeklyWorkingHours: Int? = null,
    @SerializedName("holidayWorkingSchedule") val holidayWorkingSchedule: Int? = null,
    @SerializedName("role") val role: String = "EMPLOYEE",
    @SerializedName("token") var token: String = ""
)