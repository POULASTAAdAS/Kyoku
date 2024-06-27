package com.poulastaa.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.GetSpotifyPlaylistDao
import com.poulastaa.core.database.entity.ArtistEntity
import com.poulastaa.core.database.entity.PlaylistEntity
import com.poulastaa.core.database.entity.SongEntity
import com.poulastaa.core.database.entity.relation.SongArtistRelationEntity
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity

@Database(
    entities = [
        SongEntity::class,
        ArtistEntity::class,
        PlaylistEntity::class,
        SongArtistRelationEntity::class,
        SongPlaylistRelationEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class KyokuDatabase : RoomDatabase() {
    abstract val commonDao: CommonDao
    abstract val getSpotifyPlaylistDao: GetSpotifyPlaylistDao
}