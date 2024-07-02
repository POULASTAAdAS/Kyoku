package com.poulastaa.domain.table.relation

import com.poulastaa.domain.table.GenreTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserGenreRelationTable : Table() {
    val userId = long("userId")
    val userType = varchar("userType", 20)
    val genreId = integer("genreId").references(GenreTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey: PrimaryKey = PrimaryKey(userId, userType, genreId)
}