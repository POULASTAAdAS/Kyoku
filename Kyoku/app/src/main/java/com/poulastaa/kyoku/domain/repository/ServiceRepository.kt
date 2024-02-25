package com.poulastaa.kyoku.domain.repository

import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateReq
import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateResponse
import com.poulastaa.kyoku.data.model.api.service.setup.spotiry_playlist.SpotifyPlaylistResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreResponse

interface ServiceRepository {
    suspend fun getSpotifyPlaylist(
        playlistId: String,
    ): SpotifyPlaylistResponse

    suspend fun sendBDateToServer(req: SetBDateReq): SetBDateResponse
    suspend fun suggestGenre(req: SuggestGenreReq): SuggestGenreResponse
}