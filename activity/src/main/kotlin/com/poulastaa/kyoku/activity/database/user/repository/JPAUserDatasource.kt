package com.poulastaa.kyoku.activity.database.user.repository

import com.poulastaa.kyoku.activity.database.user.entity.EntityCountry
import com.poulastaa.kyoku.activity.database.user.entity.EntityUser
import com.poulastaa.kyoku.activity.database.user.entity.EntityUserType
import com.poulastaa.kyoku.activity.utils.CountryId
import com.poulastaa.kyoku.activity.utils.UserId
import com.poulastaa.kyoku.activity.utils.UserTypeId
import org.springframework.data.jpa.repository.JpaRepository

interface UserDatasource : JpaRepository<EntityUser, UserId>
interface UserTypeDatasource : JpaRepository<EntityUserType, UserTypeId>
interface UserCountryDatasource : JpaRepository<EntityCountry, CountryId>