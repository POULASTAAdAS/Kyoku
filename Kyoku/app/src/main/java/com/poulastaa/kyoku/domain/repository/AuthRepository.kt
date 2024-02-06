package com.poulastaa.kyoku.domain.repository

import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthResponse
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyJson
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.req.PasskeyAuthReq

interface AuthRepository {
    suspend fun passkeyReq(req: PasskeyAuthReq): PasskeyJson?
    suspend fun createPasskeyUser(createPasskeyUserReq: CreatePasskeyUserReq): PasskeyAuthResponse?
    suspend fun getPasskeyUser(user: GetPasskeyUserReq): PasskeyAuthResponse?
}