package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.ViewData
import com.poulastaa.core.domain.AlbumData
import com.poulastaa.core.domain.model.PlaylistData
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.view.RemoteViewDatasource
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstViewDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) : RemoteViewDatasource {
    override suspend fun getPlaylistOnId(id: Long): PlaylistData {
        return PlaylistData()
    }

    override suspend fun getAlbumOnId(id: Long): AlbumData {
        return AlbumData()
    }

    override suspend fun getFev(): List<Song> {
        return listOf()
    }

    override suspend fun getOldMix(prevList: List<Long>): List<Song> {
        return listOf()
    }

    override suspend fun getArtistMix(prevList: List<Long>): List<Song> {
        return listOf()
    }

    override suspend fun getPopularMix(prevList: List<Long>): List<Song> {
        return listOf()
    }

    override suspend fun getSongOnIdList(list: List<Long>): List<Song> {
        return listOf()
    }
}