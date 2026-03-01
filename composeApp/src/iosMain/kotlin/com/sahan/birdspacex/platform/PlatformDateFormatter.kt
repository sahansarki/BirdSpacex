package com.sahan.birdspacex.platform

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

actual class PlatformDateFormatter {
    actual fun formatLaunchListDate(isoUtc: String): String {
        return runCatching {
            val date = Instant.parse(isoUtc).toLocalDateTime(TimeZone.currentSystemDefault()).date
            "${monthLabel(date.monthNumber)} ${date.day}, ${date.year}"
        }.getOrElse { isoUtc.ifBlank { "-" } }
    }

    actual fun formatLaunchDetailDateTime(isoUtc: String): String {
        return runCatching {
            val dateTime = Instant.parse(isoUtc).toLocalDateTime(TimeZone.currentSystemDefault())
            "${monthLabel(dateTime.monthNumber)} ${dateTime.day}, ${dateTime.year} ${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}"
        }.getOrElse { isoUtc.ifBlank { "-" } }
    }

    private fun monthLabel(monthNumber: Int): String {
        return when (monthNumber) {
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> "--"
        }
    }
}