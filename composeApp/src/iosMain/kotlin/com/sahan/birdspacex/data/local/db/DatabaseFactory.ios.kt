package com.sahan.birdspacex.data.local.db

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import platform.Foundation.NSHomeDirectory

actual class DatabaseFactory actual constructor(
    private val platformContext: Any?,
) {
    actual fun create(): SpaceDatabase {
        val dbFile = NSHomeDirectory() + "/space_db.db"
        return Room.databaseBuilder<SpaceDatabase>(
            name = dbFile,
        ).setDriver(
            BundledSQLiteDriver(),
        ).setQueryCoroutineContext(
            Dispatchers.Default,
        ).build()
    }
}
