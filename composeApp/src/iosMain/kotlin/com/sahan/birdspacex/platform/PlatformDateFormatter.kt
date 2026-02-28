package com.sahan.birdspacex.platform

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

actual class PlatformDateFormatter {
    actual fun formatLaunchListDate(isoUtc: String): String {
        return runCatching {
            val date = Instant.parse(isoUtc).toLocalDateTime(TimeZone.currentSystemDefault()).date
            "${monthLabel(date.monthNumber)} ${date.dayOfMonth}, ${date.year}"
        }.getOrElse { isoUtc.ifBlank { "-" } }
    }

    actual fun formatLaunchDetailDateTime(isoUtc: String): String {
        return runCatching {
            val dateTime = Instant.parse(isoUtc).toLocalDateTime(TimeZone.currentSystemDefault())
            "${monthLabel(dateTime.monthNumber)} ${dateTime.dayOfMonth}, ${dateTime.year} ${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}"
        }.getOrElse { isoUtc.ifBlank { "-" } }
    }

    private fun monthLabel(monthNumber: Int): String {
        return when (monthNumber) {
            1 -> "Ocak"
            2 -> "Şubak"
            3 -> "Mart"
            4 -> "Nisan"
            5 -> "Mayıs"
            6 -> "Haziran"
            7 -> "Temmuz"
            8 -> "Ağustos"
            9 -> "Eylül"
            10 -> "Ekim"
            11 -> "Kasım"
            12 -> "Aralık"
            else -> "--"
        }
    }
}