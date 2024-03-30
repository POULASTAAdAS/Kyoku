package com.poulastaa.kyoku.data.repository

import com.poulastaa.kyoku.data.model.api.service.artist.ArtistMostPopularSongReq
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistMostPopularSongRes
import com.poulastaa.kyoku.data.model.api.service.home.HomeReq
import com.poulastaa.kyoku.data.model.api.service.home.HomeResponse
import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateReq
import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateResponse
import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateResponseStatus
import com.poulastaa.kyoku.data.model.api.service.setup.spotiry_playlist.HandleSpotifyPlaylistStatus
import com.poulastaa.kyoku.data.model.api.service.setup.spotiry_playlist.SpotifyPlaylistResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.ArtistResponseStatus
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.StoreArtistReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.StoreArtistResponse
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.SuggestArtistReq
import com.poulastaa.kyoku.data.model.api.service.setup.suggest_artist.SuggestArtistResponse
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

    override suspend fun suggestArtist(req: SuggestArtistReq): SuggestArtistResponse {
        return try {
            api.suggestArtist(req)
        } catch (e: Exception) {
            SuggestArtistResponse(
                status = ArtistResponseStatus.FAILURE,
                artistList = emptyList()
            )
        }
    }

    override suspend fun storeArtist(req: StoreArtistReq): StoreArtistResponse {
        return try {
            api.storeArtist(req)
        } catch (e: Exception) {
            StoreArtistResponse(
                status = ArtistResponseStatus.FAILURE
            )
        }
    }

    override suspend fun homeReq(req: HomeReq): HomeResponse {
        return try {
            api.homeReq(req)
        } catch (e: Exception) {
            HomeResponse()
        }
    }

    override suspend fun artistMostPopularReq(req: ArtistMostPopularSongReq): ArtistMostPopularSongRes {
        return try {
            api.artistMostPopularReq(req)
        } catch (e: Exception) {
            ArtistMostPopularSongRes()
        }
    }
}