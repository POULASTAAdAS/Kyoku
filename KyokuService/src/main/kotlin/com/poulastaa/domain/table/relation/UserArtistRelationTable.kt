package com.poulastaa.domain.table.relation

import com.poulastaa.domain.table.ArtistTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object UserArtistRelationTable : Table() {
    val userId = long("userId")
    val userType = varchar("userType", 20)
    val artistId = long("artistId").references(ArtistTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(artistId, userId, userType)
}