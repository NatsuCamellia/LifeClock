package net.natsucamellia.lifeclock.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class DateUtils {
    fun convertMillisToLocalDate(millis: Long): LocalDate {
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
}