package com.poulastaa.domain.table.relation

import com.poulastaa.domain.table.AlbumTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserAlbumRelationTable : Table() {
    val userId = long("userId")
    val userType = varchar("userType", 20)
    val albumId = long("albumId").references(AlbumTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(albumId, userId, userType)
}