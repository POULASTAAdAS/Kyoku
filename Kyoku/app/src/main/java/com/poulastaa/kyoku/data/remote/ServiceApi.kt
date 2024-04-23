package com.poulastaa.kyoku.data.remote

import com.poulastaa.kyoku.data.model.api.service.ResponseSong
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistAlbum
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistMostPopularSongReq
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistMostPopularSongRes
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistPageReq
import com.poulastaa.kyoku.data.model.api.service.artist.ViewArtist
import com.poulastaa.kyoku.data.model.api.service.home.HomeReq
import com.poulastaa.kyoku.data.model.api.service.home.HomeResponse
import com.poulastaa.kyoku.data.model.api.service.home.ResponseAlbum
import com.poulastaa.kyoku.data.model.api.service.home.ResponsePlaylist
import com.poulastaa.kyoku.data.model.api.service.home.SongPreview
import com.poulastaa.kyoku.data.model.api.service.item.ItemReq
import com.poulastaa.kyoku.data.model.api.service.pinned.PinnedReq
import com.poulastaa.kyoku.data.model.api.service.playlist.AddSongToPlaylistReq
import com.poulastaa.kyoku.data.model.api.service.playlist.CreatePlaylistReq
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
import retrofit2.http.GET
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

    @POST("/api/authorised/artist")
    suspend fun artistMostPopularReq(
        @Body request: ArtistMostPopularSongReq
    ): ArtistMostPopularSongRes

    @POST("/api/authorised/artist/artistPageAlbum")
    suspend fun getArtistAlbumAsPage(
        @Body request: ArtistPageReq
    ): List<ArtistAlbum>

    @POST("/api/authorised/artist/artistPageSongs")
    suspend fun getArtistSongAsPage(
        @Body request: ArtistPageReq
    ): List<SongPreview>

    @POST("/api/authorised/album")
    suspend fun getAlbum(
        @Query("id") id: Long
    ): ResponseAlbum

    @POST("/api/authorised/editAlbum")
    suspend fun editAlbum(
        @Query("id") id: Long,
        @Query("op") op: Boolean
    ): Boolean

    @GET("/api/authorised/dailyMix")
    suspend fun getDailyMix(): List<ResponseSong>

    @GET("/api/authorised/artistMix")
    suspend fun getArtistMix(): List<ResponseSong>

    @POST("/api/authorised/pinned")
    suspend fun handlePin(
        @Body request: PinnedReq
    ): Boolean

    @POST("/api/authorised/item")
    suspend fun handleItemReq(
        @Body request: ItemReq
    ): Boolean

    @POST("/api/authorised/playlistOnSongId")
    suspend fun getPlaylistOnSongId(
        @Body request: CreatePlaylistReq
    ): ResponsePlaylist

    @POST("/api/authorised/playlistOnAlbumId")
    suspend fun getPlaylistOnAlbumId(
        @Body request: CreatePlaylistReq
    ): ResponsePlaylist

    @GET("/api/authorised/addSongToFavourite")
    suspend fun addSongToFavourite(
        @Query("songId") id: Long
    ): ResponseSong

    @GET("/api/authorised/removeSongFromFavourite")
    suspend fun removeFromFavourite(
        @Query("songId") id: Long
    ): Boolean

    @POST("/api/authorised/addSongToPlaylist")
    suspend fun addSongToPlaylist(
        @Body request: AddSongToPlaylistReq
    ): ResponseSong

    @GET("/api/authorised/songArtist")
    suspend fun getArtistOnSongId(
        @Query("songId") songId: Long
    ): List<ViewArtist>

    @GET("/api/authorised/removeFromHistory")
    suspend fun removeFromRecentlyPlayed(
        @Query("songId") songId: Long
    ): Boolean

    @GET("/api/authorised/getSongOnId")
    suspend fun getSongOnId(
        @Query("songId") songId: Long
    ): ResponseSong
}