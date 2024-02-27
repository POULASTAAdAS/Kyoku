package com.poulastaa.kyoku.data.repository

import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateReq
import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateResponse
import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateResponseStatus
import com.poulastaa.kyoku.data.model.api.service.setup.spotiry_playlist.HandleSpotifyPlaylistStatus
import com.poulastaa.kyoku.data.model.api.service.setup.spotiry_playlist.SpotifyPlaylistResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.GenreResponseStatus
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.StoreGenreReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.StoreGenreResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_genre.SuggestGenreResponse
import com.poulastaa.kyoku.data.remote.ServiceApi
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val api: ServiceApi
) : ServiceRepository {
    override suspend fun getSpotifyPlaylist(
        playlistId: String,
    ): SpotifyPlaylistResponse {
        return try {
            api.getSpotifyPlaylistSong(playlistId = playlistId)
        } catch (e: Exception) {
            SpotifyPlaylistResponse(
                status = HandleSpotifyPlaylistStatus.FAILURE,
                listOfResponseSong = emptyList()
            )
        }
    }

    override suspend fun sendBDateToServer(req: SetBDateReq): SetBDateResponse {
        return try {
            api.sendBDateToServer(req)
        } catch (e: Exception) {
            SetBDateResponse(SetBDateResponseStatus.FAILURE)
        }
    }

    override suspend fun suggestGenre(req: SuggestGenreReq): SuggestGenreResponse {
        return try {
            api.suggestGenre(req)
        } catch (e: Exception) {
            SuggestGenreResponse(status = GenreResponseStatus.FAILURE, emptyList())
        }
    }

    override suspend fun storeGenre(req: StoreGenreReq): StoreGenreResponse {
        return try {
            api.storeGenre(req)
        } catch (e: Exception) {
            StoreGenreResponse(
                status = GenreResponseStatus.FAILURE
            )
        }
    }
}