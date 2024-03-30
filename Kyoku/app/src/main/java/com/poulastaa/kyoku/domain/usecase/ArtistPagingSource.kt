package com.poulastaa.kyoku.domain.usecase

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistPageReq
import com.poulastaa.kyoku.data.model.api.service.artist.ArtistPageResponse
import com.poulastaa.kyoku.data.model.api.service.home.HomeResponseStatus
import com.poulastaa.kyoku.domain.repository.ServiceRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ArtistPagingSource @Inject constructor(
    private val api: ServiceRepository
) : PagingSource<Int, ArtistPageResponse>() {
    private lateinit var name: String

    fun load(name: String) {
        this.name = name
    }

    override fun getRefreshKey(state: PagingState<Int, ArtistPageResponse>): Int? =
        state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArtistPageResponse> {
        val page = params.key ?: 1

        val req = ArtistPageReq(
            page = page,
            pageSize = params.loadSize,
            name = "Arijit Singh"
        )

        val response = api.getArtistAsPage(req = req)

        Log.d("response", response.toString())
        Log.d("req", req.toString())

        return try {
            LoadResult.Page(
                data = listOf(response),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.status.name == HomeResponseStatus.FAILURE.name) null
                else page.plus(1)
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}