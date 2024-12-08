package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.MailType
import com.poulastaa.core.domain.model.UserType

interface LocalCacheDatasource {
    fun cachedCountryId(key: String): Int?
    fun setCountryId(key: String, value: String)

    fun cachedUserByEmail(key: String, type: UserType): DBUserDto?
    fun setUserByEmail(key: String, type: UserType, value: DBUserDto)

    fun cacheEmailVerificationStatus(key: Long): Boolean?
    fun setEmailVerificationStatus(key: Long, value: Boolean)

    fun produceMail(message: Pair<MailType, String>)
    suspend fun consumeMail(block: (Pair<MailType, String>) -> Unit)
}