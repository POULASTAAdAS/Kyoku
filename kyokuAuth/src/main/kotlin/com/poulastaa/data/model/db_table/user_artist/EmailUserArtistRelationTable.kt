package com.poulastaa.data.model.db_table.user_artist

import com.poulastaa.data.model.db_table.ArtistTable
import com.poulastaa.data.model.db_table.user.EmailAuthUserTable
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object EmailUserArtistRelationTable : LongIdTable() {
    val artistId = integer("artistId").references(ArtistTable.id)
    val userId = long("userId").references(EmailAuthUserTable.id, onDelete = ReferenceOption.CASCADE)
}