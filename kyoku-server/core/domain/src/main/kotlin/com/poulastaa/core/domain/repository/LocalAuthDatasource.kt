package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.AuthResponseDto
import com.poulastaa.core.domain.model.ServerUserDto

interface LocalAuthDatasource {
    suspend fun getCountryId(): Int?

    suspend fun googleSingUp(
        payload: ServerUserDto,
    ): AuthResponseDto

    suspend fun emailSingUp(
        payload: ServerUserDto,
        refreshToken: String,
    ): AuthResponseDto

    suspend fun emailLogIn(
        email: String,
        password: String,
        refreshToken: String,
    ): AuthResponseDto
}