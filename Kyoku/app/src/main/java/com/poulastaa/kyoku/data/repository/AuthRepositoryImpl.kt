package com.poulastaa.kyoku.data.repository

import android.util.Log
import com.poulastaa.kyoku.data.model.api.auth.email.EmailLogInReq
import com.poulastaa.kyoku.data.model.api.auth.email.EmailLogInResponse
import com.poulastaa.kyoku.data.model.api.auth.email.EmailSignUpReq
import com.poulastaa.kyoku.data.model.api.auth.email.EmailSignUpResponse
import com.poulastaa.kyoku.data.model.api.auth.email.ResendVerificationMailStatus
import com.poulastaa.kyoku.data.model.api.auth.email.SendForgotPasswordMailStatus
import com.poulastaa.kyoku.data.model.api.auth.google.GoogleAuthReq
import com.poulastaa.kyoku.data.model.api.auth.google.GoogleAuthResponse
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthResponse
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyJson
import com.poulastaa.kyoku.data.model.screens.auth.email.signup.EmailVerificationStatus
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

            result.toPasskeyJson() // convert to class and remove type and token
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createPasskeyUser(
        createPasskeyUserReq: CreatePasskeyUserReq
    ): PasskeyAuthResponse? {
        return try {
            api.createPasskeyUser(createPasskeyUserReq)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getPasskeyUser(user: GetPasskeyUserReq): PasskeyAuthResponse? {
        return try {
            api.getPasskeyUser(user)
        } catch (e: Exception) {
            Log.d("error" , e.message.toString())
            null
        }
    }

    override suspend fun googleAuth(req: GoogleAuthReq): GoogleAuthResponse? {
        return try {
            api.googleReq(req)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun emailSignUp(req: EmailSignUpReq): EmailSignUpResponse? {
        return try {
            api.emailSignUp(req)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun emailLogIn(req: EmailLogInReq): EmailLogInResponse? {
        return try {
            api.emailLogIn(req)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun isEmailVerified(email: String): Boolean {
        return try {
            val response = api.isEmailVerified(email)

            when (response.status) {
                EmailVerificationStatus.VERIFIED -> true
                EmailVerificationStatus.UN_VERIFIED -> false
                else -> false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun resendVerificationMail(email: String): ResendVerificationMailStatus {
        return try {
            api.resendVerificationMail(email).status
        } catch (e: Exception) {
            ResendVerificationMailStatus.SOMETHING_WENT_WRONG
        }
    }

    override suspend fun forgotPassword(email: String): SendForgotPasswordMailStatus {
        return try {
            api.forgotPassword(email).status
        } catch (e: Exception) {
            SendForgotPasswordMailStatus.SOMETHING_WENT_WRONG
        }
    }
}