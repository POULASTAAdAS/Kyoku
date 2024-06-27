package com.poulastaa.core.database.entity.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.poulastaa.core.database.entity.ArtistEntity
import com.poulastaa.core.database.entity.SongEntity

@Entity(
    tableName = "SongArtistRelationEntity",
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
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
    @ColumnInfo(index = true)
    val songId: Long,
    @ColumnInfo(index = true)
    val artistId: Long,
)
