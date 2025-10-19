package com.poulastaa.core.network.data

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.network.BuildConfig
import com.poulastaa.core.network.domain.model.DtoReqParam
import com.poulastaa.core.network.domain.model.Endpoints
import com.poulastaa.core.network.domain.repository.ApiRepository
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
import okio.IOException
import java.lang.reflect.Type
import java.nio.channels.UnresolvedAddressException
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class OkHttpDatasource @Inject constructor(
    @param:Named("authClient")
    private val authClient: OkHttpClient,
    @param:Named("mainClient")
    private val mainClient: OkHttpClient,
    private val gson: Gson,
) : ApiRepository {
    private val mediaType = "application/json; charset=utf-8".toMediaType()

    override suspend fun <Req : Any, Res : Any> authReq(
        route: Endpoints,
        method: ApiRepository.Method,
        type: Type,
        body: Req?,
        params: List<DtoReqParam>,
    ): Result<Res, DataError.Network> {
        val uriBuilder = constructRoute(route.endpoint).toHttpUrlOrNull()?.newBuilder()
            ?: return Result.Error(DataError.Network.UNKNOWN)

        params.forEach { uriBuilder.addQueryParameter(it.key, it.value) }
        val reqBuilder = Request.Builder().url(uriBuilder.build())

        val reqBody = body?.let { gson.toJson(it).toRequestBody(mediaType) }
        when (method) {
            ApiRepository.Method.GET -> reqBuilder.get()
            ApiRepository.Method.POST -> reqBody?.let { reqBuilder.post(reqBody) }
            ApiRepository.Method.PUT -> reqBody?.let { reqBuilder.put(reqBody) }
        }

        return try {
            val response = authClient.makeCall(reqBuilder.build())
            responseToResult<Res>(response, type).also { response.close() }
        } catch (e: Exception) {
            Log.d("requestError", e.message.toString())
            handleOtherException(e)
        }
    }

//    override suspend fun <Req : Any, Res : Any> apiReq(
//        route: Endpoints,
//        method: ApiRepository.Method,
//        body: Req?,
//        params: List<DtoReqParam>,
//    ): Result<Res, DataError.Network> {
//        val uriBuilder = constructRoute(route.endpoint).toHttpUrlOrNull()?.newBuilder()
//            ?: return Result.Error(DataError.Network.UNKNOWN)
//
//        params.forEach { uriBuilder.addQueryParameter(it.key, it.value) }
//        val reqBuilder = Request.Builder().url(uriBuilder.build())
//
//        val reqBody = body?.let { gson.toJson(it).toRequestBody(mediaType) }
//        when (method) {
//            ApiRepository.Method.GET -> reqBuilder.get()
//            ApiRepository.Method.POST -> reqBody?.let { reqBuilder.post(reqBody) }
//            ApiRepository.Method.PUT -> reqBody?.let { reqBuilder.post(reqBody) }
//        }
//
//        return try {
//            val response = mainClient.makeCall(reqBuilder.build())
//            responseToResult<Res>(response).also { response.close() }
//        } catch (e: Exception) {
//            Log.d("requestError", e.message.toString())
//
//            handleOtherException(e)
//        }
//    }

    private fun constructRoute(route: String) = "${BuildConfig.BASE_URL}$route"

    private suspend inline fun OkHttpClient.makeCall(request: Request) =
        suspendCoroutine { continuation ->
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

    private suspend inline fun <T> responseToResult(
        response: Response,
        type: Type,
    ) =
        withContext(Dispatchers.IO) {
            when (response.code) {
                in 200..499 -> {
                    val body = response.body?.string()
                        ?: return@withContext Result.Error(DataError.Network.SERIALISATION)
                    val obj = gson.fromJson<T>(body, type)

                    Result.Success(obj)
                }

                in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
                else -> Result.Error(DataError.Network.UNKNOWN)
            }
        }

    private fun handleOtherException(exception: Exception): Result<Nothing, DataError.Network> {
        return when (exception) {
            is UnresolvedAddressException -> Result.Error(DataError.Network.NO_INTERNET)
            is SerializationException -> Result.Error(DataError.Network.SERIALISATION)
            is CancellationException -> throw exception
            else -> Result.Error(DataError.Network.UNKNOWN)
        }
    }
}