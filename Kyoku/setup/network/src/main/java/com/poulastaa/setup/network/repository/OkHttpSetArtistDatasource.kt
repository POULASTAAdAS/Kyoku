package com.poulastaa.setup.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.setup.domain.model.DtoPrevArtist
import com.poulastaa.setup.domain.repository.set_artist.RemoteSetArtistDatasource
import com.poulastaa.setup.network.paging_source.SuggestArtistPagingSource
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

class OkHttpSetArtistDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val artist: SuggestArtistPagingSource,
) : RemoteSetArtistDatasource {
    override fun suggestArtist(query: String): Flow<PagingData<DtoPrevArtist>> {
        artist.init(query)

        return Pager(
            config = PagingConfig(
                pageSize = 15,
                enablePlaceholders = false
            ),
            initialKey = 1,
            pagingSourceFactory = { artist }
        ).flow
    }
}