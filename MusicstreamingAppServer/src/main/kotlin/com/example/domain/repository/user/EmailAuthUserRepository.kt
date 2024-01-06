package com.example.domain.repository.user

import com.example.data.model.EmailLoginResponse
import com.example.data.model.EmailSignInResponse
import com.example.data.model.EmailVerificationResponse
import com.example.data.model.SendForgotPasswordMail
import com.example.routes.auth.common.PasswordResetStatus
import com.example.routes.auth.common.UpdateEmailVerificationStatus

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
}