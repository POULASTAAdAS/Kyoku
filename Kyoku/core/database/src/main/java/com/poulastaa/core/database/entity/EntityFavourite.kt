package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EntitySong::class,
            parentColumns = ["id"],
            childColumns = ["songId"]
        )
    ]
)
data class EntityFavourite(
    @PrimaryKey(autoGenerate = false)
    val songId: Long,
)
