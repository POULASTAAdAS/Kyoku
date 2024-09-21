package com.poulastaa.play.data

import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.repository.create_playlist.album.CreatePlaylistAlbumRepository
import com.poulastaa.core.domain.repository.create_playlist.album.RemoteCreatePlaylistAlbumDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import javax.inject.Inject

class OnlineFirstCreatePlaylistAlbumRepository @Inject constructor(
    private val remote: RemoteCreatePlaylistAlbumDatasource,
) : CreatePlaylistAlbumRepository {
    override suspend fun getAlbum(
        albumId: Long,
        savedSongIdList: List<Long>,
    ): Result<AlbumWithSong, DataError.Network> = remote.getAlbum(
        albumId = albumId,
        savedSongIdList = savedSongIdList
    )
}