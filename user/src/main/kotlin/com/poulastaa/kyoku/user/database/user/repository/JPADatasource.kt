package com.poulastaa.kyoku.user.database.user.repository

import com.poulastaa.kyoku.user.database.user.entity.EntityCountry
import com.poulastaa.kyoku.user.database.user.entity.EntityUser
import com.poulastaa.kyoku.user.database.user.entity.EntityUserType
import com.poulastaa.kyoku.user.utils.CountryId
import com.poulastaa.kyoku.user.utils.UserId
import com.poulastaa.kyoku.user.utils.UserTypeId
import org.springframework.data.jpa.repository.JpaRepository

interface UserTypeDataSource : JpaRepository<EntityUserType, UserTypeId> {
    fun findByTypeIgnoreCase(type: String): EntityUserType?
}

interface UserDataSource : JpaRepository<EntityUser, UserId> {
    fun findByUserTypeAndEmail(
        userType: EntityUserType,
        email: String,
    ): EntityUser
}

interface CountryDataSource : JpaRepository<EntityCountry, CountryId> {
    fun getEntityCountryByCodeIgnoreCase(code: String): EntityCountry
}