package com.poulastaa.kyoku.content.service.setup

import com.poulastaa.kyoku.content.database.repository.GenreDataSource
import com.poulastaa.kyoku.content.utils.toDtoGenre
import org.springframework.stereotype.Service


@Service
class SetupJPADatasource(
    private val genre: GenreDataSource,
) {
    fun getAllGenre() = genre.findAll().toList().sortedBy { it.popularity }.map { it.toDtoGenre() }
}