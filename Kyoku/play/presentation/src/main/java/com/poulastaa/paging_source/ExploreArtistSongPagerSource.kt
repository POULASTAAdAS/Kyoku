package com.poulastaa.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.poulastaa.core.domain.model.ArtistSingleData
import com.poulastaa.core.domain.repository.explore_artist.RemoteExploreArtistDatasource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.play.domain.NoInternetException
import com.poulastaa.play.domain.OtherRemoteException
import javax.inject.Inject

class ExploreArtistSongPagerSource @Inject constructor(
    private val remote: RemoteExploreArtistDatasource
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

        val result = remote.getArtistSong(
            artistId = artistId!!,
            page = page,
            size = 10
        )

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