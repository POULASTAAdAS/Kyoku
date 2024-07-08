package com.poulastaa.core.database.entity.popular_artist_song

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PopularSongArtistEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val name: String,
    val coverImage: String,
)
