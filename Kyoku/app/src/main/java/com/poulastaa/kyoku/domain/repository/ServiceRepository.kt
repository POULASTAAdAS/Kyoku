package com.poulastaa.kyoku.domain.repository

import com.poulastaa.kyoku.data.model.api.service.artist.ArtistAlbum
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistMostPopularSongReq
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistMostPopularSongRes
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistPageReq
import com.poulastaa.kyoku.data.model.api.service.home.AlbumPreview
import com.poulastaa.kyoku.data.model.api.service.home.DailyMixPreview
import com.poulastaa.kyoku.data.model.api.service.home.HomeReq
import com.poulastaa.kyoku.data.model.api.service.home.HomeResponse
import com.poulastaa.kyoku.data.model.api.service.home.SongPreview
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

    suspend fun getAlbum(id:Long): AlbumPreview

    suspend fun getDailyMix(): DailyMixPreview
}