package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.RootDao
import com.poulastaa.core.database.mapper.toEntityPlaylist
import com.poulastaa.core.domain.model.DtoPlaylist
import com.poulastaa.core.domain.repository.LocalCreatePlaylistDatasource
import javax.inject.Inject

internal class RoomLocalCreatePlaylistDatasource @Inject constructor(
    private val root: RootDao,
) : LocalCreatePlaylistDatasource {
    override suspend fun savePlaylist(playlist: DtoPlaylist) {
        root.insertPlaylist(playlist.toEntityPlaylist())
    }
}