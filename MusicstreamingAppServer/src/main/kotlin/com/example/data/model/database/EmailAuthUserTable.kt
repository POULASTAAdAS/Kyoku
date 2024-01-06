package com.example.data.model.database

import com.example.util.Constants
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object EmailAuthUserTable : IntIdTable() {
    val userName: Column<String> = text("username")
    val email: Column<String> = varchar("email", 320).uniqueIndex()
    val password: Column<String> = varchar("password", 20)
    val emailVerified: Column<Boolean> = bool("emailVerified").default(false)
    val profilePic: Column<String> = varchar("profilePic", 200)
        .default("${Constants.DATABASE_ROOT_DIR}/userProfilePic/defaultProfilePic.png")
}