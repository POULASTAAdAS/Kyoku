package com.poulastaa.common.network

import com.poulastaa.common.network.model.ApiErrorResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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

        val response = when (type) {
            ApiRequestType.GET -> this.get(urlString = url) {
                params.takeIf { it.isNotEmpty() }?.forEach { (key, value) ->
                    parameter(key, value)
                }
            }

            ApiRequestType.POST -> this.post(urlString = url) {
                params.takeIf { it.isNotEmpty() }?.forEach { (key, value) ->
                    parameter(key, value)
                }
                body?.let { setBody(it) }
            }

            ApiRequestType.PUT -> this.put(urlString = url) {
                params.takeIf { it.isNotEmpty() }?.forEach { (key, value) ->
                    parameter(key, value)
                }
                body?.let { setBody(it) }
            }

            ApiRequestType.DELETE -> this.delete(urlString = url) {
                params.takeIf { it.isNotEmpty() }?.forEach { (key, value) ->
                    parameter(key, value)
                }
                body?.let { setBody(it) }
            }
        }

        when (response.status.value) {
            in 200..299 -> try {
                ApiResult.Success(response.body<Res>())
            } catch (_: Exception) {
                try {
                    val errorResponse = response.body<ApiErrorResponse>()
                    val cause = ApiError.Network.valueOf(errorResponse.status.uppercase())
                    ApiResult.Error(
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

fun String.toUrlString() = this


fun handleException(
    exception: Exception,
) = when (exception) {
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