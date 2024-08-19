package com.poulastaa.core.database.repository

import com.poulastaa.core.database.dao.CommonDao
import com.poulastaa.core.database.dao.LibraryDao
import com.poulastaa.core.domain.ViewSong
import com.poulastaa.core.domain.view.LocalViewDatasource
import javax.inject.Inject

class RoomLocalViewDatasource @Inject constructor(
    private val commonDao: CommonDao,
    private val libraryDao: LibraryDao
) : LocalViewDatasource {
    override suspend fun getPlaylistOnId(id: Long): List<ViewSong> {
        return listOf()
    }

    override suspend fun getAlbumOnId(id: Long): List<ViewSong> {
        return listOf()
    }

    override suspend fun getFev(): List<ViewSong> {
        return listOf()
    }

    override suspend fun getOldMix(): List<ViewSong> {
        return listOf()
    }

    override suspend fun getArtistMix(): List<ViewSong> {
        return listOf()
    }

    override suspend fun getPopularMix(): List<ViewSong> {
        return listOf()
    }

}