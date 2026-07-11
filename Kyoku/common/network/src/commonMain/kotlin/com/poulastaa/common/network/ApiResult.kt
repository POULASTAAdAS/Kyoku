package com.poulastaa.common.network

import com.poulastaa.common.network.Error as Err

data class ErrorResponse<out E : Err>(
    val error: E,
    val code: Int = -1,
    val message: String = "",
    val t: Throwable? = null,
)

sealed interface ApiResult<out RESPONSE, out ERROR : Err> {
    data class Success<out RESPONSE>(val response: RESPONSE) : ApiResult<RESPONSE, Nothing>
    data class Error<out ERROR : Err>(
        val cause: Throwable? = null,
        val error: ErrorResponse<ERROR>,
    ) : ApiResult<Nothing, ERROR>
}

typealias EmptyResponse<ERROR> = ApiResult<Unit, ERROR>

fun <RESULT, ERROR : Err> Result<RESULT>.toApiResponse(
    message: String,
    code: Int,
    error: ERROR,
) = fold(
    onSuccess = { ApiResult.Success(it) },
    onFailure = {
        ApiResult.Error(
            cause = it,
            error = ErrorResponse(
                error = error,
                code = code,
                message = message
            )
        )
    }
)

inline fun <SUCCESS, RESULT, ERROR : Err> ApiResult<SUCCESS, ERROR>.map(
    onSuccess: (SUCCESS) -> RESULT,
    onFailure: (error: ErrorResponse<ERROR>, cause: Throwable?) -> RESULT,
) = when (this) {
    is ApiResult.Success -> onSuccess(response)
    is ApiResult.Error -> onFailure(error, cause)
}

inline fun <SUCCESS, RESULT, ERROR : Err> ApiResult<SUCCESS, ERROR>.map(map: (SUCCESS) -> RESULT) =
    when (this) {
        is ApiResult.Success -> ApiResult.Success(map(response))
        is ApiResult.Error -> ApiResult.Error(
            cause = cause,
            error = error
        )
    }

fun <RESPONSE, ERROR : Err> ApiResult<RESPONSE, ERROR>.asEmptyResponse(): EmptyResponse<ERROR> =
    map {}