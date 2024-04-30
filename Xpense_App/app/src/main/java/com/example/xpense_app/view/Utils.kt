package com.example.xpense_app.view


import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun parseDateTime(dateTime: String?): LocalDateTime {
    return if (dateTime != null) {
        LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    } else {
        LocalDateTime.now()
    }
}