package com.poulastaa.data.model.db_table

import com.poulastaa.data.model.db_table.EmailAuthUserTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object InvalidRefreshTokenTable : LongIdTable() {
    val createTime: Column<Long> = long("createTime")
    val oldRefreshToken: Column<String> = text("oldRefreshToken")
    val emailUserId = reference("emailUserId", EmailAuthUserTable.id)
}