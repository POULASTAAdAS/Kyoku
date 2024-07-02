package com.poulastaa.core.domain.genre

import com.poulastaa.core.domain.model.Genre
import com.poulastaa.core.domain.utils.DataError
import com.poulastaa.core.domain.utils.EmptyResult
import com.poulastaa.core.domain.utils.Result

typealias GenreId = Int

interface RemoteGenreDataSource {
    suspend fun getGenre(sendList: List<GenreId>): Result<List<Genre>, DataError.Network>
    suspend fun storeGenre(idList: List<GenreId>): EmptyResult<DataError>
}