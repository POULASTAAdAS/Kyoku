package com.poulastaa.domain.repository.user_db

import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.auth.jwt.*
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

    suspend fun checkIfUSerExistsToSendForgotPasswordMail(email: String): SendForgotPasswordMailStatus
    suspend fun resetPassword(email: String, password: String): PasswordResetStatus

    suspend fun getUserProfilePic(email: String): File?

    suspend fun updateRefreshToken(
        email: String,
        oldRefreshToken: String,
        refreshToken: String
    ): RefreshTokenUpdateStatus

    suspend fun checkVerificationMailStatus(email: String): ResendVerificationMailStatus
}