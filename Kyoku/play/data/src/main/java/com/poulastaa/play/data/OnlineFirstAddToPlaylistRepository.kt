package com.poulastaa.play.data

import com.poulastaa.core.domain.model.PrevSavedPlaylist
import com.poulastaa.core.domain.repository.add_to_playlist.AddToPlaylistRepository
import com.poulastaa.core.domain.repository.add_to_playlist.LocalAddToPlaylistDatasource
import com.poulastaa.core.domain.repository.add_to_playlist.PRESENT
import com.poulastaa.core.domain.repository.add_to_playlist.RemoteAddToPlaylistDatasource
import com.poulastaa.core.domain.repository.add_to_playlist.SIZE
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class OnlineFirstAddToPlaylistRepository @Inject constructor(
    private val local: LocalAddToPlaylistDatasource,
    private val remote: RemoteAddToPlaylistDatasource,
    private val application: CoroutineScope,
) : AddToPlaylistRepository {
    override suspend fun checkIfSongInFev(songId: Long): Boolean =
        application.async { local.checkIfSongInFev(songId) }.await()

    override suspend fun getTotalSongsInFev(): Int =
        application.async { local.getTotalSongsInFev() }.await()

    override suspend fun getPlaylistData(songId: Long): List<Pair<Pair<SIZE, PRESENT>, PrevSavedPlaylist>> =
        application.async { local.getPlaylistData(songId) }.await()


    override suspend fun saveSong(songId: Long): EmptyResult<DataError.Network> =
        application.async {
            if (local.checkIfSongInDatabase(songId)) return@async Result.Success(Unit)
            saveSong(songId)
        }.await()

    override suspend fun editPlaylist(
        songId: Long,
        playlistIdList: Map<Long, Boolean>,
    ): EmptyResult<DataError.Network> = application.async {
        val result = async { remote.editPlaylist(songId, playlistIdList) }.await()
        if (result is Result.Success) local.editPlaylist(songId, playlistIdList)

        result
    }.await()


    override suspend fun addSongToFavourite(
        songId: Long,
    ): EmptyResult<DataError.Network> = application.async {
        val result = async { remote.addSongToFavourite(songId) }.await()
        if (result is Result.Success) local.addSongToFavourite(songId)

        result
    }.await()

    override suspend fun removeSongFromFavourite(songId: Long): EmptyResult<DataError.Network> =
        application.async {
            val result = async { remote.removeSongToFavourite(songId) }.await()
            if (result is Result.Success) local.removeSongFromFavourite(songId)

            result
        }.await()

    override suspend fun createPlaylist(
        songId: Long,
        name: String,
    ): EmptyResult<DataError.Network> {
        val result = remote.createPlaylist(songId, name)

        if (result is Result.Success)
            application.async { local.createPlaylist(result.data) }.await()
        return result.asEmptyDataResult()
    }
}