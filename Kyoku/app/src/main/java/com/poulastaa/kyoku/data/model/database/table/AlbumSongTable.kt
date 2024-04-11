package com.poulastaa.kyoku.data.model.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumSongTable")
data class AlbumSongTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val songId: Long,
    val coverImage: String,
    val masterPlaylistUrl: String,
    val totalTime: String,
    val title: String,
    val artist: String,
    val album: String,
    val date: String
)
