package com.poulastaa.auth.data.repository

import com.poulastaa.auth.domain.repository.AuthRepository
import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.LocalAuthDatasource

class AuthenticationService(
    private val db: LocalAuthDatasource,
) : AuthRepository {
    override suspend fun googleAuth(payload: GoogleAuthPayloadDto): AuthResponseDto {
        val countryId = db.getCountryId() ?: return AuthResponseDto()

        return AuthResponseDto(
            status = AuthResponseStatusDto.SERVER_ERROR,
            user = UserDto(),
            token = JwtTokenDto()
        )
    }
}