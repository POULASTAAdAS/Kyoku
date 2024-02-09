package com.poulastaa.domain.repository

import com.poulastaa.data.model.auth.google.GoogleAuthResponse
import com.poulastaa.data.model.auth.jwt.*
import com.poulastaa.data.model.auth.jwt.PasswordResetStatus
import com.poulastaa.data.model.auth.jwt.UpdateEmailVerificationStatus
import com.poulastaa.data.model.auth.passkey.PasskeyAuthResponse
import com.poulastaa.domain.dao.PasskeyAuthUser
import java.io.File

interface UserServiceRepository {
    // email auth
    suspend fun createEmailUser(
        userName: String,
        email: String,
        password: String,
    ): EmailSignInResponse


    suspend fun getEmailUser(
        email: String,
        password: String
    ): EmailLoginResponse

    suspend fun updateVerificationStatus(token: String): UpdateEmailVerificationStatus
    suspend fun checkEmailVerification(email: String): EmailVerificationResponse
    suspend fun sendForgotPasswordMail(email: String): SendForgotPasswordMail
    suspend fun resetPassword(
        token: String,
        password: String
    ): PasswordResetStatus

    suspend fun refreshToken(token: String): RefreshTokenResponse

    suspend fun getUserProfilePic(email: String): File?

    // google auth
    suspend fun createGoogleUser(
        userName: String,
        email: String,
        sub: String,
        pictureUrl: String
    ): GoogleAuthResponse

    // passkey auth
    suspend fun createPasskeyUser(
        token:String,
        userName: String,
        email: String,
        userId: String
    ): PasskeyAuthResponse

    suspend fun getPasskeyUser(userId: String , token:String): PasskeyAuthResponse

    suspend fun getPasskeyJsonResponse(email: String , displayName:String): Any

    suspend fun sendVerificationMail(email:String): ResendVerificationMailResponse
}