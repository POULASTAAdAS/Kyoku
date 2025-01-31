package com.poulastaa.kyoku.shardmanager.app.core.database.dao.kyoku

import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.EntityAlbum
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DaoAlbum(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DaoAlbum>(EntityAlbum)

    val name by EntityAlbum.name
    var popularity by EntityAlbum.popularity
}