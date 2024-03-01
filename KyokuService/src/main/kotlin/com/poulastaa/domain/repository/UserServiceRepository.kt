package com.poulastaa.domain.repository

import com.poulastaa.data.model.utils.UserTypeHelper
import com.poulastaa.data.model.setup.artist.StoreArtistReq
import com.poulastaa.data.model.setup.artist.StoreArtistResponse
import com.poulastaa.data.model.setup.artist.SuggestArtistReq
import com.poulastaa.data.model.setup.artist.SuggestArtistResponse
import com.poulastaa.data.model.setup.genre.StoreGenreReq
import com.poulastaa.data.model.setup.genre.StoreGenreResponse
import com.poulastaa.data.model.setup.genre.SuggestGenreReq
import com.poulastaa.data.model.setup.genre.SuggestGenreResponse
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponse
import com.poulastaa.data.model.setup.spotify.SpotifyPlaylistResponse

interface UserServiceRepository {
    suspend fun getFoundSpotifySongs(json: String, helper: UserTypeHelper): SpotifyPlaylistResponse

    suspend fun storeBDate(date: Long, helper: UserTypeHelper): SetBDateResponse

    suspend fun suggestGenre(req: SuggestGenreReq, helper: UserTypeHelper): SuggestGenreResponse
    suspend fun storeGenre(req: StoreGenreReq, helper: UserTypeHelper): StoreGenreResponse

    suspend fun suggestArtist(req: SuggestArtistReq, helper: UserTypeHelper): SuggestArtistResponse
    suspend fun storeArtist(req: StoreArtistReq, helper: UserTypeHelper): StoreArtistResponse
}