package com.poulastaa.data.dao.other

import com.poulastaa.domain.table.other.LogInVerificationMailTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class LogInVerificationMail(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<LogInVerificationMail>(LogInVerificationMailTable)

    var email by LogInVerificationMailTable.email
    var isVerified by LogInVerificationMailTable.isVerified
}