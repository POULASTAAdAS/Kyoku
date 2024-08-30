package com.poulastaa.play.data

import com.poulastaa.core.domain.repository.work.LocalWorkDatasource
import com.poulastaa.core.domain.repository.work.RemoteWorkDatasource
import com.poulastaa.core.domain.repository.work.WorkRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class OnlineWorkRepository @Inject constructor(
    private val local: LocalWorkDatasource,
    private val remote: RemoteWorkDatasource,
    private val applicationScope: CoroutineScope
) : WorkRepository {
    override suspend fun getUpdatedAlbums(): EmptyResult<DataError.Network> {
        val albumIdList = local.getAllAlbumId()
        val result = remote.getUpdatedAlbums(albumIdList)

        if (result is Result.Success) {
            val remove = applicationScope.async { local.removeAlbums(result.data.removeIdList) }
            val add = applicationScope.async { local.saveAlbums(result.data.newAlbumList) }

            remove.await()
            add.await()
        }

        return result.asEmptyDataResult()
    }

    override suspend fun getUpdatedPlaylists(): EmptyResult<DataError.Network> {
        val albumIdList = local.getAllPlaylistId()

        val result = remote.getUpdatedPlaylists(albumIdList)
        if (result is Result.Success) {
            val remove = applicationScope.async { local.removePlaylist(result.data.removeIdList) }
            val add = applicationScope.async { local.savePlaylists(result.data.newAlbumList) }

            remove.await()
            add.await()
        }

        return result.asEmptyDataResult()
    }

    override suspend fun getUpdatedArtists(): EmptyResult<DataError.Network> {
        val albumIdList = local.getAllArtistsId()

        val result = remote.getUpdatedArtists(albumIdList)
        if (result is Result.Success) {
            val remove = applicationScope.async { local.removeArtists(result.data.removeIdList) }
            val add = applicationScope.async { local.saveArtists(result.data.newAlbumList) }

            remove.await()
            add.await()
        }

        return result.asEmptyDataResult()
    }
}