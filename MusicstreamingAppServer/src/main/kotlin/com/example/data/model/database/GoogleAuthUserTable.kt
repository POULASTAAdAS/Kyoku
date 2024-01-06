package com.example.data.model.database

import com.example.util.Constants.DATABASE_ROOT_DIR
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object GoogleAuthUserTable : IntIdTable() {
    val userName: Column<String> = text("username")
    val email: Column<String> = varchar("email", 320).uniqueIndex()
    val sub: Column<String> = varchar("sub", 20).uniqueIndex()
    val profilePic: Column<String> = varchar("profilePic", 200)
        .default("$DATABASE_ROOT_DIR/userProfilePic/defaultProfilePic.png")
}