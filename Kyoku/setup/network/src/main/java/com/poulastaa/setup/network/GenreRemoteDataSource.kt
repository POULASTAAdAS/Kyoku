package com.poulastaa.setup.network

import com.google.gson.Gson
import com.poulastaa.core.data.network.post
import com.poulastaa.core.data.network.put
import com.poulastaa.core.domain.EndPoints
import com.poulastaa.core.domain.model.Genre
import com.poulastaa.core.domain.repository.genre.GenreId
import com.poulastaa.core.domain.repository.genre.RemoteGenreDataSource
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import com.poulastaa.core.domain.utils.asEmptyDataResult
import com.poulastaa.core.domain.utils.map
import com.poulastaa.setup.network.mappers.toGenre
import com.poulastaa.setup.network.model.req.GetGenreReq
import com.poulastaa.setup.network.model.req.StoreGenreReq
import com.poulastaa.setup.network.model.res.SuggestGenreDto
import okhttp3.OkHttpClient
import javax.inject.Inject

class GenreRemoteDataSource @Inject constructor(
    private val client: OkHttpClient,
    private val gson: Gson,
) : RemoteGenreDataSource {
    override suspend fun getGenre(
        sendList: List<GenreId>,
    ): Result<List<Genre>, DataError.Network> = client.post<GetGenreReq, SuggestGenreDto>(
        route = EndPoints.SuggestGenre.route,
        body = GetGenreReq(sendList),
        gson = gson,
    ).map {
        it.listOgGenre.map { entry ->
            entry.toGenre()
        }
    }

    override suspend fun storeGenre(
        idList: List<GenreId>,
    ): EmptyResult<DataError> = client.put<StoreGenreReq, Boolean>(
        route = EndPoints.StoreGenre.route,
        body = StoreGenreReq(idList),
        gson = gson
    ).asEmptyDataResult()
}