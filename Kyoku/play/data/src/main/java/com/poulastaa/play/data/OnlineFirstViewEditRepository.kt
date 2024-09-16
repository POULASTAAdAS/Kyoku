package com.poulastaa.play.data

import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.ViewEditType
import com.poulastaa.core.domain.repository.view_edit.LocalViewEditDatasource
import com.poulastaa.core.domain.repository.view_edit.RemoteViewEditDatasource
import com.poulastaa.core.domain.repository.view_edit.ViewEditRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstViewEditRepository @Inject constructor(
    private val local: LocalViewEditDatasource,
    private val remote: RemoteViewEditDatasource
) : ViewEditRepository {
    override fun getSongs(playlistId: Long, type: ViewEditType): Flow<List<PlaylistSong>> =
        local.getSongs(playlistId, type)

    override suspend fun deleteSong(
        playlistId: Long,
        songId: Long,
    ): EmptyResult<DataError.Network> {
        val result = remote.deleteSong(playlistId, songId)
        if (result is Result.Success) local.deleteSong(playlistId, songId)

        return result
    }

    override suspend fun addSong(playlistId: Long, songId: Long): EmptyResult<DataError.Network> {
        val result = remote.addSong(playlistId, songId)
        if (result is Result.Success) local.addSong(playlistId, songId)

        return result.asEmptyDataResult()
    }
}