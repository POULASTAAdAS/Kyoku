package com.poulastaa.data.model.db_table.playlist

import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.db_table.user.GoogleAuthUserTable
import org.jetbrains.exposed.dao.id.UUIDTable

object GoogleUserPlaylistTable : UUIDTable() {
    val songId = long("songId").references(SongTable.id)
    val sub = varchar("sub", 30). references(GoogleAuthUserTable.sub)
}