package com.poulastaa.kyoku.data.repository

import com.poulastaa.kyoku.data.model.api.service.HandleSpotifyPlaylistStatus
import com.poulastaa.kyoku.data.model.api.service.SpotifyPlaylistResponse
import com.poulastaa.kyoku.data.remote.ServiceApi
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val serviceApi: ServiceApi
) : ServiceRepository {
    override suspend fun getSpotifyPlaylist(
        playlistId: String,
    ): SpotifyPlaylistResponse {
        return try {
            serviceApi.getSpotifyPlaylistSong(playlistId = playlistId)
        } catch (e: Exception) {

            SpotifyPlaylistResponse(
                status = HandleSpotifyPlaylistStatus.FAILURE,
                listOfResponseSong = emptyList()
            )
        }
    }
}