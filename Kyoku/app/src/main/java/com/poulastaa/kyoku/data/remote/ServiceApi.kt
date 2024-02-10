package com.poulastaa.kyoku.data.remote

import com.poulastaa.kyoku.data.model.api.service.SpotifyPlaylistResponse
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ServiceApi {
    @POST("/api/authorised/spotifyPlaylist")
    suspend fun getSpotifyPlaylistSong(
        @Header("Authorization") tokenOrCookie: String,
        @Query("playlistId") playlistId: String
    ): SpotifyPlaylistResponse
}