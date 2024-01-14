package com.example.domain.repository

import com.example.data.model.auth.res.*
import com.example.data.model.auth.stat.PasswordResetStatus
import com.example.data.model.auth.stat.UpdateEmailVerificationStatus
import java.io.File

interface UserServiceRepository {
    suspend fun createUser(
        userName: String,
        email: String,
        password: String,
    ): EmailSignInResponse


    suspend fun loginUser(
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

    suspend fun getUserProfilePic(email: String): File?

    suspend fun refreshToken(token: String): RefreshTokenResponse

    suspend fun createUser(
        userName: String,
        email: String,
        sub: String,
        pictureUrl: String
    ): GoogleSignInResponse

}