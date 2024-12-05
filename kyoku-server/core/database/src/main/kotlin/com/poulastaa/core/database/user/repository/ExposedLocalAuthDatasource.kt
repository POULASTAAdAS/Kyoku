package com.poulastaa.core.database.user.repository

import com.poulastaa.core.domain.model.AuthResponseDto
import com.poulastaa.core.domain.model.ServerUserDto
import com.poulastaa.core.domain.repository.LocalAuthDatasource

class ExposedLocalAuthDatasource : LocalAuthDatasource {
    override suspend fun getCountryId(): Int? {
        return null
    }

    override suspend fun googleSingUp(payload: ServerUserDto): AuthResponseDto {
        return AuthResponseDto()
    }

    override suspend fun emailSingUp(
        payload: ServerUserDto,
        refreshToken: String,
    ): AuthResponseDto {
        return AuthResponseDto()
    }

    override suspend fun emailLogIn(
        email: String,
        password: String,
        refreshToken: String,
    ): AuthResponseDto {
        return AuthResponseDto()
    }
}