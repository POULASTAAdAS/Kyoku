package com.poulastaa.kyoku.user.database.repository

import com.poulastaa.kyoku.user.database.entity.EntityCountry
import com.poulastaa.kyoku.user.database.entity.EntityUser
import com.poulastaa.kyoku.user.database.entity.EntityUserPlaylist
import com.poulastaa.kyoku.user.database.entity.EntityUserType
import com.poulastaa.kyoku.user.database.entity.ids.EntityUserPlaylistId
import com.poulastaa.kyoku.user.utils.CountryId
import com.poulastaa.kyoku.user.utils.UserId
import com.poulastaa.kyoku.user.utils.UserTypeId
import org.springframework.data.jpa.repository.JpaRepository

interface UserTypeDataSource : JpaRepository<EntityUserType, UserTypeId> {
    fun findByTypeIgnoreCase(type: String): EntityUserType
}

interface UserDataSource : JpaRepository<EntityUser, UserId>

interface CountryDataSource : JpaRepository<EntityCountry, CountryId> {
    fun getEntityCountryByCodeIgnoreCase(code: String): EntityCountry
}

interface UserPlaylistDataSource : JpaRepository<EntityUserPlaylist, EntityUserPlaylistId>