package com.example.domain.repository

import com.example.data.model.auth.res.EmailLoginResponse
import com.example.data.model.auth.res.EmailSignInResponse
import com.example.data.model.auth.res.EmailVerificationResponse
import com.example.data.model.auth.stat.PasswordResetStatus
import com.example.data.model.auth.stat.SendForgotPasswordMail
import com.example.data.model.auth.stat.UpdateEmailVerificationStatus
import java.io.File

interface UserServiceRepository {
    suspend fun createUser(
        userName: String,
        email: String,
        password: String,
    ): EmailSignInResponse

    suspend fun updateVerificationStatus(token: String): UpdateEmailVerificationStatus
    suspend fun checkEmailVerification(email: String): EmailVerificationResponse
    suspend fun loginUser(email: String, password: String): EmailLoginResponse
    suspend fun sendForgotPasswordMail(email: String): SendForgotPasswordMail
    suspend fun resetPassword(
        token: String,
        password: String
    ): PasswordResetStatus

    suspend fun getUserProfilePic(email: String): File?
}