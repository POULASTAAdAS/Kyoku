package com.poulastaa.core.domain.repository.view

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.AlbumId
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.PlaylistId
import com.poulastaa.core.domain.repository.SongId

interface LocalViewCacheDatasource {
    fun cacheArtistById(artistId: ArtistId): DtoPrevArtist?
    fun setArtistById(artist: DtoPrevArtist)

    fun cacheDetailedPrevSongById(list: List<SongId>): List<DtoDetailedPrevSong>
    fun setDetailedPrevSongById(songs: List<DtoDetailedPrevSong>)

    fun cachePlaylistOnId(playlistId: PlaylistId): DtoPlaylist?
    fun setPlaylistOnId(playlist: DtoPlaylist)

    fun cachePrevDetailedSongByPlaylistId(playlistId: PlaylistId): Pair<List<DtoDetailedPrevSong>, List<SongId>>?
    fun setSongIdByPlaylistId(playlistId: PlaylistId, list: List<SongId>)

    fun cachePrevDetailedSongByAlbumId(albumId: AlbumId): Pair<List<DtoDetailedPrevSong>, List<SongId>>?
    fun setSongIdByAlbumId(albumId: AlbumId, list: List<SongId>)

    fun cacheAlbumById(albumId: AlbumId): DtoAlbum?
    fun setAlbumById(album: DtoAlbum)

    fun cacheUserFevPrevSong(userId: Long): Pair<List<DtoDetailedPrevSong>, List<SongId>>?
    fun setUserFevPrevSong(userId: Long, list: List<SongId>)

    fun cacheSongById(list: List<SongId>): List<DtoSong>
    fun setSongById(list: List<DtoSong>)
}