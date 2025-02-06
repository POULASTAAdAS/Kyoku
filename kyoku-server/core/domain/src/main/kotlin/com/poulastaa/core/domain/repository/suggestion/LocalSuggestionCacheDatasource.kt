package com.poulastaa.core.domain.repository.suggestion

import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.DtoPrevSong
import com.poulastaa.core.domain.repository.AlbumId
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.PlaylistId
import com.poulastaa.core.domain.repository.SongId

interface LocalSuggestionCacheDatasource {
    fun setPrevSongById(song: DtoPrevSong)
    fun setPrevSongById(list: List<DtoPrevSong>)
    fun cachePrevSongById(list: List<SongId>): List<DtoPrevSong>
    fun cachePrevSongById(songId: SongId): DtoPrevSong?

    fun cachePlaylistOnId(playlistId: PlaylistId): DtoPlaylist?
    fun cachePlaylistOnId(list: List<PlaylistId>): List<DtoPlaylist>
    fun setPlaylistOnId(data: DtoPlaylist)
    fun setPlaylistOnId(list: List<DtoPlaylist>)

    fun cacheAlbumById(albumId: AlbumId): DtoAlbum?
    fun cacheAlbumById(list: List<AlbumId>): List<DtoAlbum>
    fun setAlbumById(album: DtoAlbum)
    fun setAlbumById(list: List<DtoAlbum>)

    fun cacheArtistById(artistId: ArtistId): DtoArtist?
    fun cacheArtistById(list: List<ArtistId>): List<DtoArtist>
    fun setArtistById(artist: DtoArtist)
    fun setArtistById(list: List<DtoArtist>)
}