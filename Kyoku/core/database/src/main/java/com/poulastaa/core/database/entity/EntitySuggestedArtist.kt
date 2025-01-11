package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EntityArtist::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EntitySuggestedArtist(
    @PrimaryKey(autoGenerate = false)
    val artistId: Long,
)
