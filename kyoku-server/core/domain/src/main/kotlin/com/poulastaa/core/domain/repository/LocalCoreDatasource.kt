package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.*

typealias ArtistId = Long
typealias SongId = Long
typealias AlbumId = Long
typealias GenreId = Int
typealias CountryId = Int
typealias PlaylistId = Long

interface LocalCoreDatasource {
    suspend fun getUserByEmail(email: String, userType: UserType): DtoDBUser?
    suspend fun createPlaylist(userId: Long, list: List<SongId>): DtoPlaylist

    suspend fun getArtistOnSongId(list: List<SongId>): List<Pair<SongId, List<DtoArtist>>>
    suspend fun getInfoOnSongId(list: List<SongId>): List<Pair<SongId, DtoSongInfo>>
    suspend fun getGenreOnSongId(list: List<SongId>): List<Pair<SongId, DtoGenre>>
    suspend fun getAlbumOnSongId(list: List<SongId>): List<Pair<SongId, DtoAlbum>>

    suspend fun getSongOnId(songId: SongId): DtoSong?
    suspend fun getSongOnId(list: List<SongId>): List<DtoSong>

    suspend fun getArtistFromDbArtist(list: List<DtoDBArtist>): List<DtoArtist>
}