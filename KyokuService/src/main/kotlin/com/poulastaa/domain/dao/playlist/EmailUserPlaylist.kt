package com.poulastaa.domain.dao.playlist

import com.poulastaa.data.model.db_table.playlist.EmailUserPlaylistTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class EmailUserPlaylist(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EmailUserPlaylist>(EmailUserPlaylistTable)

    var playlistId by EmailUserPlaylistTable.playlistId
    var songId by EmailUserPlaylistTable.songId
    var userId by EmailUserPlaylistTable.userId
}