package com.poulastaa.core.database.repository

import com.poulastaa.core.ViewData
import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.database.dao.ViewDao
import com.poulastaa.core.database.mapper.toViewData
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.view.LocalViewDatasource
import javax.inject.Inject

class RoomLocalViewDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val libraryDao: LibraryDao,
    private val viewDao: ViewDao
) : LocalViewDatasource {
    override suspend fun getPlaylistOnId(id: Long): ViewData =
        viewDao.getPlaylistOnId(id).groupBy { it.playlistId }
            .map {
                it.value.toViewData(it.key)
            }.firstOrNull() ?: ViewData()

    override suspend fun getAlbumOnId(id: Long): ViewData =
        viewDao.getAlbumOnId(id).groupBy { it.playlistId }
            .map { it.value.toViewData(it.key) }.firstOrNull() ?: ViewData()

    override suspend fun getFev(): List<PlaylistSong> {
        return listOf()
    }

    override suspend fun getOldMix(): List<PlaylistSong> {
        return listOf()
    }

    override suspend fun getArtistMix(): List<PlaylistSong> {
        return listOf()
    }

    override suspend fun getPopularMix(): List<PlaylistSong> {
        return listOf()
    }

}