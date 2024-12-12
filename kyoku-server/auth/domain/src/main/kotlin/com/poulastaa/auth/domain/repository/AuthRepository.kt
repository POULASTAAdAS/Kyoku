package com.poulastaa.auth.domain.repository

import com.poulastaa.auth.domain.model.*
import com.poulastaa.core.domain.repository.Email
import com.poulastaa.core.domain.repository.JWTToken

interface AuthRepository {
    suspend fun googleAuth(payload: GoogleAuthPayloadDto, countryCode: String): AuthResponseDto
    suspend fun emailSignUp(payload: EmailSignUpPayload): AuthResponseDto
    suspend fun emailLogIn(payload: EmailLogInPayload): AuthResponseDto

    suspend fun verifyEmail(token: String): EmailVerificationStatusDto
    suspend fun getJWTToken(email: String): JwtTokenDto?

    suspend fun forgotPassword(email: String): ForgotPasswordResponseStatusDto
    suspend fun verifyResetPasswordRequest(token: String): Pair<Email, ResetPasswordStatusDto>
    fun getSubmitNewPasswordToken(email: String): JWTToken
    suspend fun updatePassword(token: String, newPassword: String): UpdatePasswordStatusDto
}