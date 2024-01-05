package com.example.domain.repository.user

import com.example.routes.auth.common.EmailVerificationStatus
import com.example.routes.auth.common.UpdateEmailVerificationStatus
import com.example.routes.auth.common.UserCreationStatus

interface EmailAuthUserRepository {
    suspend fun createUser(
        userName: String,
        email: String,
        password: String
    ): UserCreationStatus

    suspend fun updateVerificationStatus(email: String): UpdateEmailVerificationStatus

    suspend fun checkEmailVerification(email: String): EmailVerificationStatus
}