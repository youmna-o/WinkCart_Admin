package com.example.winkcart_admin.utils

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun dateToIso8601(year: Int, month: Int, day: Int, hour: Int, minute: Int): String {
    val localDateTime = LocalDateTime.of(year, month, day, hour, minute)
    val zoned = ZonedDateTime.of(localDateTime, ZoneOffset.UTC)
    return zoned.format(DateTimeFormatter.ISO_DATE_TIME)
}