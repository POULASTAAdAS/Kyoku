package com.poulastaa.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EntityAlbum::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EntitySuggestedAlbum(
    @PrimaryKey(autoGenerate = false)
    val albumId: Long,
)
