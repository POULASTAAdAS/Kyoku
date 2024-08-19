package com.poulastaa.play.data

import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.model.PlaylistSong
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
        val localPlaylist = local.getPlaylistOnId(id)
        if (localPlaylist.listOfSong.isNotEmpty()) return localPlaylist

        val remotePlaylist = remote.getPlaylistOnId(id)
//        application.async { local.savePlaylist(remotePlaylist) }.await()
        return remotePlaylist
    }

    override suspend fun getAlbumOnId(id: Long): ViewData {
        val localAlbum = local.getAlbumOnId(id)
        if (localAlbum.listOfSong.isNotEmpty()) return localAlbum

        val remoteAlbum = remote.getAlbumOnId(id)
//        application.async { local.saveAlbum(remoteAlbum) }.await()
        return remoteAlbum
    }

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