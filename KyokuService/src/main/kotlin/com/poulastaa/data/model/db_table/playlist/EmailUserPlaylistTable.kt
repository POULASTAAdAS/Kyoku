package com.poulastaa.data.model.db_table.playlist

import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.db_table.user.EmailAuthUserTable
import org.jetbrains.exposed.dao.id.UUIDTable

object EmailUserPlaylistTable : UUIDTable() {
    val playlistId = long("playlistId").references(PlaylistTable.id)
    val songId = long("songId").references(SongTable.id)
    val userId = long("userId").references(EmailAuthUserTable.id)
}