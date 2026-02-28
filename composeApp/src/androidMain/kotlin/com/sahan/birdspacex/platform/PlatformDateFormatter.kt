package com.sahan.birdspacex.platform

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.time.Instant

actual class PlatformDateFormatter {
    actual fun formatLaunchListDate(isoUtc: String): String {
        return formatIsoUtc(
            isoUtc = isoUtc,
            pattern = "MMM dd, yyyy",
            fallback = "-",
        )
    }

    actual fun formatLaunchDetailDateTime(isoUtc: String): String {
        return formatIsoUtc(
            isoUtc = isoUtc,
            pattern = "MMM dd, yyyy HH:mm",
            fallback = "-",
        )
    }

    private fun formatIsoUtc(
        isoUtc: String,
        pattern: String,
        fallback: String,
    ): String {
        return runCatching {
            val epochMillis = Instant.parse(isoUtc).toEpochMilliseconds()
            val formatter = SimpleDateFormat(pattern, Locale.getDefault()).apply {
                timeZone = TimeZone.getDefault()
            }
            formatter.format(Date(epochMillis))
        }.getOrElse { isoUtc.ifBlank { fallback } }
    }
}
