package com.poulastaa.kyoku.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.poulastaa.kyoku.data.model.database.table.AlbumTable
import com.poulastaa.kyoku.data.model.database.table.ArtistPreviewSongRelation
import com.poulastaa.kyoku.data.model.database.table.ArtistTable
import com.poulastaa.kyoku.data.model.database.table.DailyMixPrevTable
import com.poulastaa.kyoku.data.model.database.table.FevArtistsMixPreviewTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.SongPlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPreviewTable
import com.poulastaa.kyoku.data.model.database.table.SongTable

@Database(
    entities = [
        SongTable::class,
        PlaylistTable::class,
        SongPlaylistRelationTable::class,
        AlbumTable::class,
        FevArtistsMixPreviewTable::class,
        SongPreviewTable::class,
        ArtistTable::class,
        ArtistPreviewSongRelation::class,
        DailyMixPrevTable::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}