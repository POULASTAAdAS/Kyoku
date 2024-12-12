package com.poulastaa.core.database.repository

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.CountryDao
import com.poulastaa.core.database.dao.UserDao
import com.poulastaa.core.database.entity.CountryEntity
import com.poulastaa.core.database.entity.UserEntity
import com.poulastaa.core.database.entity.UserJWTRelationEntity
import com.poulastaa.core.database.mapper.toDbUserDto
import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.MailType
import com.poulastaa.core.domain.model.ServerUserDto
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.Email
import com.poulastaa.core.domain.repository.LocalAuthDatasource
import com.poulastaa.core.domain.repository.LocalCacheDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.upperCase
import org.jetbrains.exposed.sql.upsert

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

    override suspend fun createUser(user: ServerUserDto, isDbStore: Boolean): DBUserDto {
        val dbUser = when {
            (user.type == UserType.GOOGLE || user.type == UserType.EMAIL) && isDbStore -> {
                userDbQuery {
                    UserDao.new { // todo fix insert ignore
                        this.email = user.email
                        this.username = user.username
                        this.userType = user.type.name
                        this.passwordHash = user.password
                        this.profilePicUrl = user.profilePicUrl
                        this.countryId = user.countryId
                    }
                }.toDbUserDto()
            }

            else -> DBUserDto(
                id = -1,
                email = user.email,
                userName = user.username,
                passwordHash = user.password,
                profilePicUrl = user.profilePicUrl,
                countryCode = user.countryId
            )
        }

        cache.setUserByEmail(
            key = user.email,
            type = user.type,
            value = dbUser
        )

        return dbUser
    }

    override fun sendMail(message: Pair<MailType, Email>) {
        CoroutineScope(Dispatchers.IO).launch {
            cache.produceMail(message)
        }

        if (message.first == MailType.EMAIL_VERIFICATION)
            cache.setEmailVerificationStatus(message.second)
    }

    override fun isVerificationTokenUsed(token: String): Boolean = cache.isVerificationTokenUsed(token)

    override fun storeUsedVerificationToken(token: String) = cache.storeUsedVerificationToken(token)

    override fun updateVerificationMailStatus(email: Email): Boolean? {
        val status = cache.cacheEmailVerificationStatus(email)?.not() ?: return null
        if (status) {
            cache.deleteEmailVerificationStatus(email)
            cache.storeJWTTokenState(email)
        }

        return status
    }

    override fun getJWTTokenStatus(email: Email): Boolean = cache.cacheJWTTokenState(email)

    override suspend fun saveRefreshToken(token: String, email: Email) {
        val user = getUsersByEmail(email, UserType.EMAIL)
        if (user == null || user.id == -1L) return

        userDbQuery {
            UserJWTRelationEntity.upsert {
                it[this.userId] = user.id
                it[this.refreshToken] = token
            }
        }
    }

    override fun isResetPasswordTokenUsed(token: String): Boolean = cache.isResetPasswordTokenUsed(token)

    override suspend fun updatePassword(email: Email, password: String) {
        val user = userDbQuery {
            UserDao.find {
                UserEntity.email eq email and (UserEntity.userType eq UserType.EMAIL.name)
            }.singleOrNull()
        } ?: return

        userDbQuery {
            user.passwordHash = password
        }
    }
}