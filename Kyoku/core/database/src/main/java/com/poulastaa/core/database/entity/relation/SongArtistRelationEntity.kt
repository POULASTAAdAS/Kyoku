package com.poulastaa.core.database.entity.relation

import androidx.room.Entity
import androidx.room.ForeignKey
import com.poulastaa.core.database.entity.ArtistEntity
import com.poulastaa.core.database.entity.SonEntity

@Entity(
    tableName = "SongArtistRelationEntity",
    foreignKeys = [
        ForeignKey(
            entity = SonEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ArtistEntity::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    primaryKeys = ["songId", "artistId"]
)
data class SongArtistRelationEntity(
    val songId: Long,
    val artistId: Long,
)
