package com.poulastaa.core.database.dao

import com.poulastaa.core.database.entity.user.EntityUser
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DaoUser(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DaoUser>(EntityUser)

    var username by EntityUser.username
    var email by EntityUser.email
    var userType by EntityUser.userType
    var passwordHash by EntityUser.passwordHash
    var profilePicUrl by EntityUser.profilePicUrl
    var bDate by EntityUser.bDate
    var countryId by EntityUser.countryId
}