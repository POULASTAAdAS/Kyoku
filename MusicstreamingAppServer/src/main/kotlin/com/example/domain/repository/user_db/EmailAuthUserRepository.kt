package com.example.domain.repository.user_db

import com.example.data.model.auth.res.EmailLoginResponse
import com.example.data.model.auth.stat.*
import java.io.File

interface EmailAuthUserRepository {
    suspend fun createUser(
        userName: String,
        email: String,
        password: String,
        refreshToken: String
    ): UserCreationStatus

    suspend fun updateVerificationStatus(email: String): UpdateEmailVerificationStatus
    suspend fun checkEmailVerification(email: String): EmailVerificationStatus

    suspend fun loginUser(
        email: String,
        password: String,
        accessToken: String,
        refreshToken: String,
    ): EmailLoginResponse

    suspend fun checkIfUSerExistsToSendForgotPasswordMail(email: String): SendVerificationMailStatus
    suspend fun resetPassword(email: String, password: String): PasswordResetStatus

    suspend fun getUserProfilePic(email: String): File?

    suspend fun updateRefreshToken(
        email: String,
        oldRefreshToken: String,
        refreshToken: String
    ): RefreshTokenUpdateStatus
}