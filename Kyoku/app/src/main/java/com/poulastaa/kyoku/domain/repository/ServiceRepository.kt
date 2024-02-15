package com.poulastaa.kyoku.domain.repository

import com.poulastaa.kyoku.data.model.api.service.SpotifyPlaylistResponse
import com.poulastaa.kyoku.data.model.api.service.setup.SetBDateReq
import com.poulastaa.kyoku.data.model.api.service.setup.SetBDateResponse

interface ServiceRepository {
    suspend fun getSpotifyPlaylist(
        playlistId: String,
    ): SpotifyPlaylistResponse

    suspend fun sendBDateToServer(req: SetBDateReq): SetBDateResponse
}