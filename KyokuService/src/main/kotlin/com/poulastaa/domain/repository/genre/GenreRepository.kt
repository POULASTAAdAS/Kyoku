package com.poulastaa.domain.repository.genre

import com.poulastaa.data.model.setup.genre.StoreGenreResponse
import com.poulastaa.data.model.setup.genre.SuggestGenreReq
import com.poulastaa.data.model.setup.genre.SuggestGenreResponse
import com.poulastaa.data.model.utils.UserType

interface GenreRepository {
    suspend fun suggestGenre(
        req: SuggestGenreReq,
        countryId: Int
    ): SuggestGenreResponse

    suspend fun storeGenre(
        userType: UserType,
        userId: Long,
        genreNameList: List<String>
    ): StoreGenreResponse
}