package com.poulastaa.core.database.entity

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object EmailVerificationEntity : Table(name = "EmailVerification") {
    val userId = long("userId").uniqueIndex().references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val status = bool("status").default(false)
}