package com.poulastaa.network.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.poulastaa.core.data.network.get
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.CreatePlaylistPagerFilterType
import com.poulastaa.core.domain.model.CreatePlaylistPagingData
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.NoInternetException
import com.poulastaa.core.domain.utils.OtherRemoteException
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toCreatePlaylistPagerFilterTypeDto
import com.poulastaa.network.mapper.toCreatePlaylistPagingData
import com.poulastaa.network.model.CreatePlaylistPagerFilterTypeDto
import com.poulastaa.network.model.CreatePlaylistPagingDtoWrapper
import okhttp3.OkHttpClient
import javax.inject.Inject

class CreatePlaylistPagerSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : PagingSource<Int, CreatePlaylistPagingData>() {
    private var query: String = ""
    private var type: CreatePlaylistPagerFilterTypeDto = CreatePlaylistPagerFilterTypeDto.ALL
    private var savedSongIdList: List<Long> = emptyList()

    fun init(
        query: String,
        type: CreatePlaylistPagerFilterType,
        savedSongIdList: List<Long>,
    ) {
        this.query = query
        this.type = type.toCreatePlaylistPagerFilterTypeDto()
        this.savedSongIdList = savedSongIdList
    }

    override fun getRefreshKey(state: PagingState<Int, CreatePlaylistPagingData>): Int? =
        state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CreatePlaylistPagingData> {
        val page = params.key ?: 1

        val result = client.get<CreatePlaylistPagingDtoWrapper>(
            route = EndPoints.GetCreatePlaylistPagerData.route,
            params = listOf(
                "page" to page.toString(),
                "size" to 15.toString(),
                "query" to query,
                "type" to type.name,
                "savedSongIdList" to savedSongIdList.joinToString(",")
            ),
            gson = gson
        ).map { dto ->
            dto.list.map { it.toCreatePlaylistPagingData() }
        }

        return when (result) {
            is Result.Error -> {
                when (result.error) {
                    DataError.Network.NO_INTERNET -> LoadResult.Error(NoInternetException)

                    else -> LoadResult.Error(OtherRemoteException)
                }
            }

            is Result.Success -> {
                LoadResult.Page(
                    data = result.data,
                    prevKey = if (page == 1) null else page.minus(1),
                    nextKey = if (result.data.isEmpty()) null else page.plus(1)
                )
            }
        }
    }
}