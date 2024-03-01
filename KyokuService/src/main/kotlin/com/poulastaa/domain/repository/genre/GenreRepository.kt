package com.poulastaa.domain.repository.genre

import com.poulastaa.data.model.utils.UserTypeHelper
import com.poulastaa.data.model.setup.genre.StoreGenreResponse
import com.poulastaa.data.model.setup.genre.SuggestGenreReq
import com.poulastaa.data.model.setup.genre.SuggestGenreResponse

interface GenreRepository {
    suspend fun suggestGenre(
        req: SuggestGenreReq,
        countryId: Int
    ): SuggestGenreResponse

    suspend fun storeGenre(
        helper: UserTypeHelper,
        genreNameList: List<String>
    ): StoreGenreResponse
}