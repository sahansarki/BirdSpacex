package com.sahan.birdspacex

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform