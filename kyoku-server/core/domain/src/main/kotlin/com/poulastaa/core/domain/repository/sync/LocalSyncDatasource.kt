package com.poulastaa.core.domain.repository.sync

import com.poulastaa.core.domain.model.*
import com.poulastaa.core.domain.repository.AlbumId
import com.poulastaa.core.domain.repository.ArtistId
import com.poulastaa.core.domain.repository.PlaylistId
import com.poulastaa.core.domain.repository.SongId

interface LocalSyncDatasource {
    suspend fun getUsersByEmail(email: String, type: UserType): DtoDBUser?

    suspend fun getSavedAlbumIdList(userId: Long): List<AlbumId>
    suspend fun getFullAlbumOnIdList(idList: List<AlbumId>, userId: Long): List<DtoFullAlbum>
    fun removeAlbum(idList: List<AlbumId>, userId: Long)

    suspend fun getSavedPlaylistIdList(userId: Long): List<PlaylistId>
    suspend fun getFullPlaylistOnfIdList(idList: List<PlaylistId>, userId: Long): List<DtoFullPlaylist>
    fun removePlaylist(idList: List<PlaylistId>, userId: Long)

    suspend fun getSavedArtistIdList(userId: Long): List<ArtistId>
    suspend fun getArtistOnIdList(idList: List<ArtistId>, userId: Long): List<DtoArtist>
    fun removeArtist(idList: List<ArtistId>, userId: Long)

    suspend fun getSavedFavouriteSongsIdList(userId: Long): List<SongId>
    suspend fun getFavoriteSongs(idList: List<SongId>, userId: Long): List<DtoSong>
    fun removeFavouriteSongs(idList: List<SongId>, userId: Long)

    suspend fun getPlaylistSongIdList(playlistId: PlaylistId): List<SongId>
    suspend fun getPlaylistSongs(list: List<SongId>): List<DtoSong>
    fun removePlaylistSongs(playlistId: PlaylistId, songIds: List<SongId>)
}