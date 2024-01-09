package com.example.data.model.database_table

import com.example.util.Constants.PROFILE_PIC_ROOT_DIR
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object GoogleAuthUserTable : LongIdTable() {
    val userName: Column<String> = text("username")
    val email: Column<String> = varchar("email", 320).uniqueIndex()
    val sub: Column<String> = varchar("sub", 30).uniqueIndex()
    val profilePic: Column<String> = varchar("profilePic", 200)
        .default("$PROFILE_PIC_ROOT_DIR/defaultProfilePic.png")
}