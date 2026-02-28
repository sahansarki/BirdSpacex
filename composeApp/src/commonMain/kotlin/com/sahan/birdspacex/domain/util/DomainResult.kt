package com.sahan.birdspacex.domain.util

sealed interface DomainResult<out T> {
    data class Success<T>(
        val data: T,
        val fromCache: Boolean = false,
    ) : DomainResult<T>

    data class Error(
        val error: NetworkError,
        val fallbackDataAvailable: Boolean = false,
    ) : DomainResult<Nothing>
}
