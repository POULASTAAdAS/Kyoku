package com.poulastaa.core.database.user.repository

import com.google.gson.Gson
import com.poulastaa.core.database.user.dao.CountryDao
import com.poulastaa.core.database.user.dao.UserDao
import com.poulastaa.core.database.user.entity.CountryEntity
import com.poulastaa.core.database.user.entity.UserEntity
import com.poulastaa.core.database.user.mapper.toDbUserDto
import com.poulastaa.core.database.util.query
import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.ServerUserDto
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.LocalAuthDatasource
import org.jetbrains.exposed.sql.upperCase
import redis.clients.jedis.JedisPool
import redis.clients.jedis.params.SetParams

class ExposedLocalAuthDatasource(
    private val redis: JedisPool,
) : LocalAuthDatasource {

    private val countryListMap = mapOf(
        "In" to "India",
        "US" to "United States"
    )

    override suspend fun getCountryId(countryCode: String): Int? {
        val country = countryListMap.firstNotNullOfOrNull { (code, value) ->
            if (code.equals(countryCode, ignoreCase = true)) value else null
        } ?: return null

        val cachedCountryId = redis.resource.use { jedis ->
            jedis.get("countryId:${country.uppercase()}")
        }

        if (cachedCountryId != null) return cachedCountryId.toInt()


        val dao = query {
            CountryDao.find {
                CountryEntity.country.upperCase() eq country.uppercase()
            }.singleOrNull()
        } ?: return null

        redis.resource.use { jedis ->
            jedis.set(
                "countryId:${dao.country.uppercase()}",
                dao.id.value.toString(),
                SetParams.setParams().nx().ex(3600)
            )
        }
        return dao.id.value
    }

    override suspend fun getUsersByEmail(
        email: String,
        type: UserType,
    ): List<DBUserDto> {
        val user = redis.resource.use { jedis ->
            jedis.get("email:${type.name}")
        }

        if (user != null) return listOf(Gson().fromJson(user.toString(), DBUserDto::class.java))

        val users = query {
            UserDao.find {
                UserEntity.email eq email
            }.map { it.toDbUserDto() }
        }

        // todo fix userEntity add user type
        // todo add data to redis catch

        return users
    }

    override suspend fun createGoogleUser(user: ServerUserDto): DBUserDto =
        DBUserDto("", "", "", "", -1, java.time.LocalDate.now())

    override suspend fun createEmailUser(payload: ServerUserDto, refreshToken: String): DBUserDto =
        DBUserDto("", "", "", "", -1, java.time.LocalDate.now())
}