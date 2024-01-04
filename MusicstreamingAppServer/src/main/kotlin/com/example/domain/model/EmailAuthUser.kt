package com.example.domain.model

import com.example.data.model.database.EmailAuthUserTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class EmailAuthUser(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EmailAuthUser>(EmailAuthUserTable)

    var userName by EmailAuthUserTable.userName
    var email by EmailAuthUserTable.email
    var password by EmailAuthUserTable.password
    var emailVerified by EmailAuthUserTable.emailVerified
}