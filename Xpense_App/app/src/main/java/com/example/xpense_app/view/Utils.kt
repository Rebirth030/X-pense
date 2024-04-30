package com.example.xpense_app.view

import com.example.xpense_app.view.overview.INPUT_DATE_TIME_FORMAT
import java.time.LocalDateTime

fun parseDateTime(dateTime: String?): LocalDateTime {
    return if (dateTime != null) {
        LocalDateTime.parse(dateTime, INPUT_DATE_TIME_FORMAT)
    } else {
        LocalDateTime.now()
    }
}