package com.poulastaa.kyoku.domain.repository

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

interface ServiceRepository {
    suspend fun getSpotifyPlaylist(playlistId: String): SpotifyPlaylistResponse

    suspend fun sendBDateToServer(req: SetBDateReq): SetBDateResponse

    suspend fun suggestGenre(req: SuggestGenreReq): SuggestGenreResponse
    suspend fun storeGenre(req: StoreGenreReq): StoreGenreResponse

    suspend fun suggestArtist(req: SuggestArtistReq): SuggestArtistResponse
    suspend fun storeArtist(req: StoreArtistReq): StoreArtistResponse

    suspend fun homeReq(req: HomeReq): HomeResponse

    suspend fun artistMostPopularReq(req: ArtistMostPopularSongReq): ArtistMostPopularSongRes

    suspend fun getArtistAlbumAsPage(req: ArtistPageReq): List<ArtistAlbum>
    suspend fun getArtistSongAsPage(req: ArtistPageReq): List<SongPreview>

    suspend fun getAlbum(id: Long): ResponseAlbum
    suspend fun editAlbum(id: Long, op: Boolean): Boolean

    suspend fun getDailyMix(): List<ResponseSong>
    suspend fun getArtistMix(): List<ResponseSong>

    suspend fun handlePin(req: PinnedReq): Boolean

    suspend fun handleItem(req: ItemReq): Boolean

    suspend fun getPlaylistOnSongId(req: CreatePlaylistReq): ResponsePlaylist

    suspend fun getPlaylistOnAlbumId(req: CreatePlaylistReq): ResponsePlaylist

    suspend fun addSongToFavourite(songId: Long): ResponseSong
    suspend fun removeFromFavourite(songId: Long): Boolean

    suspend fun addSongToPlaylist(req: AddSongToPlaylistReq): ResponseSong

    suspend fun getArtistOnSongId(songId: Long): List<ViewArtist>
}