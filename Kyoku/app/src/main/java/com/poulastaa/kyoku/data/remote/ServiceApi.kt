package com.poulastaa.kyoku.data.remote

import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateReq
import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateResponse
import com.poulastaa.kyoku.data.model.api.service.setup.spotiry_playlist.SpotifyPlaylistResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.StoreGenreReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.StoreGenreResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreResponse
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

    @POST("/api/authorised/suggestGenre")
    suspend fun suggestGenre(
        @Body request: SuggestGenreReq
    ): SuggestGenreResponse

    @POST("/api/authorised/storeGenre")
    suspend fun storeGenre(
        @Body request: StoreGenreReq
    ): StoreGenreResponse
}