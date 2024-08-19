package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.model.PlaylistSong
import com.poulastaa.core.domain.view.RemoteViewDatasource
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstViewDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) : RemoteViewDatasource {
    override suspend fun getPlaylistOnId(id: Long): ViewData {
        return ViewData()
    }

    override suspend fun getAlbumOnId(id: Long):ViewData {
        return ViewData()
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