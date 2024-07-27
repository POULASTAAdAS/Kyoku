package com.poulastaa.core.database.entity.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.poulastaa.core.database.entity.AlbumEntity
import com.poulastaa.core.database.entity.SongEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AlbumEntity::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE,
        )
    ],
    primaryKeys = ["albumId", "songId"]
)
data class SongAlbumRelationEntity(
    @ColumnInfo(index = true)
    val albumId: Long,
    @ColumnInfo(index = true)
    val songId: Long,
)
