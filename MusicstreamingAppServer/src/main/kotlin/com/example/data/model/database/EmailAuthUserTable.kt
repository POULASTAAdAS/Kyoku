package com.example.data.model.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object EmailAuthUserTable : IntIdTable() {
    val userName: Column<String> = text("username")
    val email: Column<String> = varchar("email", 320).uniqueIndex()
    val password: Column<String> = varchar("password", 20)
    val emailVerified: Column<Boolean> = bool("emailVerified").default(false)
}