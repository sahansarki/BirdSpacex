package com.sahan.birdspacex.data.mapper

interface IMapper<T, R> {
    fun map(data: T): R
}
