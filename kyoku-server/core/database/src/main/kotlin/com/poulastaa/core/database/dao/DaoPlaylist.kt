package com.poulastaa.core.database.dao

import com.poulastaa.core.database.entity.app.EntityPlaylist
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DaoPlaylist(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DaoPlaylist>(EntityPlaylist)

    var name by EntityPlaylist.name
    var isPublic by EntityPlaylist.isPublic
    var popularity by EntityPlaylist.popularity
}