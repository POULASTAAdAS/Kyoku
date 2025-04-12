package com.poulastaa.add.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistSearchFilterType
import com.poulastaa.add.domain.repository.RemoteAddSongToPlaylistDatasource
import com.poulastaa.add.network.mapper.toDtoAddSongToPlaylistPageItem
import com.poulastaa.add.network.model.AddSongToPlaylistPageItemResponse
import com.poulastaa.add.network.model.AddSongToPlaylistRequest
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.DtoSong
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.model.PlaylistId
import com.poulastaa.core.domain.model.SongId
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.mapper.toDtoSong
import com.poulastaa.core.network.model.ResponseSong
import com.poulastaa.core.network.req
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.InternalSerializationApi
import okhttp3.OkHttpClient
import javax.inject.Inject

@OptIn(InternalSerializationApi::class)
internal class OkHttpAddSongToPlaylistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val pager: AddSongToPlaylistPagingSource,
) : RemoteAddSongToPlaylistDatasource {
    override suspend fun loadStaticData(): Result<List<DtoAddSongToPlaylistPageItem>, DataError.Network> =
        client.req<Unit, List<AddSongToPlaylistPageItemResponse>>(
            route = EndPoints.Add.Playlist.CreatePlaylistStaticData.route,
            method = ApiMethodType.GET,
            gson = gson
        ).map { list ->
            list.map { it.toDtoAddSongToPlaylistPageItem() }
        }

    override fun search(
        query: String,
        filterType: DtoAddSongToPlaylistSearchFilterType,
    ): Flow<PagingData<DtoAddSongToPlaylistItem>> {
        pager.init(query, filterType)

        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 15),
            initialKey = 1,
            pagingSourceFactory = { pager }
        ).flow
    }

    override suspend fun saveSong(
        playlistId: PlaylistId,
        songId: SongId,
    ): Result<DtoSong, DataError.Network> = client.req<AddSongToPlaylistRequest, ResponseSong>(
        route = EndPoints.Add.AddSong.route,
        method = ApiMethodType.POST,
        body = AddSongToPlaylistRequest(
            playlistId = playlistId,
            songId = songId
        ),
        gson = gson
    ).map { it.toDtoSong() }
}