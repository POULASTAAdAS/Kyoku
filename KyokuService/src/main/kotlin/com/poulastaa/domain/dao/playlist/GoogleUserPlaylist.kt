package com.poulastaa.domain.dao.playlist

import com.poulastaa.data.model.db_table.playlist.GoogleUserPlaylistTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class GoogleUserPlaylist(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<GoogleUserPlaylist>(GoogleUserPlaylistTable)

    var songId by GoogleUserPlaylistTable.songId
    var userId by GoogleUserPlaylistTable.sub
}