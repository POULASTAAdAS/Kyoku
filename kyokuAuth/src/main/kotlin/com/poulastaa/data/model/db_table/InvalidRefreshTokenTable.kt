package com.poulastaa.data.model.db_table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object InvalidRefreshTokenTable : LongIdTable() {
    val createTime = long("createTime").default(System.currentTimeMillis())
    val oldRefreshToken = text("oldRefreshToken")
    val userId = long("userId").references(EmailAuthUserTable.id, onDelete = ReferenceOption.CASCADE)
}