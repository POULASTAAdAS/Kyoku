package com.poulastaa.play.data

import androidx.paging.PagingData
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.CreatePlaylistPagingData
import com.poulastaa.core.domain.repository.create_playlist.artist.CreatePlaylistArtistRepository
import com.poulastaa.core.domain.repository.create_playlist.artist.LocalCreatePlaylistArtistDatasource
import com.poulastaa.core.domain.repository.create_playlist.artist.RemoteCreatePlaylistArtistDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstCreatePlaylistArtistRepository @Inject constructor(
    private val local: LocalCreatePlaylistArtistDatasource,
    private val remote: RemoteCreatePlaylistArtistDatasource,
) : CreatePlaylistArtistRepository {
    override suspend fun getArtist(artistId: Long): Result<Artist, DataError.Network> {
        val artist = local.getArtist(artistId) ?: return remote.getArtist(artistId)

        return Result.Success(artist)
    }

    override suspend fun getPagingAlbum(
        artistId: Long,
    ): Flow<PagingData<CreatePlaylistPagingData>> =
        remote.getPagingAlbum(artistId)

    override suspend fun getPagingSong(
        artistId: Long,
        savedSongIdList: List<Long>,
    ): Flow<PagingData<CreatePlaylistPagingData>> =
        remote.getPagingSong(artistId, savedSongIdList)
}