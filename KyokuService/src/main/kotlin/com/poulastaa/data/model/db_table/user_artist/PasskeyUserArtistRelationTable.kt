package com.poulastaa.data.model.db_table.user_artist

import com.poulastaa.data.model.db_table.ArtistTable
import com.poulastaa.data.model.db_table.user.PasskeyAuthUserTable
import org.jetbrains.exposed.dao.id.LongIdTable

object PasskeyUserArtistRelationTable: LongIdTable() {
    val artistId = integer("artistId").references(ArtistTable.id)
    val userId = long("userId").references(PasskeyAuthUserTable.id)
}