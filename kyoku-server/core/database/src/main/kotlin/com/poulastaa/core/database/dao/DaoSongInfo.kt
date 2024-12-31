package com.poulastaa.core.database.dao

import com.poulastaa.core.database.entity.app.EntitySongInfo
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DaoSongInfo(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DaoSongInfo>(EntitySongInfo)

    val songId by EntitySongInfo.songId
    val releaseYear by EntitySongInfo.releaseYear
    val composer by EntitySongInfo.composer
    val popularity by EntitySongInfo.popularity
}