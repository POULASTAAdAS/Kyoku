package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.UserType

interface LocalCoreDatasource {
    suspend fun getUserByEmail(email: String, userType: UserType): DBUserDto?
}