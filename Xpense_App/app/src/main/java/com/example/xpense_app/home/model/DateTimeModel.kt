package com.example.xpense_app.home.model

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

class DateTimeModel {
    private var timestamp: Long
    private var localDateTime: LocalDateTime = LocalDateTime.MIN

    constructor(timestamp: Long) {
        this.timestamp = timestamp
        val instant = Instant.ofEpochMilli(timestamp)
        this.localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    fun setDate(timestamp: Long) {
        this.timestamp = timestamp
        val instant = Instant.ofEpochMilli(timestamp)
        this.localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    }

    fun addTimeToDate(time: LocalTime) {
        this.localDateTime =
            LocalDateTime.of(
                this.localDateTime.year,
                this.localDateTime.month,
                this.localDateTime.dayOfMonth,
                time.hour,
                time.minute,
                time.second
            )
    }

    fun getLocalDateTime(): LocalDateTime {
        return this.localDateTime
    }

    fun getTimeStamp(): Long {
        return this.timestamp
    }

    fun getISODate(time: String): String? {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = Date(this.timestamp)
            return "${sdf.format(date)}T${time}Z"
        } catch (e: Exception) {
            return "Error while creating ISO date"
        }
    }

    fun isChanged(): Boolean {
        return this.timestamp !== 0L
    }
}