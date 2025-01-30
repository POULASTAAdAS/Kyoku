package com.poulastaa.setup.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.map
import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.EndPoints
import com.poulastaa.core.domain.model.GenreId
import com.poulastaa.core.network.ApiMethodType
import com.poulastaa.core.network.mapper.toDtoGenre
import com.poulastaa.core.network.model.UpsertGenreReq
import com.poulastaa.core.network.model.UpsertOperation
import com.poulastaa.core.network.model.UpsertReq
import com.poulastaa.core.network.req
import com.poulastaa.setup.domain.repository.set_genre.RemoteSetGenreDatasource
import com.poulastaa.setup.network.model.SaveGenreRes
import com.poulastaa.setup.network.paging_source.SuggestGenrePagingSource
import kotlinx.coroutines.flow.Flow
import okhttp3.OkHttpClient
import javax.inject.Inject

class OkHttpSetGenreDatasource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
    private val genre: SuggestGenrePagingSource,
) : RemoteSetGenreDatasource {
    override fun getPagingGenre(query: String): Flow<PagingData<DtoGenre>> {
        genre.init(query)

        return Pager(
            config = PagingConfig(
                pageSize = 15,
                initialLoadSize = 15
            ),
            initialKey = 1,
            pagingSourceFactory = { genre }
        ).flow
    }

    override suspend fun saveGenre(list: List<GenreId>): Result<List<DtoGenre>, DataError.Network> {
        val result = client.req<UpsertGenreReq, SaveGenreRes>(
            route = EndPoints.UPSERTGenre.route,
            method = ApiMethodType.POST,
            body = UpsertGenreReq(
                data = UpsertReq(
                    list = list,
                    operation = UpsertOperation.INSERT
                )
            ),
            gson = gson
        )

        return result.map { res -> res.list.map { it.toDtoGenre() } }
    }
}