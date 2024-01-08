package com.example.domain.repository.user

import com.example.data.model.auth.*
import java.io.File

interface EmailAuthUserRepository {
    suspend fun createUser(
        userName: String,
        email: String,
        password: String,
        token: String
    ): EmailSignInResponse

    suspend fun updateVerificationStatus(email: String): UpdateEmailVerificationStatus
    suspend fun checkEmailVerification(email: String): EmailVerificationResponse
    suspend fun loginUser(email: String, password: String, token: String): EmailLoginResponse
    suspend fun sendForgotPasswordMail(email: String): SendForgotPasswordMail
    suspend fun passwordReset(email: String, password: String): PasswordResetStatus

    suspend fun getUserProfilePic(email: String): File?
}