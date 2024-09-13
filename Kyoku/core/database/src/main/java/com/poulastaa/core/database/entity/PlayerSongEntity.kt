package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PlayerSongEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long = -1,
    val title: String = "",
    val artist: String = "",
    val coverImage: String = "",
    val masterPlaylistUrl: String = "",
    val album: String = "",
    val releaseYear: Int = -1,
    val currentProgress: String = "0:0",
    val endTime: String = "0:0",
    val progress: Float = 0f,
    val isPlaying: Boolean = false,
    val isInFavourite: Boolean = false,
    val primary: String = "",
    val secondary: String = "",
    val background: String = ""
)
