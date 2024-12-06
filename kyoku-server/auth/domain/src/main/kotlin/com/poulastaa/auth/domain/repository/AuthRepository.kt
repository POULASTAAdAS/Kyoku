package com.poulastaa.auth.domain.repository

import com.poulastaa.core.domain.model.AuthResponseDto
import com.poulastaa.core.domain.model.GoogleAuthPayloadDto

interface AuthRepository {
    suspend fun googleAuth(payload: GoogleAuthPayloadDto, countryCode: String): AuthResponseDto
}