package com.poulastaa.auth.domain.repository

import com.poulastaa.auth.domain.model.AuthResponseDto
import com.poulastaa.auth.domain.model.EmailSignInPayload
import com.poulastaa.auth.domain.model.EmailSignUpPayload
import com.poulastaa.auth.domain.model.GoogleAuthPayloadDto

interface AuthRepository {
    suspend fun googleAuth(payload: GoogleAuthPayloadDto, countryCode: String): AuthResponseDto
    suspend fun emailSignUp(payload: EmailSignUpPayload): AuthResponseDto
    suspend fun emailSignIn(payload: EmailSignInPayload): AuthResponseDto
}