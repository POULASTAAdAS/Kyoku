package com.poulastaa.core.database.repository

import com.poulastaa.core.database.SQLDbManager.userDbQuery
import com.poulastaa.core.database.dao.DaoUser
import com.poulastaa.core.database.entity.user.EntityUser
import com.poulastaa.core.database.mapper.toDbUserDto
import com.poulastaa.core.domain.model.DBUserDto
import com.poulastaa.core.domain.model.UserType
import com.poulastaa.core.domain.repository.LocalCoreCacheDatasource
import com.poulastaa.core.domain.repository.LocalCoreDatasource
import org.jetbrains.exposed.sql.and

class ExposedLocalCoreDatasource(
    private val cache: LocalCoreCacheDatasource,
) : LocalCoreDatasource {
    override suspend fun getUserByEmail(email: String, userType: UserType): DBUserDto? {
        cache.cacheUsersByEmail(email, userType)?.let { return it }

        val dbUser = userDbQuery {
            DaoUser.find {
                EntityUser.email eq email and (EntityUser.userType eq userType.name)
            }.firstOrNull()
        } ?: return null

        cache.setUserByEmail(email, userType, dbUser.toDbUserDto())

        return dbUser.toDbUserDto()
    }
}