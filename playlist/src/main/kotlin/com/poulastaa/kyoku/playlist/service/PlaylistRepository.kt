package com.poulastaa.kyoku.playlist.service

import com.poulastaa.kyoku.playlist.database.content.entity.*
import com.poulastaa.kyoku.playlist.database.repository.content.ArtistInfoDataSource
import com.poulastaa.kyoku.playlist.database.repository.content.SongDataSource
import com.poulastaa.kyoku.playlist.database.repository.content.SongInfoDataSource
import com.poulastaa.kyoku.playlist.domain.model.*
import com.poulastaa.kyoku.playlist.utils.SpotifySongTitle
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.regex.Pattern

@Service
class PlaylistRepository(
    private val songDb: SongDataSource,
    private val songInfoDb: SongInfoDataSource,
    private val artistInfoDb: ArtistInfoDataSource,
    private val cache: RedisCacheService,
) {
    @Transactional(readOnly = true)
    suspend fun getSongByTitles(titles: List<SpotifySongTitle>): List<DtoSong> = coroutineScope {
        if (titles.isEmpty()) return@coroutineScope emptyList()

        val cachedSongsByTitle = cache.cacheSongByTitle(titles)
        val uncachedTitles = titles.filter { it !in cachedSongsByTitle.keys }

        val dbSongs = if (uncachedTitles.isNotEmpty()) {
            fetchSongsFromDatabase(uncachedTitles)
        } else emptyList()

        cachedSongsByTitle.values + dbSongs
    }

    @Transactional(readOnly = true)
    fun fetchSongsFromDatabase(titles: List<SpotifySongTitle>): List<DtoSong> {
        val pattern = titles.joinToString("|") { Pattern.quote(it.lowercase()) }
        val songIds = songDb.findSongIdsBySimilarTitles(pattern)

        if (songIds.isEmpty()) return emptyList()

        // Fetch all data - queries execute within same transaction
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

        // Fetch artist info
        val artistIds = artistsBySongId.values.flatten().map { it.id }.distinct()
        val artistInfoMap = if (artistIds.isNotEmpty()) {
            artistInfoDb.findAllByIds(artistIds).associateBy { it.id }
        } else emptyMap()

        // Build complete DTOs from the collected data
        return songIds.mapNotNull { songId ->
            val baseSong = songsWithCountries.find { it.id == songId } ?: return@mapNotNull null

            // Populate transient songInfo
            baseSong.songInfo = songInfoMap[songId]

            // Get collections from maps
            val artists = artistsBySongId[songId] ?: emptyList()
            val albums = albumsBySongId[songId] ?: emptyList()
            val genres = genresBySongId[songId] ?: emptyList()
            val countries = countriesBySongId[songId] ?: emptyList()

            // Populate artist info
            artists.forEach { artist ->
                artist.artistInfo = artistInfoMap[artist.id]
            }

            // Convert to DTO
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
        }.also {
            cache.setSongById(it)
            cache.setSongByTitle(it)
        }
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
        artists = emptyList() // Avoid circular reference
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