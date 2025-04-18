package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.*

typealias ArtistId = Long
typealias SongId = Long
typealias AlbumId = Long
typealias GenreId = Int
typealias CountryId = Int
typealias PlaylistId = Long
typealias UserId = Long

interface LocalCoreDatasource {
    suspend fun getUserByEmail(email: String, userType: UserType): DtoDBUser?
    suspend fun createPlaylist(userId: Long, list: List<SongId>): DtoPlaylist

    suspend fun getArtistOnSongId(list: List<SongId>): List<Pair<SongId, List<DtoArtist>>>
    suspend fun getInfoOnSongId(list: List<SongId>): List<Pair<SongId, DtoSongInfo>>
    suspend fun getGenreOnSongId(list: List<SongId>): List<Pair<SongId, DtoGenre>>
    suspend fun getAlbumOnSongId(list: List<SongId>): List<Pair<SongId, DtoAlbum>>

    suspend fun getSongOnId(songId: SongId): DtoSong?
    suspend fun getSongOnId(list: List<SongId>): List<DtoSong>

    suspend fun getDetailedPrevSongOnId(songId: SongId): DtoDetailedPrevSong?
    suspend fun getDetailedPrevSongOnId(list: List<SongId>): List<DtoDetailedPrevSong>

    suspend fun getArtistFromDbArtist(list: List<DtoDBArtist>): List<DtoArtist>

    suspend fun getGenreOnArtistId(artistId: ArtistId): DtoGenre?
    suspend fun getCountryOnArtistId(artistId: ArtistId): DtoCountry?

    suspend fun getAlbumOnId(list: List<AlbumId>): List<DtoAlbum>
    suspend fun getPlaylistOnId(list: List<PlaylistId>): List<DtoPlaylist>
    suspend fun getArtistOnId(artistId: ArtistId): DtoArtist?
    suspend fun getArtistOnId(list: List<ArtistId>): List<DtoArtist>
}