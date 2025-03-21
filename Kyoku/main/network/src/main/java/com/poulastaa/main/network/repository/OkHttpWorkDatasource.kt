package com.poulastaa.main.network.repository

import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.AlbumId
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.DtoArtist
import com.poulastaa.core.domain.model.DtoFullAlbum
import com.poulastaa.core.domain.model.DtoFullPlaylist
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.ReqParam
import com.poulastaa.core.network.model.ResponseArtist
import com.poulastaa.core.network.model.ResponseFullAlbum
import com.poulastaa.core.network.model.ResponseFullPlaylist
import com.poulastaa.core.network.model.ResponseSong
import com.poulastaa.core.network.req
import com.poulastaa.main.domain.model.DtoSyncData
import com.poulastaa.main.domain.model.DtoSyncPlaylistSongs
import com.poulastaa.main.domain.repository.work.RemoteWorkDatasource
import com.poulastaa.main.network.mapper.toDtoSyncData
import com.poulastaa.main.network.model.SyncPlaylistPayload
import com.poulastaa.main.network.model.SyncPlaylistSongResponse
import com.poulastaa.main.network.model.SyncReq
import com.poulastaa.main.network.model.SyncResponse
import com.poulastaa.main.network.model.SyncType
import okhttp3.OkHttpClient
import javax.inject.Inject

internal class OkHttpWorkDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteWorkDatasource {
    override suspend fun syncSavedAlbums(list: List<AlbumId>): Result<DtoSyncData<DtoFullAlbum>, DataError.Network> =
        client.req<SyncReq<Long>, SyncResponse<ResponseFullAlbum>>(
            route = EndPoints.SyncLibrary.route,
            method = ApiMethodType.POST,
            body = SyncReq(
                idList = list,
            ),
            params = listOf(
                ReqParam(
                    key = "type",
                    value = SyncType.SYNC_ALBUM.name
                )
            ),
            gson = gson
        ).map { it.toDtoSyncData() }

    override suspend fun syncSavedPlaylist(list: List<PlaylistId>): Result<DtoSyncData<DtoFullPlaylist>, DataError.Network> =
        client.req<SyncReq<Long>, SyncResponse<ResponseFullPlaylist>>(
            route = EndPoints.SyncLibrary.route,
            method = ApiMethodType.POST,
            body = SyncReq(
                idList = list,
            ),
            params = listOf(
                ReqParam(
                    key = "type",
                    value = SyncType.SYNC_PLAYLIST.name
                )
            ),
            gson = gson
        ).map { it.toDtoSyncData() }

    override suspend fun syncSavedArtist(list: List<ArtistId>): Result<DtoSyncData<DtoArtist>, DataError.Network> =
        client.req<SyncReq<Long>, SyncResponse<ResponseArtist>>(
            route = EndPoints.SyncLibrary.route,
            method = ApiMethodType.POST,
            body = SyncReq(
                idList = list,
            ),
            params = listOf(
                ReqParam(
                    key = "type",
                    value = SyncType.SYNC_ARTIST.name
                )
            ),
            gson = gson
        ).map { it.toDtoSyncData() }

    override suspend fun syncSavedFavourite(list: List<SongId>): Result<DtoSyncData<DtoSong>, DataError.Network> =
        client.req<SyncReq<Long>, SyncResponse<ResponseSong>>(
            route = EndPoints.SyncLibrary.route,
            method = ApiMethodType.POST,
            body = SyncReq(
                idList = list,
            ),
            params = listOf(
                ReqParam(
                    key = "type",
                    value = SyncType.SYNC_FAVOURITE.name
                )
            ),
            gson = gson
        ).map { it.toDtoSyncData() }

    override suspend fun syncSavedPlaylistSongs(list: List<Pair<PlaylistId, List<SongId>>>): Result<DtoSyncPlaylistSongs<Pair<PlaylistId, List<DtoSong>>>, DataError.Network> =
        client.req<SyncReq<SyncPlaylistPayload>, SyncPlaylistSongResponse<Pair<PlaylistId, List<ResponseSong>>>>(
            route = EndPoints.SyncLibrary.route,
            method = ApiMethodType.POST,
            body = SyncReq(
                idList = list.map {
                    SyncPlaylistPayload(
                        playlistId = it.first,
                        listOfSongId = it.second
                    )
                },
            ),
            params = listOf(
                ReqParam(
                    key = "type",
                    value = SyncType.SYNC_PLAYLIST_SONGS.name
                )
            ),
            gson = gson
        ).map { it.toDtoSyncData() }
}