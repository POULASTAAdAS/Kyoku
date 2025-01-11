package com.poulastaa.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = [
        "albumId",
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
            entity = EntityAlbum::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EntityRelationArtistAlbum(
    @ColumnInfo(index = true)
    val artistId: Long,
    @ColumnInfo(index = true)
    val albumId: Long,
)
