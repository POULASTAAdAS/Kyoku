package com.poulastaa.kyoku.shardmanager.app.core.database.entity.kyoku

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object RelationSongPlaylist : Table(name = "SongPlaylist") {
    val songId = long("songId").references(EntitySong.id, onDelete = ReferenceOption.CASCADE)
    val playlistId = long("playlistId").references(EntityPlaylist.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(songId, playlistId)
}