package com.poulastaa.kyoku.content.service.setup

import com.poulastaa.kyoku.content.database.repository.GenreDataSource
import org.springframework.stereotype.Service


@Service
class SetupJPADatasource(
    private val genre: GenreDataSource,
) {
    fun getGenre() = genre.findAll().toList()
}