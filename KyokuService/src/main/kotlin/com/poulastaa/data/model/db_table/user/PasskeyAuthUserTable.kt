package com.poulastaa.data.model.db_table.user

import com.poulastaa.utils.Constants.DEFAULT_PROFILE_PIC
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object PasskeyAuthUserTable : LongIdTable() {
    val userId: Column<String> = varchar("userId", 300).uniqueIndex()
    val userName: Column<String> = text("username")
    val email: Column<String> = varchar("email", 320).uniqueIndex()
    val profilePic: Column<String> = varchar("profilePic", 200)
        .default(DEFAULT_PROFILE_PIC)
    val bDate: Column<Long?> = long("bDate").nullable()
}