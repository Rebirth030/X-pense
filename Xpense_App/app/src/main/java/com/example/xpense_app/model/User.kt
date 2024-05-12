package com.example.xpense_app.model

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id") val id: Long? = null,
    @SerializedName("prename") val prename: String = "",
    @SerializedName("lastname") val lastname: String = "",
    @SerializedName("email") val email: String = "",
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("country") val country: String = "Deutschland",
    @SerializedName("language") val language: String = "DE",
    @SerializedName("weeklyWorkingHours") var weeklyWorkingHours: Int? = 40,
    @SerializedName("holidayWorkingSchedule") val holidayWorkingSchedule: Int? = null,
    @SerializedName("role") var role: String = "EMPLOYEE",
    @SerializedName("token") var token: String = "",
    @SerializedName("forcedBreakAfter") var forcedBreakAfter: Double? = 4.0,
    @SerializedName("forcedBreakAfterOn") var forcedBreakAfterOn: Boolean? = true,
    @SerializedName("forcedEndAfter") var forcedEndAfter: Double? = 8.0,
    @SerializedName("forcedEndAfterOn") var forcedEndAfterOn: Boolean? = true,
    @SerializedName("notification") var notification: Boolean? = false,
)