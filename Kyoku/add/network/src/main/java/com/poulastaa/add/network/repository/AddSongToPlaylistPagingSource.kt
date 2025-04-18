package com.poulastaa.add.network.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistItem
import com.poulastaa.add.domain.model.DtoAddSongToPlaylistSearchFilterType
import com.poulastaa.add.network.mapper.toDtoAddSongToPlaylistItem
import com.poulastaa.add.network.model.AddSongToPlaylistItemResponse
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.NoInternetException
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.UnknownRemoteException
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.ReqParam
import com.poulastaa.core.network.req
import kotlinx.serialization.InternalSerializationApi
import okhttp3.OkHttpClient
import javax.inject.Inject

@OptIn(InternalSerializationApi::class)
internal class AddSongToPlaylistPagingSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : PagingSource<Int, DtoAddSongToPlaylistItem>() {
    private var query = ""
    private var filterType = DtoAddSongToPlaylistSearchFilterType.ALL

    override fun getRefreshKey(state: PagingState<Int, DtoAddSongToPlaylistItem>) =
        state.anchorPosition

    fun init(query: String, filterType: DtoAddSongToPlaylistSearchFilterType) {
        this.query = query
        this.filterType = filterType
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DtoAddSongToPlaylistItem> {
        val page = params.key ?: 1

        val result = client.req<Unit, List<AddSongToPlaylistItemResponse>>(
            route = EndPoints.Add.GetCreatePlaylist.route,
            method = ApiMethodType.GET,
            params = listOf(
                ReqParam(
                    key = "page",
                    value = page.toString()
                ),
                ReqParam(
                    key = "size",
                    value = params.loadSize.toString()
                ),
                ReqParam(
                    key = "filterType",
                    value = filterType.name
                ),
                ReqParam(
                    key = "query",
                    value = query.trim()
                )
            ),
            gson = gson
        ).map { list -> list.map { it.toDtoAddSongToPlaylistItem() } }

        return when (result) {
            is Result.Error -> when (result.error) {
                DataError.Network.NO_INTERNET -> LoadResult.Error(NoInternetException)
                else -> LoadResult.Error(UnknownRemoteException)
            }

            is Result.Success -> LoadResult.Page(
                data = result.data,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (result.data.isEmpty()) null else page.plus(1)
            )
        }
    }
}