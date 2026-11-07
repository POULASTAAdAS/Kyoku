package com.poulastaa.common.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.poulastaa.common.database.dao.AuthDao
import com.poulastaa.common.database.dao.PlaylistDao
import com.poulastaa.common.database.dao.SongDao
import com.poulastaa.common.database.entity.AlbumArtistEntity
import com.poulastaa.common.database.entity.AlbumEntity
import com.poulastaa.common.database.entity.ArtistEntity
import com.poulastaa.common.database.entity.ComposerEntity
import com.poulastaa.common.database.entity.CountryEntity
import com.poulastaa.common.database.entity.GenreEntity
import com.poulastaa.common.database.entity.PlaylistEntity
import com.poulastaa.common.database.entity.PlaylistSongEntity
import com.poulastaa.common.database.entity.SongAlbumEntity
import com.poulastaa.common.database.entity.SongArtistEntity
import com.poulastaa.common.database.entity.SongComposerEntity
import com.poulastaa.common.database.entity.SongCountryEntity
import com.poulastaa.common.database.entity.SongEntity
import com.poulastaa.common.database.entity.SongGenreEntity
import com.poulastaa.common.database.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal const val DATABASE_NAME = "kyoku.db"

@Database(
    entities = [
        UserEntity::class,
        SongEntity::class,
        ArtistEntity::class,
        AlbumEntity::class,
        GenreEntity::class,
        CountryEntity::class,
        ComposerEntity::class,
        PlaylistEntity::class,
        SongArtistEntity::class,
        SongAlbumEntity::class,
        SongGenreEntity::class,
        SongCountryEntity::class,
        SongComposerEntity::class,
        AlbumArtistEntity::class,
        PlaylistSongEntity::class,
    ],
    version = 2,
)
@ConstructedBy(KyokuDatabaseConstructor::class)
abstract class KyokuDatabase : RoomDatabase() {
    abstract val authDao: AuthDao
    abstract val songDao: SongDao
    abstract val playlistDao: PlaylistDao
}

// Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object KyokuDatabaseConstructor : RoomDatabaseConstructor<KyokuDatabase> {
    override fun initialize(): KyokuDatabase
}

internal fun RoomDatabase.Builder<KyokuDatabase>.buildKyokuDatabase(): KyokuDatabase =
    setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(dropAllTables = true)
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
