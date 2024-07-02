package com.poulastaa.core.domain.genre

import com.poulastaa.core.domain.model.Genre
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result

interface GenreRepository {
    suspend fun getGenre(sendIfList: List<GenreId>): Result<List<Genre>, DataError.Network>
    suspend fun storeGenre(genre: List<GenreId>): EmptyResult<DataError>
}