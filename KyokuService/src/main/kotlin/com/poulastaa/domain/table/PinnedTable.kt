package com.poulastaa.domain.table

import org.jetbrains.exposed.sql.Table

object PinnedTable : Table() {
    val id = long("id")
    val userId = long("userId")
    val userType = varchar("userType", 25)
    val pinnedType = varchar("pinnedType", 20)

    override val primaryKey = PrimaryKey(id, userId, userType, pinnedType)
}