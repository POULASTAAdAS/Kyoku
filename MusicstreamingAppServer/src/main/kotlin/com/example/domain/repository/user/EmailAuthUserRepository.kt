package com.example.domain.repository.user

import com.example.util.EmailVerificationStatus
import com.example.util.UpdateEmailVerificationStatus
import com.example.util.UserCreationStatus

interface EmailAuthUserRepository {
    suspend fun createUser(
        userName: String,
        email: String,
        password: String
    ): UserCreationStatus

    suspend fun updateVerificationStatus(email: String): UpdateEmailVerificationStatus

    suspend fun checkEmailVerification(email: String): EmailVerificationStatus
}