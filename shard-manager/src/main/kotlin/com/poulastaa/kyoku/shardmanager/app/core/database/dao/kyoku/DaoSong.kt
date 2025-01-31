package com.poulastaa.kyoku.shardmanager.app.core.database.dao.kyoku

import com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku.EntitySong
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DaoSong(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DaoSong>(EntitySong)

    val title by EntitySong.title
    val poster by EntitySong.poster
    val masterPlaylist by EntitySong.masterPlaylist
}