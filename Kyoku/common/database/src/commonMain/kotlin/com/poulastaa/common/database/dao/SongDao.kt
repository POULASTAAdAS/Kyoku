package com.poulastaa.common.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.poulastaa.common.database.entity.AlbumArtistEntity
import com.poulastaa.common.database.entity.AlbumEntity
import com.poulastaa.common.database.entity.ArtistEntity
import com.poulastaa.common.database.entity.ComposerEntity
import com.poulastaa.common.database.entity.CountryEntity
import com.poulastaa.common.database.entity.GenreEntity
import com.poulastaa.common.database.entity.SongAlbumEntity
import com.poulastaa.common.database.entity.SongArtistEntity
import com.poulastaa.common.database.entity.SongComposerEntity
import com.poulastaa.common.database.entity.SongCountryEntity
import com.poulastaa.common.database.entity.SongEntity
import com.poulastaa.common.database.entity.SongGenreEntity
import com.poulastaa.common.database.entity.relation.SongWithRelations
import com.poulastaa.common.database.entity.relation.SongWithRelationsBundle
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SongDao {
    @Upsert
    abstract suspend fun upsertSongs(songs: List<SongEntity>)

    @Upsert
    abstract suspend fun upsertArtists(artists: List<ArtistEntity>)

    @Upsert
    abstract suspend fun upsertAlbums(albums: List<AlbumEntity>)

    @Upsert
    abstract suspend fun upsertGenres(genres: List<GenreEntity>)

    @Upsert
    abstract suspend fun upsertCountries(countries: List<CountryEntity>)

    @Upsert
    abstract suspend fun upsertComposers(composers: List<ComposerEntity>)

    @Upsert
    abstract suspend fun upsertSongArtists(links: List<SongArtistEntity>)

    @Upsert
    abstract suspend fun upsertSongAlbums(links: List<SongAlbumEntity>)

    @Upsert
    abstract suspend fun upsertSongGenres(links: List<SongGenreEntity>)

    @Upsert
    abstract suspend fun upsertSongCountries(links: List<SongCountryEntity>)

    @Upsert
    abstract suspend fun upsertSongComposers(links: List<SongComposerEntity>)

    @Upsert
    abstract suspend fun upsertAlbumArtists(links: List<AlbumArtistEntity>)

    @Query("SELECT * FROM song WHERE id IN (:ids)")
    abstract suspend fun getSongs(ids: List<Long>): List<SongEntity>

    @Query("DELETE FROM song_artist WHERE songId = :songId")
    abstract suspend fun deleteSongArtists(songId: Long)

    @Query("DELETE FROM song_album WHERE songId = :songId")
    abstract suspend fun deleteSongAlbums(songId: Long)

    @Query("DELETE FROM song_genre WHERE songId = :songId")
    abstract suspend fun deleteSongGenres(songId: Long)

    @Query("DELETE FROM song_country WHERE songId = :songId")
    abstract suspend fun deleteSongCountries(songId: Long)

    @Query("DELETE FROM song_composer WHERE songId = :songId")
    abstract suspend fun deleteSongComposers(songId: Long)

    @Transaction
    @Query("SELECT * FROM song WHERE id = :id")
    abstract fun observeSong(id: Long): Flow<SongWithRelations?>

    /**
     * Single write path for songs coming from any response.
     *
     * Nullable song fields missing from the response keep their stored value, and a relation is
     * only replaced when the response carries it.
     */
    @Transaction
    open suspend fun upsertFullSongs(bundles: List<SongWithRelationsBundle>) {
        if (bundles.isEmpty()) return

        val existing = getSongs(bundles.map { it.song.id }).associateBy { it.id }

        upsertArtists(bundles.flatMap { it.artists + it.albumOnlyArtists }.distinctBy { it.id })
        upsertAlbums(bundles.flatMap { it.albums }.distinctBy { it.id })
        upsertAlbumArtists(bundles.flatMap { it.albumArtists }.distinct())
        upsertGenres(bundles.flatMap { it.genres }.distinctBy { it.id })
        upsertCountries(bundles.flatMap { it.countries }.distinctBy { it.id })
        upsertComposers(bundles.flatMap { it.composers }.distinctBy { it.id })

        upsertSongs(bundles.map { it.song.mergeWith(existing[it.song.id]) })

        bundles.forEach { bundle ->
            val songId = bundle.song.id

            if (bundle.artists.isNotEmpty()) {
                deleteSongArtists(songId)
                upsertSongArtists(bundle.artists.map { SongArtistEntity(songId, it.id) })
            }
            if (bundle.albums.isNotEmpty()) {
                deleteSongAlbums(songId)
                upsertSongAlbums(bundle.albums.map { SongAlbumEntity(songId, it.id) })
            }
            if (bundle.genres.isNotEmpty()) {
                deleteSongGenres(songId)
                upsertSongGenres(bundle.genres.map { SongGenreEntity(songId, it.id) })
            }
            if (bundle.countries.isNotEmpty()) {
                deleteSongCountries(songId)
                upsertSongCountries(bundle.countries.map { SongCountryEntity(songId, it.id) })
            }
            if (bundle.composers.isNotEmpty()) {
                deleteSongComposers(songId)
                upsertSongComposers(bundle.composers.map { SongComposerEntity(songId, it.id) })
            }
        }
    }
}

private fun SongEntity.mergeWith(stored: SongEntity?) = if (stored == null) this else copy(
    poster = poster ?: stored.poster,
    masterPlaylist = masterPlaylist.ifBlank { stored.masterPlaylist },
    releaseYear = releaseYear ?: stored.releaseYear,
    popularity = popularity ?: stored.popularity,
)
