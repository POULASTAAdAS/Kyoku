package com.poulastaa.core.domain.utils

import com.poulastaa.core.domain.utils.Error as Err

sealed interface Result<out D, out E : Err> {
    data class Success<out D>(val data: D) :
        Result<D, Nothing>

    data class Error<out E : Err>(val error: E) :
        Result<Nothing, E>
}

inline fun <T, E : Err, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Success -> Result.Success(map(data))
        is Result.Error -> Result.Error(error)
    }
}

fun <T, E : Err> Result<T, E>.asEmptyDataResult(): EmptyResult<E> = map {}

typealias EmptyResult<E> = Result<Unit, E>