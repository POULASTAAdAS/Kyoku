package com.poulastaa.data.model.db_table

import com.poulastaa.utils.Constants.PROFILE_PIC_ROOT_DIR
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object EmailAuthUserTable : LongIdTable() {
    val userName: Column<String> = text("username")
    val email: Column<String> = varchar("email", 320).uniqueIndex()
    val password: Column<String> = varchar("password", 20)
    val emailVerified: Column<Boolean> = bool("emailVerified").default(false)
    val profilePic: Column<String> = varchar("profilePic", 200)
        .default("$PROFILE_PIC_ROOT_DIR/defaultProfilePic.png")
    val refreshToken: Column<String> = text("refreshToken")
    val bDate: Column<Long?> = long("bDate").nullable()
    val countryId: Column<Int> = integer("countryId").references(CountryTable.id)
}