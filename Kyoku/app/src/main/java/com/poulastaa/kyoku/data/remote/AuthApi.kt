package com.poulastaa.kyoku.data.remote

import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthResponse
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.req.PasskeyAuthReq
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST

interface AuthApi {
    @POST("/api/auth")
    suspend fun passkeyReq(
        @Body request: PasskeyAuthReq
    ): ResponseBody

    @POST("/api/auth/createPasskeyUser")
    suspend fun createPasskeyUser(
        @Body request: CreatePasskeyUserReq
    ): PasskeyAuthResponse

    @POST("/api/auth/getPasskeyUser")
    suspend fun getPasskeyUser(
        @Body request: GetPasskeyUserReq
    ): PasskeyAuthResponse
}