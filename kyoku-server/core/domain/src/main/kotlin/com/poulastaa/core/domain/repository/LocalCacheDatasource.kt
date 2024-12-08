package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.UserType

interface LocalCacheDatasource {
    fun cachedCountryId(key: String): Int?
    fun setCountryId(key: String, value: String)

    fun cachedUserByEmail(key: String, type: UserType): DBUserDto?
    fun setUserByEmail(key: String, type: UserType, value: DBUserDto)

    suspend fun cacheEmailVerificationStatus(key: Long): Boolean?
    suspend fun setEmailVerificationStatus(key: Long, value: Boolean)
}