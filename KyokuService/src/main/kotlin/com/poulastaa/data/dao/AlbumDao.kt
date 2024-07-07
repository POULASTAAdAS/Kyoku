package com.poulastaa.data.dao

import com.poulastaa.domain.table.AlbumTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class AlbumDao(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<AlbumDao>(AlbumTable)

    val name by AlbumTable.name
    val points by AlbumTable.points
}