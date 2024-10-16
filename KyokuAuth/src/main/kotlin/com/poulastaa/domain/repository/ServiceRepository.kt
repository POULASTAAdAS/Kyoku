package com.poulastaa.domain.repository

import com.poulastaa.data.model.VerifiedMailStatus
import com.poulastaa.data.model.auth.req.EmailLogInReq
import com.poulastaa.data.model.auth.req.EmailSignUpReq
import com.poulastaa.data.model.auth.response.CheckEmailVerificationResponse
import com.poulastaa.data.model.auth.response.ForgotPasswordSetStatus
import com.poulastaa.data.model.auth.response.Payload
import com.poulastaa.data.model.auth.response.UserAuthRes
import com.poulastaa.data.model.payload.UpdatePasswordStatus

typealias UserId = Long
typealias Email = String

interface ServiceRepository {
    suspend fun createEmailUser(
        req: EmailSignUpReq,
    ): UserAuthRes

    suspend fun loginEmailUser(
        req: EmailLogInReq,
    ): UserAuthRes

    suspend fun googleAuth(
        payload: Payload,
        countryCode: String,
    ): Pair<UserAuthRes, UserId>

    suspend fun updateSignUpEmailVerificationStatus(
        token: String,
    ): VerifiedMailStatus

    suspend fun updateLogInEmailVerificationStatus(
        token: String,
    ): VerifiedMailStatus

    suspend fun signUpEmailVerificationCheck(
        email: String,
    ): CheckEmailVerificationResponse

    suspend fun logInEmailVerificationCheck(
        email: String,
    ): CheckEmailVerificationResponse

    suspend fun sendForgotPasswordMail(
        email: String,
    ): ForgotPasswordSetStatus

    suspend fun validateForgotPasswordMailToken(
        token: String,
    ): Pair<Email, VerifiedMailStatus>

    suspend fun updatePassword(
        token: String,
        password: String,
    ): UpdatePasswordStatus
}