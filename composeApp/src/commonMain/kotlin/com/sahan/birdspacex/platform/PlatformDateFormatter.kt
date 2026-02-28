package com.sahan.birdspacex.platform

expect class PlatformDateFormatter() {
    fun formatLaunchListDate(isoUtc: String): String
    fun formatLaunchDetailDateTime(isoUtc: String): String
}
