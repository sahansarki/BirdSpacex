package com.sahan.birdspacex.domain.extension

import com.sahan.birdspacex.domain.util.NetworkError

fun NetworkError.toReadableMessage(): String {
    return when (this) {
        NetworkError.NoInternet -> "Please check your internet connection."
        NetworkError.Timeout -> "An error occurred."
        NetworkError.Serialization -> "An error occurred."
        is NetworkError.Server -> "An error occurred."
        is NetworkError.Unknown -> "An error occurred."
    }
}