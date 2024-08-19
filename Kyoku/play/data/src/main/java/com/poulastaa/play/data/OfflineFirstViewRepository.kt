package com.poulastaa.play.data

import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.ViewSong
import com.poulastaa.core.domain.view.LocalViewDatasource
import com.poulastaa.core.domain.view.RemoteViewDatasource
import com.poulastaa.core.domain.view.ViewRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class OfflineFirstViewRepository @Inject constructor(
    private val local: LocalViewDatasource,
    private val remote: RemoteViewDatasource,
    private val application: CoroutineScope
) : ViewRepository {
    override suspend fun getPlaylistOnId(id: Long): ViewData {
        return ViewData()
    }

    override suspend fun getAlbumOnId(id: Long): ViewData {
        return ViewData()
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