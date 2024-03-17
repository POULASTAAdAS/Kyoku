package com.poulastaa.kyoku.domain.repository

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

interface ServiceRepository {
    suspend fun getSpotifyPlaylist(playlistId: String): SpotifyPlaylistResponse

    suspend fun sendBDateToServer(req: SetBDateReq): SetBDateResponse

    suspend fun suggestGenre(req: SuggestGenreReq): SuggestGenreResponse
    suspend fun storeGenre(req: StoreGenreReq): StoreGenreResponse

    suspend fun suggestArtist(req: SuggestArtistReq): SuggestArtistResponse
    suspend fun storeArtist(req: StoreArtistReq): StoreArtistResponse

    suspend fun homeReq(req: HomeReq): HomeResponse
}