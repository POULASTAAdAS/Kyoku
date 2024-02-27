package com.poulastaa.kyoku.domain.repository

import com.poulastaa.kyoku.data.model.api.auth.email.EmailLogInResponse
import com.poulastaa.kyoku.data.model.api.auth.email.EmailSignUpResponse
import com.poulastaa.kyoku.data.model.api.auth.email.ResendVerificationMailStatus
import com.poulastaa.kyoku.data.model.api.auth.email.SendForgotPasswordMailStatus
import com.poulastaa.kyoku.data.model.api.auth.google.GoogleAuthResponse
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthResponse
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyJson
import com.poulastaa.kyoku.data.model.api.auth.email.EmailLogInReq
import com.poulastaa.kyoku.data.model.api.auth.email.EmailSignUpReq
import com.poulastaa.kyoku.data.model.api.auth.google.GoogleAuthReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthReq

interface AuthRepository {
    suspend fun passkeyReq(req: PasskeyAuthReq): PasskeyJson?
    suspend fun createPasskeyUser(createPasskeyUserReq: CreatePasskeyUserReq): PasskeyAuthResponse?
    suspend fun getPasskeyUser(user: GetPasskeyUserReq): PasskeyAuthResponse?

    suspend fun googleAuth(req: GoogleAuthReq): GoogleAuthResponse?

    suspend fun emailSignUp(req: EmailSignUpReq): EmailSignUpResponse?
    suspend fun emailLogIn(req: EmailLogInReq): EmailLogInResponse?

    suspend fun isEmailVerified(email: String): Boolean
    suspend fun resendVerificationMail(email: String): ResendVerificationMailStatus

    suspend fun forgotPassword(email: String): SendForgotPasswordMailStatus
}