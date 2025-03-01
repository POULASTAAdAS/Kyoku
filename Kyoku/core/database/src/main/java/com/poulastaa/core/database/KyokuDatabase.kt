package com.poulastaa.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.poulastaa.core.database.dao.HomeDao
import com.poulastaa.core.database.dao.ImportPlaylistDao
import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.entity.EntityAlbum
import com.poulastaa.core.database.entity.EntityArtist
import com.poulastaa.core.database.entity.EntityCountry
import com.poulastaa.core.database.entity.EntityExplore
import com.poulastaa.core.database.entity.EntityFavourite
import com.poulastaa.core.database.entity.EntityGenre
import com.poulastaa.core.database.entity.EntityPlaylist
import com.poulastaa.core.database.entity.EntityPrevAlbum
import com.poulastaa.core.database.entity.EntityPrevArtist
import com.poulastaa.core.database.entity.EntityPrevExplore
import com.poulastaa.core.database.entity.EntityPrevSong
import com.poulastaa.core.database.entity.EntityRelationArtistAlbum
import com.poulastaa.core.database.entity.EntityRelationArtistCountry
import com.poulastaa.core.database.entity.EntityRelationArtistGenre
import com.poulastaa.core.database.entity.EntityRelationSongAlbum
import com.poulastaa.core.database.entity.EntityRelationSongArtist
import com.poulastaa.core.database.entity.EntityRelationSongCountry
import com.poulastaa.core.database.entity.EntityRelationSongPlaylist
import com.poulastaa.core.database.entity.EntityRelationSuggestedSongByArtist
import com.poulastaa.core.database.entity.EntitySong
import com.poulastaa.core.database.entity.EntitySongInfo

@Database(
    entities = [
        EntitySong::class,
        EntityAlbum::class,
        EntityGenre::class,
        EntityArtist::class,
        EntityCountry::class,
        EntityPlaylist::class,

        EntityExplore::class,

        EntitySongInfo::class,
        EntityFavourite::class,

        EntityRelationSongAlbum::class,
        EntityRelationSongArtist::class,
        EntityRelationSongCountry::class,
        EntityRelationSongPlaylist::class,

        EntityRelationArtistAlbum::class,
        EntityRelationArtistGenre::class,
        EntityRelationArtistCountry::class,

        EntityPrevSong::class,
        EntityPrevAlbum::class,
        EntityPrevArtist::class,

        EntityPrevExplore::class,
        EntityRelationSuggestedSongByArtist::class
    ],
    version = 1,
    exportSchema = true
)
internal abstract class KyokuDatabase : RoomDatabase() {
    abstract val rootDao: RootDao
    abstract val importPlaylistDao: ImportPlaylistDao
    abstract val homeDao: HomeDao
    abstract val libraryDao: LibraryDao
}