package com.poulastaa.kyoku.auth.database.repository

import com.poulastaa.kyoku.auth.database.entity.EntityCountry
import com.poulastaa.kyoku.auth.database.entity.EntityUser
import com.poulastaa.kyoku.auth.database.entity.EntityUserType
import com.poulastaa.kyoku.auth.database.entity.EntityJWTToken
import com.poulastaa.kyoku.auth.utils.CountryId
import com.poulastaa.kyoku.auth.utils.UserId
import com.poulastaa.kyoku.auth.utils.UserTypeId
import org.springframework.data.jpa.repository.JpaRepository

interface UserTypeDataSource : JpaRepository<EntityUserType, UserTypeId> {
    fun findByTypeIgnoreCase(type: String): EntityUserType
}

interface UserDataSource : JpaRepository<EntityUser, UserId> {
    fun getEntityUserByEmailAndUserTypeId(email: String, userTypeId: UserTypeId): EntityUser?
}

interface UserJWTTokenDataSource : JpaRepository<EntityJWTToken, UserId>

interface CountryDataSource : JpaRepository<EntityCountry, CountryId> {
    fun getEntityCountryByCodeIgnoreCase(code: String): EntityCountry
}