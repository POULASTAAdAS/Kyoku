package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = [
        "genreId",
        "artistId"
    ],
    foreignKeys = [
        ForeignKey(
            entity = EntityArtist::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EntityGenre::class,
            parentColumns = ["id"],
            childColumns = ["genreId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EntityRelationArtistGenre(
    @ColumnInfo(index = true)
    val artistId: Long,
    val genreId: Int,
)
