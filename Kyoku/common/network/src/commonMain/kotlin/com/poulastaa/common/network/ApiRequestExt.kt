package com.poulastaa.common.network

import com.poulastaa.common.domain.Log
import com.poulastaa.common.domain.SharedConfig
import com.poulastaa.common.network.model.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.coroutines.cancellation.CancellationException

enum class ApiRequestType {
    GET,
    POST,
    PUT,
    DELETE
}

/**
 * Executes a Ktor request and wraps the result in [ApiResult].
 *
 * [ERROR] is the endpoint-specific API error enum used to decode backend `status` values.
 * For example, auth requests should pass [ApiError.Authentication] so a backend status like
 * `PASSWORD_DOES_NOT_MATCH` maps to that auth error.
 *
 * The returned error type is still [ApiError], not [ERROR], because every endpoint can also fail
 * with shared network errors such as no internet, serialization failure, unauthorized, or server
 * errors. In short: [ERROR] controls backend status mapping, while [ApiResult] can still carry
 * either endpoint-specific errors or [ApiError.Network] failures.
 */
suspend inline fun <reified Req, reified Res, reified ERROR> HttpClient.req(
    route: String,
    type: ApiRequestType,
    params: List<Pair<String, String>> = emptyList(),
    body: Req? = null,
): ApiResult<Res, ApiError> where ERROR : Enum<ERROR>, ERROR : ApiError {
    return try {
        val url = route.toUrlString()

        val buildRequest: HttpRequestBuilder.() -> Unit = {
            params.forEach { (key, value) -> parameter(key, value) }
            body?.let { setBody(it) }
        }

        val response = when (type) {
            ApiRequestType.GET -> this.get(urlString = url, block = buildRequest)
            ApiRequestType.POST -> this.post(urlString = url, block = buildRequest)
            ApiRequestType.PUT -> this.put(urlString = url, block = buildRequest)
            ApiRequestType.DELETE -> this.delete(urlString = url, block = buildRequest)
        }

        response.toApiResult<Res, ERROR>()
    } catch (e: Exception) {
        handleException(e)
    }.also {
        Log.d("ApiRequestExt", it.toString())
    }
}

@PublishedApi
internal fun String.toUrlString() = SharedConfig.BASE_URL + this

/**
 * Converts a gateway response envelope into [ApiResult].
 *
 * The gateway wraps both success and error responses as [ApiResponse]. We decode the envelope
 * using [JsonElement] first so error payloads do not have to match an endpoint success type.
 */
@PublishedApi
internal suspend inline fun <reified Res, reified ERROR> HttpResponse.toApiResult(): ApiResult<Res, ApiError> where ERROR : Enum<ERROR>, ERROR : ApiError =
    try {
        val apiResponse = body<ApiResponse<JsonElement>>()

        if (status.value in 200..299)
            apiResponse.toSuccessResult<Res>(status.value)
        else apiResponse.toApiErrorResult<ERROR>(status.value)
    } catch (e: CancellationException) {
        throw e
    } catch (e: SerializationException) {
        if (status.value in 200..299) {
            ApiResult.Error(
                cause = e,
                error = ApiError.Network.SERIALISATION.toErrorResponse(
                    e.message,
                    code = status.value
                )
            )
        } else toNetworkErrorResult()
    } catch (e: Exception) {
        if (status.value in 200..299) {
            ApiResult.Error(
                cause = e,
                error = ApiError.Network.SERIALISATION.toErrorResponse(
                    e.message,
                    code = status.value
                )
            )
        } else toNetworkErrorResult()
    }

@PublishedApi
internal inline fun <reified Res> ApiResponse<JsonElement>.toSuccessResult(
    responseCode: Int,
): ApiResult<Res, ApiError> {
    val payload = payload

    if (payload == null || payload is JsonNull) {
        return if (Res::class == Unit::class) {
            @Suppress("UNCHECKED_CAST")
            ApiResult.Success(Unit as Res)
        } else {
            ApiResult.Error(
                error = ApiError.Network.SERIALISATION.toErrorResponse(
                    message = "Response payload missing",
                    code = resolvedCode(responseCode)
                )
            )
        }
    }

    return try {
        ApiResult.Success(PlatformHttpClient.json.decodeFromJsonElement<Res>(payload))
    } catch (e: SerializationException) {
        ApiResult.Error(
            cause = e,
            error = ApiError.Network.SERIALISATION.toErrorResponse(
                e.message,
                code = resolvedCode(responseCode)
            )
        )
    }
}

@PublishedApi
internal inline fun <reified ERROR> ApiResponse<*>.toApiErrorResult(
    responseCode: Int,
): ApiResult.Error<ApiError> where ERROR : Enum<ERROR>, ERROR : ApiError =
    ApiResult.Error(
        cause = null,
        error = toApiError<ERROR>().toErrorResponse(
            message = message,
            code = resolvedCode(responseCode),
        )
    )

/**
 * Maps the backend `status` string to the most specific [ApiError].
 *
 * [ERROR] is checked first so endpoint errors win over generic network errors. If the backend
 * sends a status unknown to that endpoint enum, shared [ApiError.Network] values are checked next.
 */
@PublishedApi
internal inline fun <reified ERROR> ApiResponse<*>.toApiError(): ApiError where ERROR : Enum<ERROR>, ERROR : ApiError {
    val responseStatus = status.toApiErrorStatus()

    return enumValues<ERROR>().firstOrNull { it.name == responseStatus }
        ?: enumValues<ApiError.Network>().firstOrNull { it.name == responseStatus }
        ?: ApiError.Network.SOMETHING_WENT_WRONG
}

@PublishedApi
internal fun ApiResponse<*>.resolvedCode(
    responseCode: Int,
) = code.takeIf { it > 0 } ?: responseCode

@PublishedApi
internal fun String.toApiErrorStatus() = when (uppercase()) {
    "EMAIL_NOT_VALID" -> ApiError.Authentication.INVALID_EMAIL.name
    "USER_NOT_FOUND" -> ApiError.Authentication.ACCOUNT_NOT_FOUND.name
    "NO_CONTENT" -> ApiError.Network.NOT_FOUND.name
    "SERVICE_UNAVAILABLE",
    "INTERNAL_SERVER_ERROR",
        -> ApiError.Network.SERVER_ERROR.name

    "INVALID_REQUEST_BODY",
    "METHOD_NOT_ALLOWED",
        -> ApiError.Network.SOMETHING_WENT_WRONG.name

    else -> uppercase()
}

/**
 * Last-resort mapping used when the response body cannot be decoded as [ApiResponse].
 */
@PublishedApi
internal fun Int.toNetworkError() = when (this) {
    401 -> ApiError.Network.UNAUTHORIZED
    404 -> ApiError.Network.NOT_FOUND
    429 -> ApiError.Network.RATE_LIMITED
    in 500..599 -> ApiError.Network.SERVER_ERROR
    else -> ApiError.Network.SOMETHING_WENT_WRONG
}

@PublishedApi
internal fun HttpResponse.toNetworkErrorResult(): ApiResult.Error<ApiError> {
    val error = status.value.toNetworkError()

    return ApiResult.Error(
        cause = null,
        error = error.toErrorResponse(
            message = status.description,
            code = status.value,
        )
    )
}

/**
 * Converts request-level failures into shared network errors.
 *
 * Cancellation is rethrown so coroutine cancellation still behaves correctly.
 */
@PublishedApi
internal fun handleException(
    exception: Exception,
): ApiResult.Error<ApiError> = when (exception) {
    is UnresolvedAddressException -> ApiResult.Error(
        cause = exception,
        error = ApiError.Network.NO_INTERNET.toErrorResponse(exception.message, -1)
    )

    is SerializationException -> ApiResult.Error(
        cause = exception,
        error = ApiError.Network.SERIALISATION.toErrorResponse(exception.message, -1)
    )

    is CancellationException -> throw exception

    else -> ApiResult.Error(
        cause = exception,
        error = ApiError.Network.SOMETHING_WENT_WRONG.toErrorResponse(exception.message, -1)
    )
}
