package com.poulastaa.explore.network.repository.all_from_artist

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.NoInternetException
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.UnknownRemoteException
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.ArtistId
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.ReqParam
import com.poulastaa.core.network.req
import com.poulastaa.explore.domain.model.DtoExploreItem
import com.poulastaa.explore.network.mapper.toDtoAllFromArtistItem
import com.poulastaa.explore.network.model.ResponseExploreItem
import kotlinx.serialization.InternalSerializationApi
import okhttp3.OkHttpClient
import javax.inject.Inject

@OptIn(InternalSerializationApi::class)
internal class AllFromArtistAlbumPagingSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : PagingSource<Int, DtoExploreItem>() {
    private var artistId: ArtistId? = null
    private var query: String = ""

    fun init(artistId: ArtistId, query: String) {
        this.artistId = artistId
        this.query = query
    }

    override fun getRefreshKey(state: PagingState<Int, DtoExploreItem>) = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DtoExploreItem> {
        if (artistId == null) return LoadResult.Error(UnknownRemoteException)

        val page = params.key ?: 1

        val result = client.req<Unit, List<ResponseExploreItem>>(
            route = EndPoints.Artist.GetArtistPagingAlbums.route,
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
                    key = "artistId",
                    value = artistId.toString()
                ),
                ReqParam(
                    key = "query",
                    value = query
                )
            ),
            gson = gson
        ).map { it -> it.map { it.toDtoAllFromArtistItem() } }

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