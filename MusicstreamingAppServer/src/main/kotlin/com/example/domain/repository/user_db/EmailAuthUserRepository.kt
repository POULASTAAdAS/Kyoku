package com.example.domain.repository.user_db

import com.example.data.model.auth.res.EmailLoginResponse
import com.example.data.model.auth.res.EmailSignInResponse
import com.example.data.model.auth.res.EmailVerificationResponse
import com.example.data.model.auth.stat.PasswordResetStatus
import com.example.data.model.auth.stat.SendForgotPasswordMail
import com.example.data.model.auth.stat.UpdateEmailVerificationStatus
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