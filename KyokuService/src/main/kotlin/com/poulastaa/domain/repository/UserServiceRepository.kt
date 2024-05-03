package com.poulastaa.domain.repository

import com.poulastaa.data.model.artist.*
import com.poulastaa.data.model.common.ResponseSong
import com.poulastaa.data.model.home.*
import com.poulastaa.data.model.item.ItemReq
import com.poulastaa.data.model.pinned.PinnedReq
import com.poulastaa.data.model.playlist.AddSongToPlaylistReq
import com.poulastaa.data.model.playlist.CreatePlaylistReq
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
import com.poulastaa.data.model.utils.UserTypeHelper

interface UserServiceRepository {
    suspend fun getFoundSpotifySongs(json: String, helper: UserTypeHelper): SpotifyPlaylistResponse

    suspend fun storeBDate(date: Long, helper: UserTypeHelper): SetBDateResponse

    suspend fun suggestGenre(req: SuggestGenreReq, helper: UserTypeHelper): SuggestGenreResponse
    suspend fun storeGenre(req: StoreGenreReq, helper: UserTypeHelper): StoreGenreResponse

    suspend fun suggestArtist(req: SuggestArtistReq, helper: UserTypeHelper): SuggestArtistResponse
    suspend fun storeArtist(req: StoreArtistReq, helper: UserTypeHelper): StoreArtistResponse

    suspend fun generateHomeResponse(req: HomeReq, helper: UserTypeHelper): HomeResponse

    suspend fun getMostPopularSongOfArtist(req: ArtistMostPopularSongReq): ArtistMostPopularSongRes

    suspend fun artistPageAlbumResponse(req: ArtistPageReq): List<ArtistAlbum>

    suspend fun getArtistPageSongResponse(req: ArtistPageReq): List<SongPreview>

    suspend fun getAlbum(id: Long): ResponseAlbum
    suspend fun editAlbum(id: Long, operation: Boolean, helper: UserTypeHelper): Boolean

    suspend fun getDailyMix(helper: UserTypeHelper): List<ResponseSong>

    suspend fun getArtistMix(helper: UserTypeHelper): List<ResponseSong>

    suspend fun handlePinnedOperation(helper: UserTypeHelper, req: PinnedReq): Boolean

    suspend fun handleItemOperations(helper: UserTypeHelper, req: ItemReq): Boolean

    suspend fun createPlaylist(helper: UserTypeHelper, req: CreatePlaylistReq): ResponsePlaylist

    suspend fun insertIntoFavourite(helper: UserTypeHelper, songId: Long): ResponseSong
    suspend fun removeFromFavourite(helper: UserTypeHelper, songId: Long): Boolean

    suspend fun editSongAndPlaylist(helper: UserTypeHelper, req: AddSongToPlaylistReq): ResponseSong

    suspend fun getResponseArtistOnSongId(songId: Long): List<ViewArtist>

    suspend fun removeFromHistory(songId: Long, helper: UserTypeHelper): Boolean

    suspend fun getSongOnId(songId: Long, helper: UserTypeHelper): ResponseSong
    suspend fun getListOfSong(songIdList: List<Long>, helper: UserTypeHelper): SongListResponse
}