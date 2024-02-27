package com.poulastaa.domain.repository

import com.poulastaa.data.model.UserType
import com.poulastaa.data.model.UserTypeHelper
import com.poulastaa.data.model.setup.genre.StoreGenreReq
import com.poulastaa.data.model.setup.genre.StoreGenreResponse
import com.poulastaa.data.model.setup.genre.SuggestGenreReq
import com.poulastaa.data.model.setup.genre.SuggestGenreResponse
import com.poulastaa.data.model.setup.set_b_date.SetBDateResponse
import com.poulastaa.data.model.spotify.SpotifyPlaylistResponse
import java.io.File

interface UserServiceRepository {
    suspend fun getFoundSpotifySongs(json: String, user: UserTypeHelper): SpotifyPlaylistResponse
    suspend fun getSongCover(name: String): File?

    suspend fun storeBDate(date: Long, userType: UserType, id: String): SetBDateResponse

    suspend fun suggestGenre(req: SuggestGenreReq, userType: UserTypeHelper): SuggestGenreResponse
    suspend fun storeGenre(req: StoreGenreReq, helper: UserTypeHelper): StoreGenreResponse
}