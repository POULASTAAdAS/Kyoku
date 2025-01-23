package com.poulastaa.setup.data.repository.set_genre

import androidx.paging.PagingData
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.EmptyResult
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.asEmptyDataResult
import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.GenreId
import com.poulastaa.core.domain.repository.LocalSetGenreDatasource
import com.poulastaa.setup.domain.repository.set_genre.RemoteSetGenreDatasource
import com.poulastaa.setup.domain.repository.set_genre.SetGenreRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OnlineFirstSetGenreRepository @Inject constructor(
    private val remote: RemoteSetGenreDatasource,
    private val local: LocalSetGenreDatasource,
) : SetGenreRepository {
    override fun getPagingGenre(query: String): Flow<PagingData<DtoGenre>> =
        remote.getPagingGenre(query)

    override suspend fun saveGenre(list: List<GenreId>): EmptyResult<DataError.Network> {
        val result = remote.saveGenre(list)
        if (result is Result.Success && result.data.isNotEmpty()) local.storeGenreLocally(result.data)

        return result.asEmptyDataResult()
    }
}