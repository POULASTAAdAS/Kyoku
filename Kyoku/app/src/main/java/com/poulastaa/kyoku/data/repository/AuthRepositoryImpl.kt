package com.poulastaa.kyoku.data.repository

import android.util.Log
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthResponse
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyJson
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.req.PasskeyAuthReq
import com.poulastaa.kyoku.data.remote.AuthApi
import com.poulastaa.kyoku.domain.repository.AuthRepository
import com.poulastaa.kyoku.utils.toPasskeyJson
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun passkeyReq(req: PasskeyAuthReq): PasskeyJson? {
        return try {
            val result = api.passkeyReq(req).string()

            return result.toPasskeyJson() // convert to class and remove type and token
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createPasskeyUser(
        createPasskeyUserReq: CreatePasskeyUserReq
    ): PasskeyAuthResponse? {
        return try {
            return api.createPasskeyUser(createPasskeyUserReq)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getPasskeyUser(user: GetPasskeyUserReq): PasskeyAuthResponse? {
        return try {
            return api.getPasskeyUser(user)
        } catch (e: Exception) {
            Log.d("apiError", e.message.toString())
            null
        }
    }
}