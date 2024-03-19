package com.poulastaa.domain.dao

import com.poulastaa.data.model.db_table.AlbumTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Album(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Album>(AlbumTable)

    val name by AlbumTable.name
    var points by AlbumTable.points
}