package com.poulastaa.kyoku.data.remote

import com.poulastaa.kyoku.data.model.api.service.SpotifyPlaylistResponse
import com.poulastaa.kyoku.data.model.api.service.setup.SetBDateReq
import com.poulastaa.kyoku.data.model.api.service.setup.SetBDateResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ServiceApi {
    @POST("/api/authorised/spotifyPlaylist")
    suspend fun getSpotifyPlaylistSong(
        @Query("playlistId") playlistId: String
    ): SpotifyPlaylistResponse

    @POST("/api/authorised/storeBDate")
    suspend fun sendBDateToServer(
        @Body request: SetBDateReq
    ): SetBDateResponse
}