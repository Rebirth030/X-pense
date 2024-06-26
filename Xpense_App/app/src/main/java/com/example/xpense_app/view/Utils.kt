package com.example.xpense_app.view


import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * Parses a date time string into a LocalDateTime object.
 *
 * @param dateTime The date time string to parse.
 * @return The parsed LocalDateTime object.
 */
fun parseDateTime(dateTime: String?): LocalDateTime {
    return if (dateTime != null) {
        ZonedDateTime.parse(dateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
    } else {
        LocalDateTime.now()
    }
}