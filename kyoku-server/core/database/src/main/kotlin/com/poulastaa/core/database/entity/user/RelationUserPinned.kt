package com.poulastaa.core.database.entity.user

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationUserPinned : Table(name = "UserPinned") {
    val userId = long("userId").references(EntityUser.id, onDelete = ReferenceOption.CASCADE)
    val otherId = long("otherId")
    val pinnedType = varchar("pinnedType", 9).index()

    override val primaryKey = PrimaryKey(userId, otherId, pinnedType)
}