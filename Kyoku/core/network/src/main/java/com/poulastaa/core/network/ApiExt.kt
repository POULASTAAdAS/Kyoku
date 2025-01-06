package com.poulastaa.core.network

import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import okhttp3.Call
import okhttp3.Callback
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.nio.channels.UnresolvedAddressException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

val mediaType = "application/json; charset=utf-8".toMediaType()

suspend inline fun <reified Req : Any, reified Response : Any> OkHttpClient.req(
    route: String,
    method: ApiMethodType,
    body: Req? = null,
    params: List<ReqParam>? = null,
    gson: Gson,
): Result<Response, DataError.Network> {
    val urlBuilder = constructRoute(route).toHttpUrlOrNull()?.newBuilder()
        ?: return Result.Error(DataError.Network.UNKNOWN)

    params?.forEach { urlBuilder.addQueryParameter(it.key, it.value) }
    val url = urlBuilder.build()

    val reqBody = body?.let { gson.toJson(it).toRequestBody(mediaType) }
    val reqBuilder = Request.Builder().url(url)
    when (method) {
        ApiMethodType.GET -> reqBuilder.get()
        ApiMethodType.POST -> reqBody?.let { reqBuilder.post(it) }
        ApiMethodType.PUT -> reqBody?.let { reqBuilder.put(it) }
        ApiMethodType.DELETE -> TODO("add delete request")
    }

    return try {
        val response = makeCall(reqBuilder.build())
        responseToResult<Response>(response, gson)
    } catch (e: Exception) {
        handleOtherException(e)
    }
}

fun constructRoute(route: String) = "${BuildConfig.BASE_URL}$route"

suspend fun OkHttpClient.makeCall(request: Request): Response {
    return suspendCoroutine { continuation ->
        try {
            newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    continuation.resume(response)
                }
            })
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }
}

suspend inline fun <reified T> responseToResult(
    response: Response,
    gson: Gson,
): Result<T, DataError.Network> = withContext(Dispatchers.IO) {
    when (response.code) {
        in 200..299 -> {
            val body = response.body!!.string()
            val obj = gson.fromJson(body, T::class.java)

            Result.Success(obj)
        }

        401 -> Result.Error(DataError.Network.UNAUTHORISED)
        403 -> Result.Error(DataError.Network.PASSWORD_DOES_NOT_MATCH)
        404 -> Result.Error(DataError.Network.NOT_FOUND)
        406 -> Result.Error(DataError.Network.EMAIL_NOT_VERIFIED)
        409 -> Result.Error(DataError.Network.CONFLICT)
        in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
        else -> Result.Error(DataError.Network.UNKNOWN)
    }
}

fun handleOtherException(exception: Exception): Result<Nothing, DataError.Network> {
    return when (exception) {
        is UnresolvedAddressException -> Result.Error(DataError.Network.NO_INTERNET)
        is SerializationException -> Result.Error(DataError.Network.SERIALISATION)
        is CancellationException -> throw exception
        else -> Result.Error(DataError.Network.UNKNOWN)
    }
}