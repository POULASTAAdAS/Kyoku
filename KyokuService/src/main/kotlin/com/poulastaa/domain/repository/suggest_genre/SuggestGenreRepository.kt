package com.poulastaa.domain.repository.suggest_genre

import com.poulastaa.data.model.setup.suggest_genre.SuggestGenreReq
import com.poulastaa.data.model.setup.suggest_genre.SuggestGenreResponse

interface SuggestGenreRepository {
    suspend fun suggestGenre(
        req: SuggestGenreReq,
        countryId: Int
    ): SuggestGenreResponse
}