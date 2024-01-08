package com.example.domain.dao

import com.example.data.model.database.EmailAuthUserTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class EmailAuthUser(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<EmailAuthUser>(EmailAuthUserTable)

    var userName by EmailAuthUserTable.userName
    var email by EmailAuthUserTable.email
    var password by EmailAuthUserTable.password
    var emailVerified by EmailAuthUserTable.emailVerified
    var profilePic by EmailAuthUserTable.profilePic
}