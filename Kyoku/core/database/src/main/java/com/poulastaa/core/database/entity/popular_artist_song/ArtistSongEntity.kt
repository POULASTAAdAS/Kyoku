package com.poulastaa.core.database.entity.popular_artist_song

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ArtistSongEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val title: String,
    val coverImage: String,
)
