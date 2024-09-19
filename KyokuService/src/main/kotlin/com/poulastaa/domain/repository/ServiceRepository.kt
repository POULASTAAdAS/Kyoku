package com.poulastaa.domain.repository

import com.poulastaa.data.model.*
import com.poulastaa.data.model.home.HomeDto
import com.poulastaa.domain.model.ReqUserPayload
import com.poulastaa.domain.model.route_model.req.home.HomeReq
import com.poulastaa.domain.model.route_model.req.pin.PinReq
import com.poulastaa.domain.model.route_model.req.playlist.CreatePlaylistWithSongReq
import com.poulastaa.domain.model.route_model.req.playlist.SavePlaylistReq
import com.poulastaa.domain.model.route_model.req.playlist.UpdatePlaylistReq

interface ServiceRepository {
    suspend fun getSpotifyPlaylist(
        userPayload: ReqUserPayload,
        spotifyPayload: List<SpotifySongTitle>,
    ): PlaylistDto

    suspend fun updateBDate(
        userPayload: ReqUserPayload,
        date: Long,
    ): Boolean

    suspend fun getGenre(
        userPayload: ReqUserPayload,
        genreIds: List<Int>,
    ): SuggestGenreDto

    suspend fun storeGenre(
        userPayload: ReqUserPayload,
        genreIds: List<Int>,
    ): Boolean

    suspend fun getArtist(
        userPayload: ReqUserPayload,
        artistIds: List<Long>,
    ): SuggestArtistDao

    suspend fun storeArtist(
        userPayload: ReqUserPayload,
        artistIds: List<Long>,
    ): Boolean

    suspend fun homeReq(
        userPayload: ReqUserPayload,
        req: HomeReq,
    ): HomeDto

    suspend fun getLoginData(
        userType: String,
        token: String,
    ): LogInDto

    suspend fun addToFavourite(
        id: Long,
        userPayload: ReqUserPayload,
    ): SongDto

    suspend fun removeFromFavourite(
        id: Long,
        userPayload: ReqUserPayload,
    ): Boolean

    suspend fun addArtist(
        list: List<Long>,
        payload: ReqUserPayload,
    ): AddArtistDto

    suspend fun removeArtist(
        artistId: Long,
        userPayload: ReqUserPayload,
    ): Boolean

    suspend fun getAlbum(
        albumId: Long,
        savedSongList: List<Long>,
        payload: ReqUserPayload,
    ): AlbumWithSongDto

    suspend fun addAlbum(
        list: List<Long>,
        payload: ReqUserPayload,
    ): AddAlbumDto

    suspend fun removeAlbum(
        id: Long,
        userPayload: ReqUserPayload,
    ): Boolean

    suspend fun savePlaylist(
        req: SavePlaylistReq,
        payload: ReqUserPayload,
    ): PlaylistDto

    suspend fun getSong(
        songId: Long,
    ): SongDto

    suspend fun updatePlaylist(
        req: UpdatePlaylistReq,
        payload: ReqUserPayload,
    ): Boolean

    suspend fun createPlaylist(
        req: CreatePlaylistWithSongReq,
        payload: ReqUserPayload,
    ): PlaylistDto

    suspend fun pinData(
        req: PinReq,
        payload: ReqUserPayload,
    ): Boolean

    suspend fun unPinData(
        req: PinReq,
        payload: ReqUserPayload,
    ): Boolean

    suspend fun deleteSavedData(
        id: Long,
        type: String,
        payload: ReqUserPayload,
    ): Boolean

    suspend fun getListOfData(
        req: GetDataReq,
        payload: ReqUserPayload,
    ): Any

    suspend fun getViewArtistData(
        artistId: Long,
        payload: ReqUserPayload,
    ): ViewArtistDto

    suspend fun getArtistOnId(
        artistId: Long,
        payload: ReqUserPayload,
    ): ArtistDto

    suspend fun getArtistSongPagingData(
        artistId: Long,
        page: Int,
        size: Int,
        payload: ReqUserPayload,
        savedSongList: List<Long>,
    ): ArtistPagerDataDto

    suspend fun getArtistAlbumPagingData(
        artistId: Long,
        page: Int,
        size: Int,
        payload: ReqUserPayload,
    ): ArtistPagerDataDto

    suspend fun getSyncData(
        req: UpdateSavedDataReq,
        payload: ReqUserPayload,
    ): SyncDto<Any>

    suspend fun getAlbumPaging(
        page: Int,
        size: Int,
        query: String,
        type: AlbumPagingTypeDto,
        payload: ReqUserPayload,
    ): PagingAlbumResDto

    suspend fun getArtistPaging(
        page: Int,
        size: Int,
        query: String,
        type: ArtistPagingTypeDto,
        payload: ReqUserPayload,
    ): PagingArtistResDto

    suspend fun getCreatePlaylistData(
        payload: ReqUserPayload,
    ): CreatePlaylistDto

    suspend fun getCreatePlaylistPagerData(
        page: Int,
        size: Int,
        query: String,
        type: CreatePlaylistPagerFilterTypeDto,
        payload: ReqUserPayload,
    ): CreatePlaylistPagingDtoWrapper

    suspend fun getSongArtist(
        songId: Long,
        payload: ReqUserPayload,
    ): SongArtistRes
}