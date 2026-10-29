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
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
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

@PublishedApi
internal const val API_LOG_KEY = "ApiRequestExt"

/**
 * Executes a Ktor request and wraps the result in [AppResult].
 *
 * [ERROR] is the endpoint-specific API error enum used to decode backend `status` values.
 * For example, auth requests should pass [ApiError.Authentication] so a backend status like
 * `PASSWORD_DOES_NOT_MATCH` maps to that auth error.
 *
 * The returned error type is still [ApiError], not [ERROR], because every endpoint can also fail
 * with shared network errors such as no internet, serialization failure, unauthorized, or server
 * errors. In short: [ERROR] controls backend status mapping, while [AppResult] can still carry
 * either endpoint-specific errors or [ApiError.Network] failures.
 */
suspend inline fun <reified Req, reified Res, reified ERROR> HttpClient.req(
    route: String,
    type: ApiRequestType,
    params: List<Pair<String, String>> = emptyList(),
    body: Req? = null,
): AppResult<Res, ApiError> where ERROR : Enum<ERROR>, ERROR : ApiError {
    var requestCurl: String? = null

    return try {
        val url = route.toUrlString()

        val buildRequest: HttpRequestBuilder.() -> Unit = {
            params.forEach { (key, value) -> parameter(key, value) }
            body?.let { setBody(it) }

            requestCurl = toCurl(
                method = type.name,
                url = this.url.buildString(),
                headers = headers.build(),
                bodyJson = body?.let { curlJson.encodeToString(it) },
            )
        }

        val response = when (type) {
            ApiRequestType.GET -> this.get(urlString = url, block = buildRequest)
            ApiRequestType.POST -> this.post(urlString = url, block = buildRequest)
            ApiRequestType.PUT -> this.put(urlString = url, block = buildRequest)
            ApiRequestType.DELETE -> this.delete(urlString = url, block = buildRequest)
        }

        // Final request carries headers added by client plugins (default request, auth).
        requestCurl = toCurl(
            method = response.call.request.method.value,
            url = response.call.request.url.toString(),
            headers = response.call.request.headers,
            bodyJson = body?.let { curlJson.encodeToString(it) },
        )

        response.toApiResult<Res, ERROR>()
    } catch (e: Exception) {
        handleException(e)
    }.also {
        requestCurl?.let { curl -> Log.d(API_LOG_KEY, curl) }
        Log.d(API_LOG_KEY, it.toString())
    }
}


/**
 * Public inline functions cannot use non-public API, since if they are inlined, those non-public API references would violate access restrictions at a call site (see restrictions for public API inline functions).
 *
 * To overcome this restriction an internal declaration can be annotated with the @PublishedApi annotation:
 * this allows calling that declaration from public inline functions;
 * the declaration becomes effectively public, and this should be considered with respect to binary compatibility maintaining.
 */

@PublishedApi
internal fun String.toUrlString() = SharedConfig.BASE_URL + this

/**
 * Compact json so the logged curl stays on a single line.
 */
@PublishedApi
internal val curlJson = Json(PlatformHttpClient.json) { prettyPrint = false }

/**
 * Renders the request as a single line curl command that can be imported into Postman.
 */
@PublishedApi
internal fun toCurl(
    method: String,
    url: String,
    headers: Headers,
    bodyJson: String?,
): String = buildString {
    append("curl -X $method '${url.escapeCurl()}'")
    headers.entries()
        .filterNot { (key, _) -> key.equals(HttpHeaders.ContentType, ignoreCase = true) }
        .forEach { (key, values) ->
            values.filter { it.isValidHeaderValue(key) }
                .forEach { append(" -H '$key: ${it.escapeCurl()}'") }
        }
    if (bodyJson != null) {
        append(" -H '${HttpHeaders.ContentType}: ${ContentType.Application.Json}'")
        append(" --data-raw '${bodyJson.escapeCurl()}'")
    }
}

/**
 * Skips blank header values and auth headers that carry only a scheme without credentials
 * (e.g. `Bearer ` with no token).
 */
@PublishedApi
internal fun String.isValidHeaderValue(key: String): Boolean {
    if (isBlank()) return false
    if (!key.equals(HttpHeaders.Authorization, ignoreCase = true)) return true

    return trim().substringAfter(' ', missingDelimiterValue = "").isNotBlank()
}

@PublishedApi
internal fun String.escapeCurl() = replace("'", "'\\''")

/**
 * Converts a gateway response envelope into [AppResult].
 *
 * The gateway wraps both success and error responses as [ApiResponse]. We decode the envelope
 * using [JsonElement] first so error payloads do not have to match an endpoint success type.
 */
@PublishedApi
internal suspend inline fun <reified Res, reified ERROR> HttpResponse.toApiResult(): AppResult<Res, ApiError> where ERROR : Enum<ERROR>, ERROR : ApiError =
    try {
        val apiResponse = body<ApiResponse<JsonElement>>()

        if (status.value in 200..299)
            apiResponse.toSuccessResult<Res>(status.value)
        else apiResponse.toApiErrorResult<ERROR>(status.value)
    } catch (e: CancellationException) {
        throw e
    } catch (e: SerializationException) {
        if (status.value in 200..299) {
            AppResult.Error(
                cause = e,
                error = ApiError.Network.SERIALISATION.toErrorResponse(
                    e.message,
                    code = status.value
                )
            )
        } else toNetworkErrorResult()
    } catch (e: Exception) {
        if (status.value in 200..299) {
            AppResult.Error(
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
): AppResult<Res, ApiError> {
    val payload = payload

    if (payload == null || payload is JsonNull) {
        return if (Res::class == Unit::class) {
            @Suppress("UNCHECKED_CAST")
            AppResult.Success(Unit as Res)
        } else {
            AppResult.Error(
                error = ApiError.Network.SERIALISATION.toErrorResponse(
                    message = "Response payload missing",
                    code = resolvedCode(responseCode)
                )
            )
        }
    }

    return try {
        AppResult.Success(PlatformHttpClient.json.decodeFromJsonElement<Res>(payload))
    } catch (e: SerializationException) {
        AppResult.Error(
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
): AppResult.Error<ApiError> where ERROR : Enum<ERROR>, ERROR : ApiError =
    AppResult.Error(
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
internal fun HttpResponse.toNetworkErrorResult(): AppResult.Error<ApiError> {
    val error = status.value.toNetworkError()

    return AppResult.Error(
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
): AppResult.Error<ApiError> = when (exception) {
    is UnresolvedAddressException -> AppResult.Error(
        cause = exception,
        error = ApiError.Network.NO_INTERNET.toErrorResponse(exception.message, -1)
    )

    is SerializationException -> AppResult.Error(
        cause = exception,
        error = ApiError.Network.SERIALISATION.toErrorResponse(exception.message, -1)
    )

    is CancellationException -> throw exception

    else -> AppResult.Error(
        cause = exception,
        error = ApiError.Network.SOMETHING_WENT_WRONG.toErrorResponse(exception.message, -1)
    )
}
