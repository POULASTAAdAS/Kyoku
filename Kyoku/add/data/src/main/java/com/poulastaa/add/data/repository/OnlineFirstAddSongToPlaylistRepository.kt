package com.poulastaa.add.data.repository

import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.repository.AddSongToPlaylistRepository
import com.poulastaa.add.domain.repository.RemoteAddSongToPlaylistDatasource
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import javax.inject.Inject

internal class OnlineFirstAddSongToPlaylistRepository @Inject constructor(
    private val remote: RemoteAddSongToPlaylistDatasource,
) : AddSongToPlaylistRepository {
    override suspend fun loadStaticData(): Result<List<DtoAddSongToPlaylistPageItem>, DataError.Network> {
        return remote.loadStaticData()
    }
}