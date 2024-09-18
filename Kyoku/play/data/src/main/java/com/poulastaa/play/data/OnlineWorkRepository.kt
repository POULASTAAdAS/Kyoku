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
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class OnlineWorkRepository @Inject constructor(
    private val local: LocalWorkDatasource,
    private val remote: RemoteWorkDatasource,
    private val applicationScope: CoroutineScope,
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
        val result = remote.getUpdatedPlaylists(albumIdList, true)

        if (result is Result.Success) {
            val remove = applicationScope.async { local.removePlaylist(result.data.removeIdList) }
            val add = applicationScope.async { local.savePlaylists(result.data.newAlbumList) }

            remove.await()
            add.await()
        }

        return result.asEmptyDataResult()
    }

    override suspend fun getUpdatedPlaylistSongs(): EmptyResult<DataError.Network> =
        coroutineScope {
            local.getAllPlaylistId().map { playlistId ->
                val res = async {
                    val songIdList = local.getAllPlaylistSongsIdList(playlistId)
                    val result = remote.getUpdatedPlaylists(
                        list = listOf(playlistId) + songIdList,
                        arePlaylist = false
                    ) // IMPROVISED added playlistId in front of the list...backend works expecting this behaviour

                    if (result is Result.Success) {
                        async {
                            local.removePlaylistSongs(
                                playlistId,
                                result.data.removeIdList
                            )
                        }.await()

                        local.updatePlaylistsSongs(result.data.newAlbumList.first())
                    }

                    result.asEmptyDataResult()
                }.await()

                if (res is Result.Error) return@coroutineScope res
            }

            Result.Success(Unit)
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

    override suspend fun getUpdatedFavourite(): EmptyResult<DataError.Network> {
        val favouriteIds = local.getAllFavouriteId()
        val result = remote.getUpdatedFavourite(favouriteIds)

        if (result is Result.Success) {
            applicationScope.async { local.removeFavourite(result.data.removeIdList) }.await()
            applicationScope.async { local.saveFavourite(result.data.newAlbumList) }.await()
        }

        return result.asEmptyDataResult()
    }
}