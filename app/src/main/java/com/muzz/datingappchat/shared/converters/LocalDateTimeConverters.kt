package com.muzz.datingappchat.shared.converters

import androidx.room3.ColumnTypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A simple converter to allow LocalDateTime to exist in Room Database inside of Message.
 */
class LocalDateTimeConverters {
    private val fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    @ColumnTypeConverter
    fun fromString(v: String): LocalDateTime = LocalDateTime.parse(v, fmt)
    @ColumnTypeConverter
    fun toDateString(d: LocalDateTime): String = d.format(fmt)
}