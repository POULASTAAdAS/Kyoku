package com.poulastaa.kyoku.data.remote

import com.poulastaa.kyoku.data.model.api.service.home.HomeReq
import com.poulastaa.kyoku.data.model.api.service.home.HomeResponse
import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateReq
import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateResponse
import com.poulastaa.kyoku.data.model.api.service.setup.spotiry_playlist.SpotifyPlaylistResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.StoreArtistReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.StoreArtistResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.SuggestArtistReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.SuggestArtistResponse
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

    @POST("/api/authorised/suggestArtist")
    suspend fun suggestArtist(
        @Body request: SuggestArtistReq
    ): SuggestArtistResponse

    @POST("/api/authorised/storeArtist")
    suspend fun storeArtist(
        @Body request: StoreArtistReq
    ): StoreArtistResponse

    @POST("/api/authorised/home")
    suspend fun homeReq(
        @Body request: HomeReq
    ): HomeResponse
}