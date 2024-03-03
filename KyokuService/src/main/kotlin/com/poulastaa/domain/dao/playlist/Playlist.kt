package com.poulastaa.domain.dao.playlist

import com.poulastaa.data.model.db_table.PlaylistTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Playlist(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Playlist>(PlaylistTable)

    var name by PlaylistTable.name
}