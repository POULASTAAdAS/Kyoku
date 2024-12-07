package com.poulastaa.core.database.user.dao

import com.poulastaa.core.database.user.entity.UserEntity
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserDao>(UserEntity)

    var username by UserEntity.username
    var email by UserEntity.email
    var userType by UserEntity.userType
    var passwordHash by UserEntity.passwordHash
    var profilePicUrl by UserEntity.profilePicUrl
    var bDate by UserEntity.bDate
    var countryId by UserEntity.countryId
}