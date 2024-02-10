package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class SongTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val coverImage: String,
    val masterPlaylistUrl: String,
    val totalTime: String,
    val title: String,
    val artist: String,
    val album: String,
    val genre: String,
    val composer: String,
    val publisher: String,
    val albumArtist: String,
    val description: String,
    val track: String,
    val date: String
)
