package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.auth.Email

interface LocalCoreCacheDatasource {
    fun cacheUsersByEmail(email: String, type: UserType): DtoDBUser?
    fun setUserByEmail(key: Email, type: UserType, value: DtoDBUser)

    fun setPlaylist(playlistDto: DtoPlaylist)

    fun setSongById(song: DtoSong)
    fun setSongById(list: List<DtoSong>)

    fun cacheGenreById(genreId: GenreId): DtoGenre?
    fun cacheGenreById(list: List<GenreId>): List<DtoGenre>
    fun setGenreById(genre: DtoGenre)
    fun setGenreById(list: List<DtoGenre>)

    fun cacheAlbumById(albumId: AlbumId): DtoAlbum?
    fun cacheAlbumById(list: List<AlbumId>): List<DtoAlbum>
    fun setAlbumById(album: DtoAlbum)
    fun setAlbumById(list: List<DtoAlbum>)

    fun cacheArtistById(artistId: ArtistId): DtoArtist?
    fun cacheArtistById(list: List<ArtistId>): List<DtoArtist>
    fun setArtistById(artist: DtoArtist)
    fun setArtistById(list: List<DtoArtist>)

    fun cacheCountryById(countryId: CountryId): DtoCountry?
    fun cacheCountryById(list: List<CountryId>): List<DtoCountry>
    fun setCountryById(country: DtoCountry)
    fun setCountryById(list: List<DtoCountry>)

    fun cacheSongInfo(songId: SongId): DtoSongInfo?
    fun cacheSongInfo(list: List<SongId>): List<DtoSongInfo>
    fun setSongInfoById(songInfo: DtoSongInfo)
    fun setSongInfoById(list: List<DtoSongInfo>)

    fun cacheArtistIdBySongId(songId: SongId): List<ArtistId>
    fun cacheArtistIdBySongId(list: List<SongId>): Map<SongId, List<ArtistId>>
    fun setArtistIdBySongId(songId: SongId, list: List<ArtistId>)
    fun setArtistIdBySongId(map: Map<SongId, List<ArtistId>>)

    fun cacheGenreIdBySongId(songId: SongId): GenreId?
    fun cacheGenreIdBySongId(list: List<SongId>): Map<SongId, GenreId>
    fun setGenreIdBySongId(songId: SongId, countryId: GenreId)
    fun setGenreIdBySongId(map: Map<SongId, GenreId>)

    fun cacheAlbumIdBySongId(songId: SongId): AlbumId?
    fun cacheAlbumIdBySongId(list: List<SongId>): Map<SongId, AlbumId>
    fun setAlbumIdBySongId(songId: SongId, albumId: AlbumId)
    fun setAlbumIdBySongId(map: Map<SongId, AlbumId>)

    fun cacheGenreIdByArtistId(artistId: ArtistId): GenreId?
    fun cacheGenreIdByArtistId(list: List<ArtistId>): Map<ArtistId, GenreId>
    fun setGenreIdByArtistId(artistId: ArtistId, genreId: GenreId)
    fun setGenreIdByArtistId(map: Map<GenreId, ArtistId>)

    fun cacheCountryIdByArtistId(artistId: ArtistId): CountryId?
    fun cacheCountryIdByArtistId(list: List<ArtistId>): Map<ArtistId, CountryId>
    fun setCountryIdByArtistId(artistId: ArtistId, countryId: CountryId)
    fun setCountryIdByArtistId(map: Map<GenreId, CountryId>)
}