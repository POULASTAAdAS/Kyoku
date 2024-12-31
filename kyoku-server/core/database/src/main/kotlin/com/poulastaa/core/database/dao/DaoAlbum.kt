package com.poulastaa.core.database.dao

import com.poulastaa.core.database.entity.app.EntityAlbum
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DaoAlbum(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DaoAlbum>(EntityAlbum)

    val name by EntityAlbum.name
    var popularity by EntityAlbum.popularity
}