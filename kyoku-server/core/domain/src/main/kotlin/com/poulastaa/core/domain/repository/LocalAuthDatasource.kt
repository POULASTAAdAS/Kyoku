package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.ServerUserDto
import com.poulastaa.core.domain.model.UserType

interface LocalAuthDatasource {
    suspend fun getCountryIdFromCountryCode(countryCode: String): Int?

    suspend fun getUsersByEmail(email: String, type: UserType): DBUserDto?

    suspend fun createGoogleUser(user: ServerUserDto): DBUserDto

    suspend fun isEmailUserEmailVerified(userId: Long): Boolean

    suspend fun createEmailUser(
        user: ServerUserDto,
        refreshToken: String,
    ): DBUserDto
}