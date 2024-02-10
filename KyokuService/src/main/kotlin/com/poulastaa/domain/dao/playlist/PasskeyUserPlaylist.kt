package com.poulastaa.domain.dao.playlist

import com.poulastaa.data.model.db_table.playlist.PasskeyUserPlaylistTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.*

class PasskeyUserPlaylist(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PasskeyUserPlaylist>(PasskeyUserPlaylistTable)

    var songId by PasskeyUserPlaylistTable.songId
    var userId by PasskeyUserPlaylistTable.userId
}