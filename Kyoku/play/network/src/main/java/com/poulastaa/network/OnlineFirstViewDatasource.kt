package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.domain.ViewSong
import com.poulastaa.core.domain.view.RemoteViewDatasource
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstViewDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) : RemoteViewDatasource {
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