package com.poulastaa.network.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.poulastaa.core.data.network.get
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.ArtistSingleData
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.NoInternetException
import com.poulastaa.core.domain.utils.OtherRemoteException
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.map
import com.poulastaa.network.mapper.toArtistSingleData
import com.poulastaa.network.model.ArtistPagerDataDto
import okhttp3.OkHttpClient
import javax.inject.Inject

class ExploreArtistAlbumPagerSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : PagingSource<Int, ArtistSingleData>() {
    private var artistId: Long? = null

    fun init(artistId: Long) {
        this.artistId = artistId
    }

    override fun getRefreshKey(state: PagingState<Int, ArtistSingleData>): Int? =
        state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistSingleData> {
        if (artistId == null) return LoadResult.Error(OtherRemoteException)

        val page = params.key ?: 1

        val result = client.get<ArtistPagerDataDto>(
            route = EndPoints.GetArtistAlbum.route,
            params = listOf(
                "page" to page.toString(),
                "size" to 10.toString(),
                "artistId" to artistId.toString()
            ),
            gson = gson
        ).map {
            it.list.map { dto -> dto.toArtistSingleData() }
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