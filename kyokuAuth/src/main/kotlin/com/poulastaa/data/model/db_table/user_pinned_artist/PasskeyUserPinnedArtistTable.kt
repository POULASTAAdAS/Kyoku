package com.poulastaa.data.model.db_table.user_pinned_artist

import com.poulastaa.data.model.db_table.ArtistTable
import com.poulastaa.data.model.db_table.user.PasskeyAuthUserTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object PasskeyUserPinnedArtistTable : Table() {
    val userId = long("userId").references(PasskeyAuthUserTable.id, onDelete = ReferenceOption.CASCADE)
    val artistId = integer("artistId").references(ArtistTable.id)

    override val primaryKey = PrimaryKey(userId, artistId)
}