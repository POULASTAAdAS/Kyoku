package com.poulastaa.play.data

import androidx.paging.PagingData
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.ArtistSingleData
import com.poulastaa.core.domain.repository.explore_artist.ExploreArtistRepository
import com.poulastaa.core.domain.repository.explore_artist.LocalExploreArtistDatasource
import com.poulastaa.core.domain.repository.explore_artist.RemoteExploreArtistDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstExploreArtistRepository @Inject constructor(
    private val local: LocalExploreArtistDatasource,
    private val remote: RemoteExploreArtistDatasource,
    private val application: CoroutineScope,
) : ExploreArtistRepository {
    override suspend fun getArtist(artistId: Long): Result<Artist, DataError.Network> {
        val artist = local.getArtist(artistId) ?: return remote.getArtist(artistId)
        return Result.Success(artist)
    }

    override suspend fun followArtist(artistId: Long): EmptyResult<DataError.Network> {
        val result = remote.followArtist(artistId)
        if (result is Result.Success) application.async { local.followArtist(result.data) }.await()

        return result.asEmptyDataResult()
    }

    override suspend fun unFollowArtist(artistId: Long): EmptyResult<DataError.Network> {
        val result = remote.unFollowArtist(artistId)
        if (result is Result.Success) application.async { local.unFollowArtist(artistId) }.await()

        return result.asEmptyDataResult()
    }

    override suspend fun getArtistSong(artistId: Long): Flow<PagingData<ArtistSingleData>> =
        remote.getArtistSong(artistId)

    override suspend fun getArtistAlbum(artistId: Long): Flow<PagingData<ArtistSingleData>> =
        remote.getArtistAlbum(artistId)

    override suspend fun isSongInFavourite(songId: Long): Boolean = local.isSongInFavourite(songId)
    override suspend fun isAlbumSaved(albumId: Long): Boolean = local.isAlbumSaved(albumId)


    override suspend fun saveAlbum(albumId: Long): EmptyResult<DataError.Network> {
        val result = remote.saveAlbum(albumId)
        if (result is Result.Success) application.async { local.saveAlbum(result.data) }.await()

        return result.asEmptyDataResult()
    }

    override suspend fun addSongToFavourite(songId: Long): EmptyResult<DataError.Network> {
        val result = remote.addSongToFavourite(songId)
        if (result is Result.Success) application.async { local.addSongToFavourite(result.data) }
            .await()

        return result.asEmptyDataResult()
    }
}