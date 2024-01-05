package com.example.domain.repository.user

import com.example.data.model.EmailLoginResponse
import com.example.data.model.EmailSignUpResponse
import com.example.data.model.SendVerificationMail
import com.example.data.model.UserCreationResponse
import com.example.routes.auth.common.UpdateEmailVerificationStatus

interface EmailAuthUserRepository {
    suspend fun createUser(
        userName: String,
        email: String,
        password: String
    ): UserCreationResponse

    suspend fun updateVerificationStatus(email: String): UpdateEmailVerificationStatus

    suspend fun checkEmailVerification(email: String): EmailSignUpResponse

    suspend fun loginUser(email: String, password: String): EmailLoginResponse
    suspend fun checkIfUserExists(email: String, password: String): SendVerificationMail
}