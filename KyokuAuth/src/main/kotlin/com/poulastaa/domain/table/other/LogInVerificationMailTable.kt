package com.poulastaa.domain.table.other

import org.jetbrains.exposed.dao.id.LongIdTable

object LogInVerificationMailTable : LongIdTable() {
    val email = varchar("email", 255)
    val isVerified = bool("isVerified").default(false)
}