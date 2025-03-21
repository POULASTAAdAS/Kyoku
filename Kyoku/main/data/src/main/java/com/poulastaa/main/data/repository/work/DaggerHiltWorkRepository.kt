package com.poulastaa.main.data.repository.work

import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import com.poulastaa.core.domain.repository.LocalWorkDatasource
import com.poulastaa.main.domain.repository.work.RemoteWorkDatasource
import com.poulastaa.main.domain.repository.work.WorkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import javax.inject.Inject

internal class DaggerHiltWorkRepository @Inject constructor(
    private val local: LocalWorkDatasource,
    private val remote: RemoteWorkDatasource,
    private val scope: CoroutineScope,
) : WorkRepository {
    override suspend fun syncSavedAlbums(): EmptyResult<DataError.Network> {
        val savedAlbumIdList = local.getSavedAlbumIds()
        val result = remote.syncSavedAlbums(savedAlbumIdList)

        if (result is Result.Success) {
            val remove = scope.async { local.removeSavedAlbums(result.data.removeIdList) }
            val add = scope.async { local.saveAlbums(result.data.newData) }

            remove.await()
            add.await()
        }

        return result.asEmptyDataResult()
    }

    override suspend fun syncSavedPlaylist(): EmptyResult<DataError.Network> {
        val savedPlaylistIdList = local.getSavedPlaylistIds()
        val result = remote.syncSavedPlaylist(savedPlaylistIdList)

        if (result is Result.Success) {
            val remove = scope.async { local.removeSavedPlaylists(result.data.removeIdList) }
            val add = scope.async { local.savePlaylists(result.data.newData) }

            listOf(
                remove,
                add
            ).awaitAll()

            val savedPlaylistSongIds = local.getSavedPlaylistSongs()
            val res = remote.syncSavedPlaylistSongs(savedPlaylistSongIds)

            if (res is Result.Success) {
                val rev = scope.async { local.removeSavedPlaylistSongs(res.data.removeList) }
                val addd = scope.async { local.updateSavedPlaylistSongs(res.data.newData) }

                listOf(
                    rev,
                    addd
                ).awaitAll()
            }
        }

        return result.asEmptyDataResult()
    }

    override suspend fun syncSavedArtist(): EmptyResult<DataError.Network> {
        val savedArtistIdList = local.getSavedArtistIds()
        val result = remote.syncSavedArtist(savedArtistIdList)

        if (result is Result.Success) {
            val remove = scope.async { local.removeSavedArtists(result.data.removeIdList) }
            val add = scope.async { local.saveArtists(result.data.newData) }

            remove.await()
            add.await()
        }

        return result.asEmptyDataResult()
    }

    override suspend fun syncSavedFavourite(): EmptyResult<DataError.Network> {
        val savedFavouriteIdList = local.getSavedFavouriteIds()
        val result = remote.syncSavedFavourite(savedFavouriteIdList)

        if (result is Result.Success) {
            val remove = scope.async { local.removeSavedFavourites(result.data.removeIdList) }
            val add = scope.async { local.saveFavourites(result.data.newData) }

            remove.await()
            add.await()
        }

        return result.asEmptyDataResult()
    }
}