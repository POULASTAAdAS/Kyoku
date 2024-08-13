package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.domain.add_to_playlist.RemoteAddToPlaylistDatasource
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstAddToPlaylistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) : RemoteAddToPlaylistDatasource {
    override suspend fun addSongToPlaylist(songId: Long, playlistId: Long) {

    }

    override suspend fun addSongToFavourite(songId: Long) {

    }
}