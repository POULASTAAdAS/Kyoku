package com.poulastaa.network.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.poulastaa.core.data.network.get
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.AlbumPagingType
import com.poulastaa.core.domain.model.PagingAlbumData
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.NoInternetException
import com.poulastaa.core.domain.utils.OtherRemoteException
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toAlbumPagingTypeDto
import com.poulastaa.network.mapper.toPagingAlbumData
import com.poulastaa.network.model.AlbumPagingTypeDto
import com.poulastaa.network.model.PagingAlbumResDto
import okhttp3.OkHttpClient
import javax.inject.Inject

class NewAlbumPagerSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : PagingSource<Int, PagingAlbumData>() {
    private var query: String = ""
    private var type: AlbumPagingTypeDto = AlbumPagingTypeDto.BY_POPULARITY

    fun init(
        query: String,
        type: AlbumPagingType
    ) {
        this.query = query
        this.type = type.toAlbumPagingTypeDto()
    }

    override fun getRefreshKey(state: PagingState<Int, PagingAlbumData>): Int? =
        state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PagingAlbumData> {
        val page = params.key ?: 1

        val result = client.get<PagingAlbumResDto>(
            route = EndPoints.GetArtistSong.route,
            params = listOf(
                "page" to page.toString(),
                "size" to 15.toString(),
                "query" to query,
                "type" to type.name
            ),
            gson = gson
        ).map {
            it.list.map { dto -> dto.toPagingAlbumData() }
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