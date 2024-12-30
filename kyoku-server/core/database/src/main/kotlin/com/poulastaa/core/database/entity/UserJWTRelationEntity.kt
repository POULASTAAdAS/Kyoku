package com.poulastaa.core.database.entity

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserJWTRelationEntity : Table(name = "UserJWTRelation") {
    val userId = long("userId").uniqueIndex().references(UserEntity.id, onDelete = ReferenceOption.CASCADE)
    val refreshToken = varchar("refreshToken", 1000)
}