package com.poulastaa.domain.dao

import com.poulastaa.data.model.db_table.SongTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Song(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Song>(SongTable)

    val coverImage by SongTable.coverImage
    val masterPlaylistPath by SongTable.masterPlaylistPath
    val totalTime by SongTable.totalTime
    val title by SongTable.title
    val artist by SongTable.artist
    val album by SongTable.album
    val genre by SongTable.genre
    val composer by SongTable.composer
    val publisher by SongTable.publisher
    val albumArtist by SongTable.album_artist
    val description by SongTable.description
    val track  by SongTable.track
    val date by SongTable.date
}