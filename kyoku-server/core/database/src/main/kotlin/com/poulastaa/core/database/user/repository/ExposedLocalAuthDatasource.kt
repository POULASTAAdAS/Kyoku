package com.poulastaa.core.database.user.repository

import com.google.gson.Gson
import com.poulastaa.core.database.user.DbManager.kyokuDbQuery
import com.poulastaa.core.database.user.DbManager.userDbQuery
import com.poulastaa.core.database.user.dao.CountryDao
import com.poulastaa.core.database.user.dao.UserDao
import com.poulastaa.core.database.user.entity.CountryEntity
import com.poulastaa.core.database.user.entity.EmailVerificationEntity
import com.poulastaa.core.database.user.entity.UserEntity
import com.poulastaa.core.database.user.entity.UserJWTRelationEntity
import com.poulastaa.core.database.user.mapper.toDbUserDto
import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.ServerUserDto
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.LocalAuthDatasource
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.upperCase
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.SetParams

class ExposedLocalAuthDatasource(
    private val redisPool: JedisPool,
) : LocalAuthDatasource {
    private val countryListMap = mapOf(
        "In" to "India",
        "US" to "United States"
    )

    override suspend fun getCountryIdFromCountryCode(countryCode: String): Int? {
        val country = countryListMap.firstNotNullOfOrNull { (code, value) ->
            if (code.equals(countryCode, ignoreCase = true)) value else null
        } ?: return null

        val cachedCountryId = redisPool.resource.use { jedis ->
            jedis.get("countryId:${country.uppercase()}")
        }

        if (cachedCountryId != null) return cachedCountryId.toInt()

        val dao = kyokuDbQuery {
            CountryDao.find {
                CountryEntity.country.upperCase() eq country.uppercase()
            }.singleOrNull()
        } ?: return null

        redisPool.resource.use { jedis ->
            jedis.set(
                "countryId:${dao.country.uppercase()}",
                dao.id.value.toString(),
                SetParams.setParams().nx().ex(3600) // 1 hour
            )
        }
        return dao.id.value
    }

    override suspend fun getUsersByEmail(
        email: String,
        type: UserType,
    ): DBUserDto? {
        val gson = Gson()
        val redisKey = "user:${type.name}:${email}"

        val userStr = redisPool.resource.use { jedis ->
            jedis.get(redisKey)
        }

        if (userStr != null) return gson.fromJson(userStr, DBUserDto::class.java)

        val user = userDbQuery {
            UserDao.find {
                UserEntity.email eq email and (UserEntity.userType eq type.name)
            }.firstOrNull()
        } ?: return null

        redisPool.resource.use { jedis ->
            jedis.set(
                redisKey,
                gson.toJson(user.toDbUserDto()),
                SetParams.setParams().nx().ex(15 * 60) // 15 minute
            )
        }

        return user.toDbUserDto()
    }

    override suspend fun createGoogleUser(user: ServerUserDto): DBUserDto = createUser(user).toDbUserDto()

    override suspend fun isEmailUserEmailVerified(userId: Long): Boolean {
        val redisKey = "emailVerificationStatus:${userId}"

        val redisStatus = redisPool.resource.use { jedis ->
            jedis.get(redisKey)
        }

        if (redisStatus != null) return redisStatus.toBoolean()

        val dbStatus = userDbQuery {
            EmailVerificationEntity.select {
                EmailVerificationEntity.userId eq userId
            }.map { row ->
                row[EmailVerificationEntity.status]
            }.firstOrNull()
        } == true


        redisPool.resource.use { jedis ->
            jedis.set(
                redisKey,
                dbStatus.toString(),
                SetParams.setParams().nx().ex(15 * 60) // 15 minute
            )
        }

        return dbStatus
    }

    override suspend fun createEmailUser(user: ServerUserDto, refreshToken: String): DBUserDto {
        val dbUser = createUser(user)

        userDbQuery {
            UserJWTRelationEntity.insertIgnore { statement ->
                statement[UserJWTRelationEntity.userId] = dbUser.id.value
                statement[UserJWTRelationEntity.refreshToken] = refreshToken
            }
        }

        return dbUser.toDbUserDto()
    }

    private suspend fun createUser(user: ServerUserDto) = userDbQuery {
        UserDao.new {
            this.email = user.email
            this.username = user.username
            this.userType = user.type.name
            this.passwordHash = user.password
            this.profilePicUrl = user.profilePicUrl
            this.countryId = user.countryId
        }
    }
}