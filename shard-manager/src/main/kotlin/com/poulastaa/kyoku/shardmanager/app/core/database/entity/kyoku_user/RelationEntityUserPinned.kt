package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku_user

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationEntityUserPinned : Table(name = "UserPinned") {
    val userId = long("userId").references(EntityUser.id, onDelete = ReferenceOption.CASCADE)
    val otherId = long("otherId")
    val pinnedType = varchar("pinnedType", 9).index()

    override val primaryKey = PrimaryKey(userId, otherId, pinnedType)
}