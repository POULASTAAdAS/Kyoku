package com.poulastaa.core.domain.repository.auth

import com.poulastaa.core.domain.model.DtoDBUser
import com.poulastaa.core.domain.model.MailType
import com.poulastaa.core.domain.model.DtoServerUser
import com.poulastaa.core.domain.model.UserType

typealias Email = String

interface LocalAuthDatasource {
    suspend fun getCountryIdFromCountryCode(countryCode: String): Int?

    suspend fun getUsersByEmail(email: String, type: UserType): DtoDBUser?

    suspend fun createUser(user: DtoServerUser, isDbStore: Boolean = true): DtoDBUser

    fun sendMail(message: Pair<MailType, Email>)

    fun isVerificationTokenUsed(token: String): Boolean
    fun storeUsedVerificationToken(token: String)
    fun updateVerificationMailStatus(email: Email): Boolean?

    fun getJWTTokenStatus(email: Email): Boolean

    suspend fun saveRefreshToken(token: String, email: Email)

    fun isResetPasswordTokenUsed(token: String): Boolean
    suspend fun updatePassword(email: Email, password: String)
}