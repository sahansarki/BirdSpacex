package com.sahan.birdspacex.data.local.db

expect class DatabaseFactory(platformContext: Any? = null) {
    fun create(): SpaceDatabase
}
