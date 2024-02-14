package com.poulastaa.kyoku.data.remote

import com.poulastaa.kyoku.data.model.auth.email.reafresh_token.RefreshTokenReq
import com.poulastaa.kyoku.data.model.auth.email.reafresh_token.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApi {
    @POST("/api/auth/refreshToken")
    suspend fun refreshToken(
        @Body req: RefreshTokenReq
    ): RefreshTokenResponse
}