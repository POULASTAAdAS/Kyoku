package com.poulastaa.network.pager_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.poulastaa.network.model.ViewArtistSong
import javax.inject.Inject

class ViewArtistPagerSource @Inject constructor() : PagingSource<Long, ViewArtistSong> {
    override fun getRefreshKey(state: PagingState<Long, ViewArtistSong>): Long? {

    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ViewArtistSong> {
        TODO("Not yet implemented")
    }
}