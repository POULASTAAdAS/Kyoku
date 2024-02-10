package com.poulastaa.domain.dao.user

import com.poulastaa.data.model.db_table.user.EmailAuthUserTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass

class EmailAuthUser(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<EmailAuthUser>(EmailAuthUserTable)

    var userName by EmailAuthUserTable.userName
    var email by EmailAuthUserTable.email
    var password by EmailAuthUserTable.password
    var emailVerified by EmailAuthUserTable.emailVerified
    var profilePic by EmailAuthUserTable.profilePic
    var refreshToken by EmailAuthUserTable.refreshToken
}