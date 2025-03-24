package com.poulastaa.core.domain.repository.sync

import com.poulastaa.core.domain.model.DtoAlbum
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.repository.AlbumId
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.PlaylistId
import com.poulastaa.core.domain.repository.SongId

interface LocalSyncCacheDatasource {
    fun setSongById(list: List<DtoSong>)
    fun cacheSongById(list: List<SongId>): List<DtoSong>

    fun setAlbumById(list: List<DtoAlbum>)
    fun cacheAlbumById(list: List<AlbumId>): List<DtoAlbum>

    fun setPlaylistById(list: List<DtoPlaylist>)
    fun cachePlaylistOnId(list: List<PlaylistId>): List<DtoPlaylist>

    fun setArtistById(list: List<DtoArtist>)
    fun cacheArtistById(list: List<ArtistId>): List<DtoArtist>

    fun cacheSongIdByPlaylistId(playlistId: PlaylistId): List<SongId>
    fun setSongIdByPlaylistId(playlistId: PlaylistId, list: List<SongId>)
}