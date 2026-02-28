package com.sahan.birdspacex.platform

import android.content.Intent
import com.sahan.birdspacex.AndroidContextHolder
import androidx.core.net.toUri

actual class PlatformUrlOpener {
    actual fun open(url: String) {
        val context = AndroidContextHolder.appContext
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}