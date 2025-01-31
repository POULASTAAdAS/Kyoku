package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku_user

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.date

object EntityUser : LongIdTable(name = "User") {
    val username = varchar("username", 320)
    val userType = varchar("userType", 7)
    val email = varchar("email", 320)
    val passwordHash = varchar("passwordHash", 700)
    val profilePicUrl = varchar("profilePicUrl", 700).nullable()
    val bDate = date("bDate").nullable()
    val countryId = integer("countryId")

    init {
        uniqueIndex(userType, email)
    }
}