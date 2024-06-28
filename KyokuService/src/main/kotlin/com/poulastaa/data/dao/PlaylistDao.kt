package com.poulastaa.data.dao

import com.poulastaa.domain.table.PlaylistTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class PlaylistDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<PlaylistDao>(PlaylistTable)

    var name by PlaylistTable.name
    var points by PlaylistTable.points
}