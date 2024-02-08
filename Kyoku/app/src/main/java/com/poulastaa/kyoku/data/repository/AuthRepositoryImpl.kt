package com.poulastaa.kyoku.data.repository

import com.poulastaa.kyoku.data.model.api.auth.email.EmailLogInResponse
import com.poulastaa.kyoku.data.model.api.auth.email.EmailSignUpResponse
import com.poulastaa.kyoku.data.model.api.auth.google.GoogleAuthResponse
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthResponse
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyJson
import com.poulastaa.kyoku.data.model.api.req.EmailLogInReq
import com.poulastaa.kyoku.data.model.api.req.EmailSignUpReq
import com.poulastaa.kyoku.data.model.api.req.GoogleAuthReq
import com.poulastaa.kyoku.data.model.api.req.PasskeyAuthReq
import com.poulastaa.kyoku.data.model.auth.email.signup.EmailVerificationStatus
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
            null
        }
    }

    override suspend fun googleAuth(req: GoogleAuthReq): GoogleAuthResponse? {
        return try {
            return api.googleReq(req)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun emailSignUp(req: EmailSignUpReq): EmailSignUpResponse? {
        return try {
            return api.emailSignUp(req)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun emailLogIn(req: EmailLogInReq): EmailLogInResponse? {
        return try {
            return api.emailLogIn(req)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun isEmailVerified(email: String): Boolean {
        return try {
            val response = api.isEmailVerified(email)

            return when (response.status) {
                EmailVerificationStatus.VERIFIED -> true
                EmailVerificationStatus.UN_VERIFIED -> false
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }
}