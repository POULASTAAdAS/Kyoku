package com.poulastaa.data.model.db_table.user_playlist

import com.poulastaa.data.model.db_table.PlaylistTable
import com.poulastaa.data.model.db_table.SongTable
import com.poulastaa.data.model.db_table.user.PasskeyAuthUserTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object PasskeyUserPlaylistTable : UUIDTable() {
    val playlistId = long("playlistId").references(PlaylistTable.id, onDelete = ReferenceOption.CASCADE)
    val songId = long("songId").references(SongTable.id)
    val userId = long("userId").references(PasskeyAuthUserTable.id, onDelete = ReferenceOption.CASCADE)
}