package com.sahan.birdspacex.data.remote

import com.sahan.birdspacex.domain.util.DomainResult
import com.sahan.birdspacex.domain.util.NetworkError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

suspend fun <T> safeApiCall(block: suspend () -> T): DomainResult<T> {
    return try {
        DomainResult.Success(data = block())
    } catch (throwable: Throwable) {
        DomainResult.Error(error = throwable.toNetworkError())
    }
}

fun Throwable.toNetworkError(): NetworkError {
    return when (this) {
        is ConnectTimeoutException,
        is SocketTimeoutException,
        -> NetworkError.Timeout

        is IOException -> NetworkError.NoInternet

        is SerializationException,
        is NoTransformationFoundException,
        -> NetworkError.Serialization

        is ServerResponseException -> NetworkError.Server(code = response.status.value, message = message)
        is ClientRequestException -> NetworkError.Server(code = response.status.value, message = message)
        is ResponseException -> NetworkError.Server(code = response.status.value, message = message)
        else -> NetworkError.Unknown(throwable = this)
    }
}