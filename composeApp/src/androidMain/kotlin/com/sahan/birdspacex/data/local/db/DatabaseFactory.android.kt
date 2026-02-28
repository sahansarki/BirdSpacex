package com.sahan.birdspacex.data.local.db

import android.content.Context
import androidx.room.Room
import com.sahan.birdspacex.AndroidContextHolder

actual class DatabaseFactory actual constructor(
    private val platformContext: Any?,
) {
    actual fun create(): SpaceDatabase {
        val context = (platformContext as? Context)?.applicationContext ?: AndroidContextHolder.appContext
        return Room.databaseBuilder(
            context,
            SpaceDatabase::class.java,
            "space_db",
        ).build()
    }
}
