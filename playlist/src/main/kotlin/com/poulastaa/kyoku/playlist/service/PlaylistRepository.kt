package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.playlist.database.content.entity.*
import com.poulastaa.kyoku.playlist.database.content.repository.ArtistInfoDataSource
import com.poulastaa.kyoku.playlist.database.content.repository.SongDataSource
import com.poulastaa.kyoku.playlist.database.content.repository.SongInfoDataSource
import com.poulastaa.kyoku.playlist.domain.model.*
import com.poulastaa.kyoku.playlist.utils.SongId
import com.poulastaa.kyoku.playlist.utils.SpotifySongTitle
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.regex.Pattern

private const val FALLBACK_TITLE_CHUNK_SIZE = 50

@Service
class PlaylistRepository(
    private val songDb: SongDataSource,
    private val songInfoDb: SongInfoDataSource,
    private val artistInfoDb: ArtistInfoDataSource,
    private val cache: RedisCacheService,
) {
    /**
     * Fallback matcher (used only when `search` is unavailable): substring title match on CONTENT DB.
     * Titles are matched in chunks to keep each REGEXP small.
     */
    @Transactional(transactionManager = "contentTransactionManger", readOnly = true)
    fun findSongIdsByTitles(titles: List<SpotifySongTitle>): List<SongId> = titles
        .asSequence()
        .map { it.lowercase().trim() }
        .filter { it.isNotEmpty() }
        .distinct()
        .chunked(FALLBACK_TITLE_CHUNK_SIZE)
        .flatMap { chunk ->
            songDb.findSongIdsBySimilarTitles(chunk.joinToString("|") { Pattern.quote(it) })
        }
        .distinct()
        .toList()

    /**
     * Full songs for [ids] in the given order: `SONG:ID:` cache first, CONTENT DB for the misses.
     */
    @Transactional(transactionManager = "contentTransactionManger", readOnly = true)
    fun getSongsByIds(ids: List<SongId>): List<DtoSong> {
        if (ids.isEmpty()) return emptyList()

        val cached = cache.getSongsById(ids)
        val missingIds = ids.filter { it !in cached.keys }
        val fetched = if (missingIds.isNotEmpty()) fetchSongsFromDatabase(missingIds).associateBy { it.id }
        else emptyMap()

        return ids.mapNotNull { cached[it] ?: fetched[it] }
    }

    private fun fetchSongsFromDatabase(songIds: List<SongId>): List<DtoSong> {
        // Fetch all data - queries execute within the caller's transaction
        val songInfoMap = songInfoDb.findAllByIds(songIds).associateBy { it.id }
        val songsWithArtists = songDb.findSongsWithArtistsByIds(songIds)
        val songsWithAlbums = songDb.findSongsWithAlbumsByIds(songIds)
        val songsWithGenres = songDb.findSongsWithGenresByIds(songIds)
        val songsWithCountries = songDb.findSongsWithCountriesByIds(songIds)

        // Build lookup maps from the different result sets
        val artistsBySongId = songsWithArtists
            .flatMap { song -> song.artists.map { artist -> song.id to artist } }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.distinctBy { artist -> artist.id } }

        val albumsBySongId = songsWithAlbums
            .flatMap { song -> song.albums.map { album -> song.id to album } }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.distinctBy { album -> album.id } }

        val genresBySongId = songsWithGenres
            .flatMap { song -> song.genres.map { genre -> song.id to genre } }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.distinctBy { genre -> genre.id } }

        val countriesBySongId = songsWithCountries
            .flatMap { song -> song.countries.map { country -> song.id to country } }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.distinctBy { country -> country.id } }

        // Fetch artist info for all artists (from songs and albums)
        val songArtistIds = artistsBySongId.values.flatten().map { it.id }
        val albumArtistIds = albumsBySongId.values.flatten()
            .flatMap { album -> album.artists.map { it.id } }
        val artistIds = (songArtistIds + albumArtistIds).distinct()
        val artistInfoMap = if (artistIds.isNotEmpty()) {
            artistInfoDb.findAllByIds(artistIds).associateBy { it.id }
        } else emptyMap()

        val baseSongById = songsWithCountries.associateBy { it.id }

        // Build complete DTOs from the collected data
        return songIds.mapNotNull { songId ->
            val baseSong = baseSongById[songId] ?: return@mapNotNull null

            // Populate transient songInfo
            baseSong.songInfo = songInfoMap[songId]

            // Get collections from maps
            val artists = artistsBySongId[songId] ?: emptyList()
            val albums = albumsBySongId[songId] ?: emptyList()
            val genres = genresBySongId[songId] ?: emptyList()
            val countries = countriesBySongId[songId] ?: emptyList()

            // Populate artist info for song artists
            artists.forEach { artist ->
                artist.artistInfo = artistInfoMap[artist.id]
            }

            // Populate artist info for album artists
            albums.forEach { album ->
                album.artists.forEach { artist ->
                    artist.artistInfo = artistInfoMap[artist.id]
                }
            }

            DtoSong(
                id = baseSong.id,
                title = baseSong.title,
                rawPoster = baseSong.poster,
                rawMasterPlaylist = baseSong.url,
                info = baseSong.songInfo?.toDtoSongInfo() ?: DtoSongInfo(),
                artists = artists.map { it.toDtoArtist() },
                album = albums.map { it.toDtoAlbum() },
                genre = genres.map { it.toDtoGenre() },
                country = countries.map { it.toDtoCountry() }
            )
        }.also { cache.setSongById(it) }
    }

    private fun EntitySongInfo.toDtoSongInfo(): DtoSongInfo = DtoSongInfo(
        songId = this.id,
        releaseYear = this.releaseYear,
        composer = this.composer?.split(",")
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.map { name ->
                DtoComposer(
                    id = 0,
                    name = name,
                    rawCoverImage = null,
                    followers = 0
                )
            } ?: emptyList(),
        popularity = this.popularity
    )

    private fun EntityArtist.toDtoArtist(): DtoArtist = DtoArtist(
        id = this.id,
        name = this.name,
        rawCoverImage = this.coverImage,
        followers = this.followers,
        biography = this.artistInfo?.biography,
        birthDate = this.artistInfo?.birthDate,
        monthlyListeners = this.artistInfo?.monthlyListeners ?: 0,
        albums = emptyList(), // Avoid circular reference
        genres = this.genres.map { it.toDtoGenre() }
    )

    private fun EntityAlbum.toDtoAlbum(): DtoAlbum = DtoAlbum(
        id = this.id,
        name = this.name,
        popularity = this.popularity,
        rawPoster = null,
        artists = this.artists.map { artist ->
            DtoArtist(
                id = artist.id,
                name = artist.name,
                rawCoverImage = artist.coverImage,
                followers = artist.followers,
                biography = artist.artistInfo?.biography,
                birthDate = artist.artistInfo?.birthDate,
                monthlyListeners = artist.artistInfo?.monthlyListeners ?: 0,
                albums = emptyList(), // Avoid deeper circular reference
                genres = emptyList() // Genres not needed at this depth
            )
        }
    )

    private fun EntityGenre.toDtoGenre(): DtoGenre = DtoGenre(
        id = this.id,
        name = this.name,
        rawCoverImage = this.coverImage,
        popularity = this.popularity
    )

    private fun EntityCountry.toDtoCountry(): DtoCountry = DtoCountry(
        id = this.id,
        country = this.country,
        code = this.code
    )
}