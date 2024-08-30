package com.poulastaa.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.poulastaa.core.database.dao.AddPlaylistDao
import com.poulastaa.core.database.dao.AddToPlaylistDao
import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.GetSpotifyPlaylistDao
import com.poulastaa.core.database.dao.HomeDao
import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.dao.SettingDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.dao.WorkDao
import com.poulastaa.core.database.entity.AlbumEntity
import com.poulastaa.core.database.entity.ArtistEntity
import com.poulastaa.core.database.entity.DayTypeSongEntity
import com.poulastaa.core.database.entity.DayTypeSongPrevEntity
import com.poulastaa.core.database.entity.FavouriteArtistMixEntity
import com.poulastaa.core.database.entity.FavouriteArtistMixPrevEntity
import com.poulastaa.core.database.entity.FavouriteEntity
import com.poulastaa.core.database.entity.PinnedEntity
import com.poulastaa.core.database.entity.PlaylistEntity
import com.poulastaa.core.database.entity.PopularAlbumPrevEntity
import com.poulastaa.core.database.entity.PopularSongFromYourTimeEntity
import com.poulastaa.core.database.entity.PopularSongFromYourTimePrevEntity
import com.poulastaa.core.database.entity.PopularSongMixEntity
import com.poulastaa.core.database.entity.PopularSongMixPrevEntity
import com.poulastaa.core.database.entity.PopularSuggestArtistEntity
import com.poulastaa.core.database.entity.SongEntity
import com.poulastaa.core.database.entity.popular_artist_song.ArtistSongEntity
import com.poulastaa.core.database.entity.popular_artist_song.PopularSongArtistEntity
import com.poulastaa.core.database.entity.relation.PopularArtistSongRelationEntity
import com.poulastaa.core.database.entity.relation.SongAlbumRelationEntity
import com.poulastaa.core.database.entity.relation.SongArtistRelationEntity
import com.poulastaa.core.database.entity.relation.SongPlaylistRelationEntity

@Database(
    entities = [
        SongEntity::class,
        ArtistEntity::class,
        PlaylistEntity::class,
        SongArtistRelationEntity::class,
        SongPlaylistRelationEntity::class,

        PopularSongMixPrevEntity::class,
        PopularSongFromYourTimePrevEntity::class,
        FavouriteArtistMixPrevEntity::class,
        DayTypeSongPrevEntity::class,

        PopularSongMixEntity::class,
        PopularSongFromYourTimeEntity::class,
        FavouriteArtistMixEntity::class,
        DayTypeSongEntity::class,

        PopularAlbumPrevEntity::class,
        PopularSuggestArtistEntity::class,

        ArtistSongEntity::class,
        PopularSongArtistEntity::class,
        PopularArtistSongRelationEntity::class,

        AlbumEntity::class,
        SongAlbumRelationEntity::class,

        FavouriteEntity::class,
        PinnedEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class KyokuDatabase : RoomDatabase() {
    abstract val commonDao: CommonDao
    abstract val getSpotifyPlaylistDao: GetSpotifyPlaylistDao
    abstract val homeDao: HomeDao
    abstract val libraryDao: LibraryDao
    abstract val addPlaylistDao: AddPlaylistDao
    abstract val settingDao: SettingDao
    abstract val addToPlaylist: AddToPlaylistDao
    abstract val viewDao: ViewDao
    abstract val workDao: WorkDao
}