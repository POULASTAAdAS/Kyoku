package com.poulastaa.core.database.dao

import com.poulastaa.core.database.entity.app.EntityArtist
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DaoArtist(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DaoArtist>(EntityArtist)

    val name by EntityArtist.name
    val coverImage by EntityArtist.coverImage
    var popularity by EntityArtist.popularity
}