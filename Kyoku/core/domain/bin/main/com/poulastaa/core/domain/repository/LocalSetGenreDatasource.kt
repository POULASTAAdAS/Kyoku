package com.poulastaa.core.domain.repository

import com.poulastaa.core.domain.model.DtoGenre

interface LocalSetGenreDatasource {
    suspend fun storeGenreLocally(list: List<DtoGenre>)
}