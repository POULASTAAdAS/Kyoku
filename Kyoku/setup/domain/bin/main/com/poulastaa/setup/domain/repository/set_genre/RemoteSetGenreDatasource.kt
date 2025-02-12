package com.poulastaa.setup.domain.repository.set_genre

import androidx.paging.PagingData
import com.poulastaa.core.domain.DataError
import com.poulastaa.core.domain.Result
import com.poulastaa.core.domain.model.DtoGenre
import com.poulastaa.core.domain.model.GenreId
import kotlinx.coroutines.flow.Flow

interface RemoteSetGenreDatasource {
    fun getPagingGenre(query: String): Flow<PagingData<DtoGenre>>
    suspend fun saveGenre(list: List<GenreId>): Result<List<DtoGenre>, DataError.Network>
}