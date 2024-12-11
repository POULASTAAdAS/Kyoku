package com.poulastaa.auth.domain.repository

import com.poulastaa.auth.domain.model.*

interface AuthRepository {
    suspend fun googleAuth(payload: GoogleAuthPayloadDto, countryCode: String): AuthResponseDto
    suspend fun emailSignUp(payload: EmailSignUpPayload): AuthResponseDto
    suspend fun emailLogIn(payload: EmailLogInPayload): AuthResponseDto

    suspend fun verifyEmail(token: String): EmailVerificationStatusDto
    suspend fun checkEmailVerificationState(email: String): JwtTokenDto?
    suspend fun forgotPassword(email: String): ForgotPasswordResponseStatusDto

    suspend fun updatePassword(token: String, newPassword: String): UpdatePasswordStatusDto
}