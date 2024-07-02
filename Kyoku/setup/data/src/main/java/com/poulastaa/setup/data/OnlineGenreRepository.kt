package com.poulastaa.setup.data

import com.poulastaa.core.domain.genre.GenreId
import com.poulastaa.core.domain.genre.GenreRepository
import com.poulastaa.core.domain.genre.RemoteGenreDataSource
import com.poulastaa.core.domain.model.Genre
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class OnlineGenreRepository @Inject constructor(
    private val remote: RemoteGenreDataSource,
    private val applicationScope: CoroutineScope,
) : GenreRepository {
    override suspend fun getGenre(
        sendIfList: List<GenreId>,
    ): Result<List<Genre>, DataError.Network> = applicationScope.async {
        remote.getGenre(sendIfList)
    }.await()

    override suspend fun storeGenre(
        genre: List<GenreId>,
    ): EmptyResult<DataError> = applicationScope.async {
        remote.storeGenre(genre)
    }.await()
}