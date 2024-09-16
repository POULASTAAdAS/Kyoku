package com.poulastaa.play.data

import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.model.ViewEditType
import com.poulastaa.core.domain.repository.view_edit.LocalViewEditDatasource
import com.poulastaa.core.domain.repository.view_edit.ViewEditRepository
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstViewEditRepository @Inject constructor(
    private val local: LocalViewEditDatasource,
) : ViewEditRepository {
    override fun getSongs(playlistId: Long, type: ViewEditType): Flow<List<PlaylistSong>> =
        local.getSongs(playlistId, type)

    override suspend fun deleteSong(
        playlistId: Long,
        songId: Long,
    ): EmptyResult<DataError.Network> {
        return Result.Error(DataError.Network.NOT_FOUND)
    }

    override suspend fun addSong(playlistId: Long, songId: Long): EmptyResult<DataError.Network> {
        return Result.Error(DataError.Network.NOT_FOUND)
    }
}