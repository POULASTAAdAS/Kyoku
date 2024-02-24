package com.poulastaa.kyoku.data.remote

import com.poulastaa.kyoku.data.model.api.req.RefreshTokenReq
import com.poulastaa.kyoku.data.model.api.auth.email.RefreshTokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApi {
    @POST("/api/auth/refreshToken")
    suspend fun refreshToken(
        @Body req: RefreshTokenReq
    ): RefreshTokenResponse
}