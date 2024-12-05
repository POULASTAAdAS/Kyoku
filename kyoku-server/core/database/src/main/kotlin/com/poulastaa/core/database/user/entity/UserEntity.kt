package com.poulastaa.core.database.user.entity

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.date

object UserEntity : LongIdTable(name = "User") {
    val username = varchar("username", 320)
    val email = varchar("email", 320)
    val passwordHash = varchar("passwordHash", 700)
    val profilePicUrl = varchar("profilePicUrl", 700)
    val bDate = date("bDate").nullable()
    val countryId = integer("countryId")
}