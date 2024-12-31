package com.poulastaa.core.database.entity.user

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntityUserJWT : Table(name = "UserJWTRelation") {
    val userId = long("userId").uniqueIndex().references(EntityUser.id, onDelete = ReferenceOption.CASCADE)
    val refreshToken = varchar("refreshToken", 1000)
}