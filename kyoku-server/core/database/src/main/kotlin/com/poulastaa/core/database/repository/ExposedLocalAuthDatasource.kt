package com.poulastaa.core.database.repository

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.CountryDao
import com.poulastaa.core.database.dao.UserDao
import com.poulastaa.core.database.entity.CountryEntity
import com.poulastaa.core.database.entity.EmailVerificationEntity
import com.poulastaa.core.database.entity.UserEntity
import com.poulastaa.core.database.mapper.toDbUserDto
import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.ServerUserDto
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.LocalAuthDatasource
import com.poulastaa.core.domain.repository.LocalCacheDatasource
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.upperCase

class ExposedLocalAuthDatasource(
    private val cache: LocalCacheDatasource,
) : LocalAuthDatasource {
    private val countryListMap = mapOf(
        "In" to "India",
        "US" to "United States"
    )

    override suspend fun getCountryIdFromCountryCode(countryCode: String): Int? {
        val country = countryListMap.firstNotNullOfOrNull { (code, value) ->
            if (code.equals(countryCode, ignoreCase = true)) value else null
        } ?: return null

        cache.cachedCountryId(country.uppercase())?.let { return it }

        val dao = kyokuDbQuery {
            CountryDao.find {
                CountryEntity.country.upperCase() eq country.uppercase()
            }.singleOrNull()
        } ?: return null

        cache.setCountryId(dao.country.uppercase(), dao.id.value.toString())

        return dao.id.value
    }

    override suspend fun getUsersByEmail(
        email: String,
        type: UserType,
    ): DBUserDto? {
        cache.cachedUserByEmail(email, type)?.let { return it }

        val dbUser = userDbQuery {
            UserDao.find {
                UserEntity.email eq email and (UserEntity.userType eq type.name)
            }.firstOrNull()
        } ?: return null

        cache.setUserByEmail(email, type, dbUser.toDbUserDto())

        return dbUser.toDbUserDto()
    }

    override suspend fun isEmailUserEmailVerified(userId: Long): Boolean {
        cache.cacheEmailVerificationStatus(userId)?.let { return@let it }

        val dbStatus = userDbQuery {
            EmailVerificationEntity.select {
                EmailVerificationEntity.userId eq userId
            }.map { row ->
                row[EmailVerificationEntity.status]
            }.firstOrNull()
        } == true

        cache.setEmailVerificationStatus(userId, dbStatus)

        return dbStatus
    }

    override suspend fun createUser(user: ServerUserDto): DBUserDto = userDbQuery {
        UserDao.new {
            this.email = user.email
            this.username = user.username
            this.userType = user.type.name
            this.passwordHash = user.password
            this.profilePicUrl = user.profilePicUrl
            this.countryId = user.countryId
        }
    }.toDbUserDto()
}