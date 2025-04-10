package com.poulastaa.add.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistPageItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistSearchUiFilterType
import com.poulastaa.add.domain.repository.RemoteAddSongToPlaylistDatasource
import com.poulastaa.add.network.mapper.toDtoAddSongToPlaylistPageItem
import com.poulastaa.add.network.model.AddSongToPlaylistPageItemResponse
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
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
            route = EndPoints.Add.Playlist.CreatePlaylist.route,
            method = ApiMethodType.GET,
            gson = gson
        ).map { list ->
            list.map { it.toDtoAddSongToPlaylistPageItem() }
        }

    override fun search(
        query: String,
        filterType: DtoAddSongToPlaylistSearchUiFilterType,
    ): Flow<PagingData<DtoAddSongToPlaylistItem>> {
        pager.init(query, filterType)

        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 15),
            initialKey = 1,
            pagingSourceFactory = { pager }
        ).flow
    }
}