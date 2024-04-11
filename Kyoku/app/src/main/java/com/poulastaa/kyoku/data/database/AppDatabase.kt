package com.poulastaa.kyoku.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.poulastaa.kyoku.data.model.database.table.AlbumPrevTable
import com.poulastaa.kyoku.data.model.database.table.AlbumTable
import com.poulastaa.kyoku.data.model.database.table.ArtistMixTable
import com.poulastaa.kyoku.data.model.database.table.ArtistPreviewSongRelation
import com.poulastaa.kyoku.data.model.database.table.ArtistTable
import com.poulastaa.kyoku.data.model.database.table.DailyMixPrevTable
import com.poulastaa.kyoku.data.model.database.table.DailyMixTable
import com.poulastaa.kyoku.data.model.database.table.FavouriteTable
import com.poulastaa.kyoku.data.model.database.table.FevArtistsMixPreviewTable
import com.poulastaa.kyoku.data.model.database.table.PinnedTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistSongTable
import com.poulastaa.kyoku.data.model.database.table.PlaylistTable
import com.poulastaa.kyoku.data.model.database.table.RecentlyPlayedPrevTable
import com.poulastaa.kyoku.data.model.database.table.SongAlbumRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPlaylistRelationTable
import com.poulastaa.kyoku.data.model.database.table.SongPreviewTable
import com.poulastaa.kyoku.data.model.database.table.SongTable
import com.poulastaa.kyoku.data.model.database.table.internal.InternalItemTable
import com.poulastaa.kyoku.data.model.database.table.internal.InternalPinnedTable
import com.poulastaa.kyoku.data.model.database.table.prev.ArtistSongTable
import com.poulastaa.kyoku.data.model.database.table.prev.PreviewAlbumTable

@Database(
    entities = [
        SongTable::class,
        PlaylistTable::class,
        PlaylistSongTable::class,
        SongPlaylistRelationTable::class,

        PreviewAlbumTable::class,
        ArtistSongTable::class,

        SongPreviewTable::class,
        AlbumPrevTable::class,
        FevArtistsMixPreviewTable::class,
        ArtistTable::class,
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
        InternalPinnedTable::class,
        InternalItemTable::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
    abstract fun internalDao(): InternalDao
}