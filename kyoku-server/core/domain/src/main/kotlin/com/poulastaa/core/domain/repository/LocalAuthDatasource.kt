package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.MailType
import com.poulastaa.core.domain.model.ServerUserDto
import com.poulastaa.core.domain.model.UserType

typealias Email = String

interface LocalAuthDatasource {
    suspend fun getCountryIdFromCountryCode(countryCode: String): Int?

    suspend fun getUsersByEmail(email: String, type: UserType): DBUserDto?

    suspend fun createUser(user: ServerUserDto, isDbStore: Boolean = true): DBUserDto

    fun sendMail(message: Pair<MailType, Email>)

    fun isVerificationTokenUsed(token: String): Boolean
    fun storeUsedVerificationToken(token: String)
    fun updateVerificationMailStatus(email: Email): Boolean?

    fun getJWTTokenStatus(email: Email): Boolean

    suspend fun saveRefreshToken(token: String, email: Email)

    fun isResetPasswordTokenUsed(token: String): Boolean
    suspend fun updatePassword(email: Email, password: String)
}