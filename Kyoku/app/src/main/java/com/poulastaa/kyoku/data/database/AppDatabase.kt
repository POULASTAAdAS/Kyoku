package com.poulastaa.kyoku.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.poulastaa.kyoku.data.model.database.table.AlbumPrevTable
import com.poulastaa.kyoku.data.model.database.table.AlbumPreviewSongRelationTable
import com.poulastaa.kyoku.data.model.database.table.AlbumTable
import com.poulastaa.kyoku.data.model.database.table.ArtistMixTable
import com.poulastaa.kyoku.data.model.database.table.ArtistPrevTable
import com.poulastaa.kyoku.data.model.database.table.ArtistPreviewSongRelation
import com.poulastaa.kyoku.data.model.database.table.DailyMixPrevTable
import com.poulastaa.kyoku.data.model.database.table.DailyMixTable
import com.poulastaa.kyoku.data.model.database.table.FavouriteTable
import com.poulastaa.kyoku.data.model.database.table.FevArtistsMixPreviewTable
import com.poulastaa.kyoku.data.model.database.table.InternalPinnedTable
import com.poulastaa.kyoku.data.model.database.table.PinnedTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.RecentlyPlayedPrevTable
import com.poulastaa.kyoku.data.model.database.table.SongAlbumRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPreviewTable
import com.poulastaa.kyoku.data.model.database.table.SongTable

@Database(
    entities = [
        SongTable::class,
        PlaylistTable::class,
        SongPlaylistRelationTable::class,

        SongPreviewTable::class,
        AlbumPrevTable::class,
        AlbumPreviewSongRelationTable::class,
        FevArtistsMixPreviewTable::class,
        ArtistPrevTable::class,
        ArtistPreviewSongRelation::class,
        DailyMixPrevTable::class,

        FavouriteTable::class,
        AlbumTable::class,
        SongAlbumRelationTable::class,
        RecentlyPlayedPrevTable::class,

        PinnedTable::class,
        DailyMixTable::class,
        ArtistMixTable::class,

        // internal tables
        InternalPinnedTable::class
    ],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
    abstract fun internalDao(): InternalDao
}