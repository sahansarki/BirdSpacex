package com.sahan.birdspacex.domain.util

sealed interface NetworkError {
    data object NoInternet : NetworkError
    data object Timeout : NetworkError
    data object Serialization : NetworkError
    data class Server(val code: Int? = null, val message: String? = null) : NetworkError
    data class Unknown(val throwable: Throwable? = null) : NetworkError
}
