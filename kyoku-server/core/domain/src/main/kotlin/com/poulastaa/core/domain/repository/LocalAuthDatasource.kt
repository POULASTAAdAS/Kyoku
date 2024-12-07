package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.ServerUserDto
import com.poulastaa.core.domain.model.UserType

interface LocalAuthDatasource {
    suspend fun getCountryId(countryCode: String): Int?

    suspend fun getUsersByEmail(email: String, type: UserType): DBUserDto?

    suspend fun createGoogleUser(user: ServerUserDto): DBUserDto
    suspend fun createEmailUser(
        payload: ServerUserDto,
        refreshToken: String,
    ): DBUserDto
}