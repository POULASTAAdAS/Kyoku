package com.poulastaa.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.poulastaa.core.database.dao.ImportPlaylistDao
import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.entity.EntityAlbum
import com.poulastaa.core.database.entity.EntityArtist
import com.poulastaa.core.database.entity.EntityCountry
import com.poulastaa.core.database.entity.EntityFavourite
import com.poulastaa.core.database.entity.EntityGenre
import com.poulastaa.core.database.entity.EntityPlaylist
import com.poulastaa.core.database.entity.EntityRelationArtistAlbum
import com.poulastaa.core.database.entity.EntityRelationArtistCountry
import com.poulastaa.core.database.entity.EntityRelationArtistGenre
import com.poulastaa.core.database.entity.EntityRelationSongAlbum
import com.poulastaa.core.database.entity.EntityRelationSongArtist
import com.poulastaa.core.database.entity.EntityRelationSongCountry
import com.poulastaa.core.database.entity.EntityRelationSongPlaylist
import com.poulastaa.core.database.entity.EntityRelationSuggestedPlaylist
import com.poulastaa.core.database.entity.EntityRelationSuggestedSongByArtist
import com.poulastaa.core.database.entity.EntitySong
import com.poulastaa.core.database.entity.EntitySongInfo
import com.poulastaa.core.database.entity.EntitySuggestedAlbum
import com.poulastaa.core.database.entity.EntitySuggestedArtist

@Database(
    entities = [
        EntitySong::class,
        EntityAlbum::class,
        EntityGenre::class,
        EntityArtist::class,
        EntityCountry::class,
        EntityPlaylist::class,

        EntitySongInfo::class,
        EntityFavourite::class,
        EntitySuggestedAlbum::class,
        EntitySuggestedArtist::class,

        EntityRelationSongAlbum::class,
        EntityRelationSongArtist::class,
        EntityRelationSongCountry::class,
        EntityRelationSongPlaylist::class,

        EntityRelationArtistAlbum::class,
        EntityRelationArtistGenre::class,
        EntityRelationArtistCountry::class,

        EntityRelationSuggestedPlaylist::class,
        EntityRelationSuggestedSongByArtist::class
    ],
    version = 3,
    exportSchema = true
)
abstract class KyokuDatabase : RoomDatabase() {
    abstract val rootDao: RootDao
    abstract val importPlaylistDao: ImportPlaylistDao
}