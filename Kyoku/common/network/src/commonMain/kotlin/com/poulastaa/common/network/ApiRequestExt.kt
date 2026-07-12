package com.poulastaa.common.network

import com.poulastaa.common.domain.SharedConfig
import com.poulastaa.common.network.model.ApiErrorResponse
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

        when (response.status.value) {
            in 200..299 -> try {
                ApiResult.Success(response.body<Res>())
            } catch (e: SerializationException) {
                ApiResult.Error(
                    cause = e,
                    error = ApiError.Network.SERIALISATION.toErrorResponse(
                        e.message,
                        code = response.status.value
                    )
                )
            } catch (_: Exception) {
                response.toApiErrorResult<ERROR>()
            }

            else -> response.toApiErrorResult<ERROR>()
        }
    } catch (e: Exception) {
        handleException(e)
    }
}

@PublishedApi
internal fun String.toUrlString() = SharedConfig.BASE_URL + this

/**
 * Converts a non-success HTTP response, or a success response that failed to deserialize as [Res],
 * into an [ApiResult.Error].
 *
 * Mapping order:
 * 1. Try parsing the backend error body as [ApiErrorResponse].
 * 2. Try mapping `status` to the endpoint-specific [ERROR] enum.
 * 3. Fall back to [ApiError.Network] if the status is a shared network status.
 * 4. Fall back to an HTTP-code based [ApiError.Network] if the error body cannot be parsed.
 */
@PublishedApi
internal suspend inline fun <reified ERROR> HttpResponse.toApiErrorResult(): ApiResult.Error<ApiError> where ERROR : Enum<ERROR>, ERROR : ApiError =
    try {
        val errorResponse = body<ApiErrorResponse>()

        ApiResult.Error(
            cause = null,
            error = errorResponse.toApiError<ERROR>().toErrorResponse(
                message = errorResponse.message,
                code = errorResponse.code,
            )
        )
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        val error = status.value.toNetworkError()

        ApiResult.Error(
            cause = null,
            error = error.toErrorResponse(
                message = status.description,
                code = status.value,
            )
        )
    }

/**
 * Maps the backend `status` string to the most specific [ApiError].
 *
 * [ERROR] is checked first so endpoint errors win over generic network errors. If the backend
 * sends a status unknown to that endpoint enum, shared [ApiError.Network] values are checked next.
 */
@PublishedApi
internal inline fun <reified ERROR> ApiErrorResponse.toApiError(): ApiError where ERROR : Enum<ERROR>, ERROR : ApiError {
    val responseStatus = status.uppercase()

    return enumValues<ERROR>().firstOrNull { it.name == responseStatus }
        ?: enumValues<ApiError.Network>().firstOrNull { it.name == responseStatus }
        ?: ApiError.Network.SOMETHING_WENT_WRONG
}

/**
 * Last-resort mapping used when the response body cannot be decoded as [ApiErrorResponse].
 */
@PublishedApi
internal fun Int.toNetworkError() = when (this) {
    401 -> ApiError.Network.UNAUTHORIZED
    404 -> ApiError.Network.NOT_FOUND
    in 500..599 -> ApiError.Network.SERVER_ERROR
    else -> ApiError.Network.SOMETHING_WENT_WRONG
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
