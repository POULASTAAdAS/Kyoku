package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EntitySong::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EntitySongInfo(
    @PrimaryKey(autoGenerate = false)
    val songId: Long,
    val releaseYear: Int,
    val popularity: Long = 0,
    val composer: String? = null,
)
