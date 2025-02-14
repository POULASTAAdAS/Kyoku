package com.poulastaa.setup.network.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.NoInternetException
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.UnknownRemoteException
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.ReqParam
import com.poulastaa.core.network.req
import com.poulastaa.core.domain.model.DtoPrevArtist
import com.poulastaa.setup.network.model.SuggestedArtistRes
import com.poulastaa.setup.network.toDtoPrevArtist
import okhttp3.OkHttpClient
import javax.inject.Inject

class SuggestArtistPagingSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : PagingSource<Int, DtoPrevArtist>() {
    private var query: String = ""

    fun init(query: String) {
        this.query = query
    }

    override fun getRefreshKey(state: PagingState<Int, DtoPrevArtist>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DtoPrevArtist> {
        val page = params.key ?: 1
        val size = params.loadSize

        val result = client.req<Unit, SuggestedArtistRes>(
            route = EndPoints.SuggestArtist.route,
            method = ApiMethodType.GET,
            params = listOf(
                ReqParam("page", page.toString()),
                ReqParam("size", size.toString()),
                ReqParam("query", query)
            ),
            gson = gson
        ).map { it.toDtoPrevArtist() }

        return when (result) {
            is Result.Error -> {
                when (result.error) {
                    DataError.Network.NO_INTERNET -> LoadResult.Error(NoInternetException)
                    else -> LoadResult.Error(UnknownRemoteException)
                }
            }

            is Result.Success -> {
                LoadResult.Page(
                    data = result.data,
                    prevKey = if (page == 1) null else page.minus(1),
                    nextKey = if (result.data.size >= size) page.plus(1)
                    else null
                )
            }
        }
    }
}