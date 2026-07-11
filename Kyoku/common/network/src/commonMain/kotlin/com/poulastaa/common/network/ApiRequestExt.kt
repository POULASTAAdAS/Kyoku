package com.poulastaa.common.network

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
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlin.coroutines.cancellation.CancellationException

enum class ApiRequestType {
    GET,
    POST,
    PUT,
    DELETE
}

suspend inline fun <reified Req, reified Res> HttpClient.req(
    route: String,
    type: ApiRequestType,
    params: List<Pair<String, String>> = emptyList(),
    body: Req? = null,
): ApiResult<Res, Error> {
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
                try {
                    val errorResponse = response.body<ApiErrorResponse>()
                    val cause = ApiError.Network.valueOf(errorResponse.status.uppercase())
                    ApiResult.Error(
                        cause = null,
                        error = cause.toErrorResponse(
                            errorResponse.message,
                            errorResponse.code
                        )
                    )
                } catch (e: Exception) {
                    handleException(e)
                }
            }

            401 -> ApiResult.Error(
                cause = null,
                error = ApiError.Authentication.UNAUTHORIZED.toErrorResponse(
                    response.status.description,
                    response.status.value
                )
            )

            404 -> ApiResult.Error(
                cause = null,
                error = ApiError.Network.NOT_FOUND.toErrorResponse(
                    response.status.description,
                    response.status.value
                )
            )

            in 500..599 -> ApiResult.Error(
                cause = null,
                error = ApiError.Network.SERVER_ERROR.toErrorResponse(
                    response.status.description,
                    response.status.value
                )
            )

            else -> ApiResult.Error(
                cause = null,
                error = ApiError.Network.SOMETHING_WENT_WRONG.toErrorResponse(
                    response.status.description,
                    response.status.value
                )
            )
        }
    } catch (e: Exception) {
        handleException(e)
    }
}

@PublishedApi
internal fun String.toUrlString() = this

@PublishedApi
internal fun handleException(
    exception: Exception,
): ApiResult.Error<Error> = when (exception) {
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