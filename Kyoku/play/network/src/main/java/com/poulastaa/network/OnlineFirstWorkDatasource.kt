package com.poulastaa.network

import com.google.gson.Gson
import com.poulastaa.core.data.model.AlbumWithSongDto
import com.poulastaa.core.data.model.ArtistDto
import com.poulastaa.core.data.model.PlaylistDto
import com.poulastaa.core.data.model.SongDto
import com.poulastaa.core.data.network.post
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.AlbumWithSong
import com.poulastaa.core.domain.model.Artist
import com.poulastaa.core.domain.model.PlaylistWithSong
import com.poulastaa.core.domain.model.Song
import com.poulastaa.core.domain.model.SyncData
import com.poulastaa.core.domain.repository.work.RemoteWorkDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toAlbumWithSong
import com.poulastaa.network.mapper.toArtist
import com.poulastaa.network.mapper.toPlaylistWithSong
import com.poulastaa.network.mapper.toSong
import com.poulastaa.network.mapper.toSyncData
import com.poulastaa.network.model.SyncDto
import com.poulastaa.network.model.UpdateSavedDataReq
import com.poulastaa.network.model.UpdateSavedDataType
import okhttp3.OkHttpClient
import javax.inject.Inject

class OnlineFirstWorkDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson
) : RemoteWorkDatasource {
    override suspend fun getUpdatedAlbums(list: List<Long>): Result<SyncData<AlbumWithSong>, DataError.Network> =
        client.post<UpdateSavedDataReq, SyncDto<AlbumWithSongDto>>(
            route = EndPoints.SyncData.route,
            body = UpdateSavedDataReq(
                list = list,
                type = UpdateSavedDataType.ALBUM
            ),
            gson = gson
        ).map { dto ->
            dto.toSyncData {
                it.toAlbumWithSong()
            }
        }

    override suspend fun getUpdatedPlaylists(list: List<Long>): Result<SyncData<PlaylistWithSong>, DataError.Network> =
        client.post<UpdateSavedDataReq, SyncDto<PlaylistDto>>(
            route = EndPoints.SyncData.route,
            body = UpdateSavedDataReq(
                list = list,
                type = UpdateSavedDataType.PLAYLIST
            ),
            gson = gson
        ).map { dto ->
            dto.toSyncData {
                it.toPlaylistWithSong()
            }
        }

    override suspend fun getUpdatedArtists(list: List<Long>): Result<SyncData<Artist>, DataError.Network> =
        client.post<UpdateSavedDataReq, SyncDto<ArtistDto>>(
            route = EndPoints.SyncData.route,
            body = UpdateSavedDataReq(
                list = list,
                type = UpdateSavedDataType.ARTIST
            ),
            gson = gson
        ).map { dto ->
            dto.toSyncData {
                it.toArtist()
            }
        }

    override suspend fun getUpdatedFavourite(list: List<Long>): Result<SyncData<Song>, DataError.Network> =
        client.post<UpdateSavedDataReq, SyncDto<SongDto>>(
            route = EndPoints.SyncData.route,
            body = UpdateSavedDataReq(
                list = list,
                type = UpdateSavedDataType.ARTIST
            ),
            gson = gson
        ).map { dto ->
            dto.toSyncData {
                it.toSong()
            }
        }
}