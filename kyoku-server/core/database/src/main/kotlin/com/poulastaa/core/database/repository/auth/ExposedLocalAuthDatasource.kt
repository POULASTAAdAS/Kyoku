package com.poulastaa.core.database.repository.auth

import com.poulastaa.core.database.SQLDbManager.kyokuDbQuery
import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.DaoCountry
import com.poulastaa.core.database.dao.DaoUser
import com.poulastaa.core.database.entity.app.EntityCountry
import com.poulastaa.core.database.entity.user.EntityUser
import com.poulastaa.core.database.entity.user.RelationEntityUserArtist
import com.poulastaa.core.database.entity.user.RelationEntityUserGenre
import com.poulastaa.core.database.entity.user.RelationEntityUserJWT
import com.poulastaa.core.database.mapper.toDbUserDto
import com.poulastaa.core.domain.model.DtoDBUser
import com.poulastaa.core.domain.model.DtoServerUser
import com.poulastaa.core.domain.model.MailType
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.LocalCoreDatasource
import com.poulastaa.core.domain.repository.auth.Email
import com.poulastaa.core.domain.repository.auth.LocalAuthCacheDatasource
import com.poulastaa.core.domain.repository.auth.LocalAuthDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.upperCase
import org.jetbrains.exposed.sql.upsert

class ExposedLocalAuthDatasource(
    private val coreDB: LocalCoreDatasource,
    private val cache: LocalAuthCacheDatasource,
) : LocalAuthDatasource {
    private val countryListMap = mapOf(
        "In" to "India",
        "US" to "United States of America" // UNITED STATES
    )

    override suspend fun getCountryIdFromCountryCode(countryCode: String): Int? {
        val country = countryListMap.firstNotNullOfOrNull { (code, value) ->
            if (code.equals(countryCode, ignoreCase = true)) value else null
        } ?: return null

        cache.cachedCountryId(country.uppercase())?.let { return it }

        val dao = kyokuDbQuery {
            DaoCountry.find {
                EntityCountry.country.upperCase() eq country.uppercase()
            }.singleOrNull()
        } ?: return null

        cache.setCountryId(dao.country.uppercase(), dao.id.value.toString())

        return dao.id.value
    }

    override suspend fun getUsersByEmail(email: String, type: UserType): DtoDBUser? =
        coreDB.getUserByEmail(email, type)

    override suspend fun createUser(user: DtoServerUser, isDbStore: Boolean): DtoDBUser {
        val dbUser = when {
            (user.type == UserType.GOOGLE || user.type == UserType.EMAIL) && isDbStore -> {
                userDbQuery {
                    DaoUser.new { // todo fix insert ignore
                        this.email = user.email
                        this.username = user.username
                        this.userType = user.type.name
                        this.passwordHash = user.password
                        this.profilePicUrl = user.profilePicUrl
                        this.countryId = user.countryId
                    }
                }.toDbUserDto()
            }

            else -> DtoDBUser(
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
            RelationEntityUserJWT.upsert {
                it[this.userId] = user.id
                it[this.refreshToken] = token
            }
        }
    }

    override fun isResetPasswordTokenUsed(token: String): Boolean = cache.isResetPasswordTokenUsed(token)

    override suspend fun updatePassword(email: Email, password: String) {
        val user = userDbQuery {
            DaoUser.find {
                EntityUser.email eq email and (EntityUser.userType eq UserType.EMAIL.name)
            }.singleOrNull()
        } ?: return

        userDbQuery {
            user.passwordHash = password
        }
    }

    override suspend fun isSavedGenre(userId: Long): Boolean {
        val entrys = userDbQuery {
            RelationEntityUserGenre.select {
                RelationEntityUserGenre.userId eq userId
            }.count()
        }

        return entrys > 0
    }

    override suspend fun isSavedArtist(userId: Long): Boolean {
        val entrys = userDbQuery {
            RelationEntityUserArtist.select {
                RelationEntityUserArtist.userId eq userId
            }.count()
        }

        return entrys > 0
    }
}