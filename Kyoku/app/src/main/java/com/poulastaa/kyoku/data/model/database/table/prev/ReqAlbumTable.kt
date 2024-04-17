package com.poulastaa.kyoku.data.model.database.table.prev

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ReqAlbumSongTable")
data class ReqAlbumSongTable(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val albumId: Long = 0,
    val albumName: String,
    val songId: Long,
    val coverImage: String,
    val masterPlaylistUrl: String,
    val totalTime: String,
    val title: String,
    val artist: String,
    val date: String
)
