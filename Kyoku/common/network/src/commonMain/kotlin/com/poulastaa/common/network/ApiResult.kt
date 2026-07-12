package com.poulastaa.common.network

import kotlin.jvm.JvmName
import com.poulastaa.common.network.Error as Err

/**
 * Standard error payload carried by [ApiResult.Error].
 *
 * [error] is the typed app error. [code] and [message] usually come from the backend or HTTP
 * response. [t] is available for lower-level failures that need to preserve a throwable.
 */
data class ErrorResponse<out E : Err>(
    val error: E,
    val code: Int = -1,
    val message: String = "",
    val t: Throwable? = null,
)

/**
 * Common success/error wrapper for network and local data operations.
 *
 * Keep data that the ViewModel does not need inside repositories. Repositories can consume a
 * [Success.response] internally and return [EmptyResponse] when the UI only needs success/failure.
 */
sealed interface ApiResult<out RESPONSE, out ERROR : Err> {
    data class Success<out RESPONSE>(val response: RESPONSE) : ApiResult<RESPONSE, Nothing>
    data class Error<out ERROR : Err>(
        val cause: Throwable? = null,
        val error: ErrorResponse<ERROR>,
    ) : ApiResult<Nothing, ERROR>
}

/**
 * Result type for operations that only need to report success or failure.
 */
typealias EmptyResponse<ERROR> = ApiResult<Unit, ERROR>

/**
 * Converts Kotlin [Result] into [ApiResult] using the provided typed [error] for failures.
 */
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

/**
 * Folds [ApiResult] into a plain value while exposing both the typed error payload and cause.
 */
inline fun <SUCCESS, RESULT, ERROR : Err> ApiResult<SUCCESS, ERROR>.map(
    onSuccess: (SUCCESS) -> RESULT,
    onFailure: (error: ErrorResponse<ERROR>, cause: Throwable?) -> RESULT,
) = when (this) {
    is ApiResult.Success -> onSuccess(response)
    is ApiResult.Error -> onFailure(error, cause)
}

/**
 * Maps only the success payload and preserves errors unchanged.
 */
@JvmName("mapResult")
inline fun <SUCCESS, RESULT, ERROR : Err> ApiResult<SUCCESS, ERROR>.map(map: (SUCCESS) -> RESULT) =
    when (this) {
        is ApiResult.Success -> ApiResult.Success(map(response))
        is ApiResult.Error -> ApiResult.Error(
            cause = cause,
            error = error
        )
    }

/**
 * Maps only the success payload for API calls and preserves typed API errors unchanged.
 */
@JvmName("mapApiResult")
inline fun <SUCCESS, RESULT, ERROR : ApiError> ApiResult<SUCCESS, ERROR>.map(map: (SUCCESS) -> RESULT) =
    when (this) {
        is ApiResult.Success -> ApiResult.Success(map(response))
        is ApiResult.Error -> ApiResult.Error(
            cause = cause,
            error = error
        )
    }

/**
 * Drops the success payload and keeps only success/failure information.
 */
fun <RESPONSE, ERROR : Err> ApiResult<RESPONSE, ERROR>.asEmptyResponse(): EmptyResponse<ERROR> =
    map {}
