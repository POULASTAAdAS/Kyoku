package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.MailType
import com.poulastaa.core.domain.model.UserType

interface LocalAuthCacheDatasource {
    fun cachedCountryId(key: String): Int?
    fun setCountryId(key: String, value: String)

    fun cachedUserByEmail(key: Email, type: UserType): DBUserDto?
    fun setUserByEmail(key: Email, type: UserType, value: DBUserDto)

    fun isVerificationTokenUsed(token: String): Boolean
    fun storeUsedVerificationToken(token: String)

    fun cacheEmailVerificationStatus(key: Email): Boolean?
    fun setEmailVerificationStatus(key: Email)
    fun deleteEmailVerificationStatus(key: Email)

    fun produceMail(message: Pair<MailType, Email>)
    suspend fun consumeMail(block: (Pair<MailType, Email>) -> Unit)

    fun cacheJWTTokenState(email: String): Boolean
    fun storeJWTTokenState(email: Email)

    fun isResetPasswordTokenUsed(token: String): Boolean
}