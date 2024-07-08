package com.poulastaa.core.database.entity.relation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.poulastaa.core.database.entity.popular_artist_song.ArtistSongEntity
import com.poulastaa.core.database.entity.popular_artist_song.PopularSongArtistEntity

@Entity(
    primaryKeys = ["artistId", "songId"],
    foreignKeys = [
        ForeignKey(
            entity = PopularSongArtistEntity::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ArtistSongEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PopularArtistSongRelation(
    @ColumnInfo(index = true)
    val artistId: Long,
    @ColumnInfo(index = true)
    val songId: Long,
)
